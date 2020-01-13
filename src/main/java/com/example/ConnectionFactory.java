package com.example;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public final class ConnectionFactory {

    private KeyStore loadKeyStore(String path, String password) throws ExampleException {
        KeyStore keyStore;

        try {
            keyStore = KeyStore.getInstance("JKS");
        } catch (KeyStoreException e) {
            throw new ExampleException(e);
        }

        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            keyStore.load(fileInputStream, password.toCharArray());
        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {
            throw new ExampleException("Can not load keyStore from " + path, e);
        }

        return keyStore;
    }

    private TrustManagerFactory initTrustManagerFactory(KeyStore keyStore) throws ExampleException {
        TrustManagerFactory trustManagerFactory;

        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new ExampleException(e);
        }

        try {
            trustManagerFactory.init(keyStore);
        } catch (KeyStoreException e) {
            throw new ExampleException(e);
        }

        return trustManagerFactory;
    }

    private SSLSocketFactory buildSSLSocketFactory(TrustManagerFactory trustManagerFactory) throws ExampleException {
        SSLContext sslContext;

        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            throw new ExampleException(e);
        }

        try {
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            throw new ExampleException(e);
        }

        return sslContext.getSocketFactory();
    }

    private HttpsURLConnection buildConnection(URL url, SSLSocketFactory sslSocketFactory) throws ExampleException {
        HttpsURLConnection connection;

        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new ExampleException("Can not connetc to " + url.getPath(), e);
        }

        connection.setSSLSocketFactory(sslSocketFactory);
        return connection;
    }

    public HttpsURLConnection getHttpsURLConnection(URL url, String trustStorePath, String trustStorePassword) throws ExampleException {
        KeyStore keyStore = loadKeyStore(trustStorePath, trustStorePassword);
        TrustManagerFactory trustManagerFactory = initTrustManagerFactory(keyStore);
        SSLSocketFactory sslSocketFactory = buildSSLSocketFactory(trustManagerFactory);
        return buildConnection(url, sslSocketFactory);
    }
}

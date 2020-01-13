package com.example;

import javax.net.ssl.HttpsURLConnection;

public class App
{
    public static void main( String[] args ) throws ExampleException {

        HttpsURLConnection connection = new ConnectionFactory().getHttpsURLConnection(null, null, null);

    }
}

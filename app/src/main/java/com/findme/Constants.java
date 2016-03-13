package com.findme;

/**
 * Created by paul on 3/13/16.
 */
public class Constants {

    // connection timeout, in milliseconds-waiting to connect
    public static final int CONN_TIMEOUT = 60000;
    // socket timeout, in milliseconds-waiting for data
    public static final int SOCKET_TIMEOUT = 60000;

    public enum PostType {
        POST, GET, POST_JSON
    }
}

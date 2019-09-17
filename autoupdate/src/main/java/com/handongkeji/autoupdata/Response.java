package com.handongkeji.autoupdata;

public class Response {

    public static final int SUCCESS = 1;
    public static final int ERROR = 0;

    String message;
    int status;

    public Response(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public Response() {
    }
}

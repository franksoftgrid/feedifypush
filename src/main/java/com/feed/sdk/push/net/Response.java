package com.feed.sdk.push.net;

import android.graphics.Bitmap;

/**
 * Response representation of data returned by FeedNet
 */
public class Response {
    boolean isError;
    String data;
    Bitmap image;
    String errorMessage;


    public Response(boolean isError, String errorMessage, String data) {
        this.isError = isError;
        this.data = errorMessage;
        this.data = data;

    }

    public Response(boolean isError, String errorMessage, Bitmap data) {
        this.isError = isError;
        this.data = errorMessage;
        this.image = data;

    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getData() {
        return data;
    }

    public Bitmap getBitmap() {
        return this.image;
    }

}

package com.feed.sdk.push.common;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class Assets {

    public static String textFileToString(Context context, String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer);
    }
}

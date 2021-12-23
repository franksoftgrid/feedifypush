package com.feed.sdk.push.common;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class Assets {
    /**
     * reads text file and returns all content as String
     *
     * @param context  - context to be used
     * @param fileName - name of the file that needs to be read from assets folder
     */
    public static String textFileToString(Context context, String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer);
    }
}

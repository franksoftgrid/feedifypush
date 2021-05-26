package com.feed.sdk.push.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cache {

    public static void save_bitmap(Context context, String key, Bitmap bitmap) {
        key = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
        Logs.e("=========key====save======", key);
        if (bitmap != null) {
            try {
                File cacheDir = context.getCacheDir();
                File file = new File(cacheDir, key);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getBitmap(Context context, String key) {
        key = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
        Logs.e("=========key====take======", key);

        FileInputStream fis = null;
        try {
            File cacheDir = context.getCacheDir();
            File file = new File(cacheDir, key);
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        }
        if (fis != null) {
            return BitmapFactory.decodeStream(fis);
        }
        return null;

    }

    public static long getLastModified(Context context, String key) {
        return new File(context.getCacheDir(), key).lastModified();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}

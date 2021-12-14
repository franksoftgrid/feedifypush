package com.feed.sdk.push.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;

import com.feed.sdk.push.common.Logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by krish on 16/12/16. For internet purpose
 */
public class FeedNet {


    private Context ctx;

    private FeedNet(Context ctx) {
        this.ctx = ctx;
    }


    public static FeedNet getInstance(Context ctx) {
        return new FeedNet(ctx);
    }

    public void downloadPush() {

    }


    public void executeRequest(final Request request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (request.getRequestMethod()) {
                    case Request.REQUEST_GET:
                        processGet(request);
                        break;
                    case Request.REQUEST_POST:
                        processPost(request);
                        break;
                    case Request.REQUEST_IMAGE:
                        processGetImage(request);
                }
            }
        }).start();

    }

    public Bitmap getImage(Request req) {
        if (!isNetworkAvailable()) {
            return null;
        }

        try {
            URL url = new URL(req.getRequestUrl());
            URLConnection urlConn = url.openConnection();
            InputStream in = urlConn.getInputStream();
            Bitmap data = getBitmap(in);
            in.close();
            return data;

        } catch (Exception ex) {

            return null;

        }
    }

    public String getHttpResponse(Request req) {
        if (!isNetworkAvailable()) {
            return null;
        }

        HttpURLConnection con = getHttpURLConnection(req);
        if (con != null) {
            try {
                // TODO: Add support for 301 and 302 redirect response.
                int resCode = con.getResponseCode();
                if (resCode == 200 || resCode == 206) {
                    InputStream in = con.getInputStream();
                    int code = con.getResponseCode();
                    String data = getData(in);

                    return data;
                } else {

                    return null;
                }
            } catch (Exception ex) {

                return null;

            } finally {
                try {
                    if (con != null)
                        con.disconnect();
                } catch (Exception ex) {
                }
            }
        }
        return null;
    }

    /**
     * Special get to get images
     *
     * @param request
     */
    private void processGetImage(Request request) {

        if (!isNetworkAvailable()) {

            Response resp = new Response(true, "Network not available", "");
            sendResponse(request.getListener(), resp);
            return;
        }


        try {
            URL url = new URL(request.getRequestUrl());
            URLConnection urlConn = url.openConnection();
            InputStream in = urlConn.getInputStream();
            Bitmap data = getBitmap(in);
            in.close();
            Response resp = new Response(false, null, data);
            sendResponse(request.getListener(), resp);

        } catch (Exception ex) {

            Response resp = new Response(true, "There seems to be some problem with your network. Please try again after some time.", "");
            sendResponse(request.getListener(), resp);

            ex.printStackTrace();

        } finally {

        }


    }

    /**
     * Normal get request
     *
     * @param request
     */
    private void processGet(Request request) {
        if (!isNetworkAvailable()) {
            Response resp = new Response(true, "Network not available", "");
            sendResponse(request.getListener(), resp);
            return;
        }
        HttpURLConnection con = getHttpURLConnection(request);
        if (con != null) {
            try {
                if (con.getResponseCode() == 200) {
                    InputStream in = con.getInputStream();
                    int code = con.getResponseCode();
                    String data = getData(in);


                    Response resp = new Response(false, null, data);
                    sendResponse(request.getListener(), resp);
                } else {

                    Response resp = new Response(true, "Invalid response code", "");
                    sendResponse(request.getListener(), resp);
                }
            } catch (Exception ex) {

                Response resp = new Response(true, "There seems to be some problem with your network. Please try again after some time.", "");
                sendResponse(request.getListener(), resp);

                ex.printStackTrace();

            } finally {
                try {
                    if (con != null)
                        con.disconnect();
                } catch (Exception ex) {
                }
            }

        }

    }

    /**
     * if network is availalbe
     *
     * @return true if yes
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Post request
     *
     * @param request
     */
    private void processPost(Request request) {
        if (!isNetworkAvailable()) {

            Response resp = new Response(true, "Network not available", "");
            sendResponse(request.getListener(), resp);

            return;
        }

        String data;
        HttpURLConnection con = getHttpURLConnection(request);
        if (con != null) {
            try {
                OutputStream out = con.getOutputStream();
                out.write(request.getPostData().getBytes());
                out.flush();
                if (con.getResponseCode() == 200) {
                    InputStream in = con.getInputStream();
                    data = getData(in);

                    Response resp = new Response(false, null, data);
                    sendResponse(request.getListener(), resp);


                } else {

                    Response resp = new Response(true, "Invalid response code", "");
                    sendResponse(request.getListener(), resp);

                    //  listener.onError(new MAdeptNetworkError(request.getRequestId(), "Invalid Response Code"));
                }
            } catch (Exception ex) {

                Response resp = new Response(true, "There seems to be some problem with your network. Please try again after some time.", "");
                //request.getListener().onResponse(resp);
                sendResponse(request.getListener(), resp);


            } finally {
                try {
                    if (con != null)
                        con.disconnect();
                } catch (Exception ex) {
                }
            }

        }

    }


    private HttpURLConnection getHttpURLConnection(Request req) {
        try {
            String resourceURL = req.getRequestUrl();

            URL url = new URL(req.getRequestUrl());
            HttpURLConnection con = null;
            if (resourceURL.toLowerCase().indexOf("https") != -1)
                con = (HttpsURLConnection) url.openConnection();
            else {
                con = (HttpURLConnection) url.openConnection();
            }
            con.setRequestMethod(req.getRequestHTTPMethod());
            Map<String, String> headers = req.getHeaders();
            if (headers != null) {
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    con.addRequestProperty(key, headers.get(key));
                }
            }
            con.setDoInput(true);
            con.setDoOutput(true);

            return con;

        } catch (MalformedURLException exp) {
            Logs.e(exp.getMessage());
        } catch (IOException ex) {
            Logs.e(ex.getMessage());
        }
        return null;
    }


    private String getData(InputStream in) throws Exception {
        if (in == null) {

            throw new Exception("Input stream can not be null");

        }
        int c = -1;
        StringBuffer r = new StringBuffer();

        try {

            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null)
                r.append(line);
            isr.close();
            reader.close();
            return r.toString();
        } catch (Exception e) {
            throw new Exception("Error while reading response");
        }

    }

    private Bitmap getBitmap(InputStream in) throws Exception {
        if (in == null) {
            throw new Exception("Input stream can not be null");
        }
        int c = -1;
        StringBuffer r = new StringBuffer();
        try {
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            throw new Exception("Error while reading response");
        }

    }

    private void sendResponse(final ResponseListener listener, final Response resp) {
        if (listener != null) {
            final long threadId = Thread.currentThread().getId();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                Handler mainThread = new Handler(Looper.getMainLooper());
                mainThread.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                listener.onResponse(resp);
                            }
                        });
                return;
            } else {
                listener.onResponse(resp);
            }
        }
    }
}

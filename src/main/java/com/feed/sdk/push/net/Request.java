package com.feed.sdk.push.net;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Map;

/**
 *  Request class to represent a Request to FeedNet class
 */
public class Request {

    public final static int REQUEST_GET = 0;
    public final static int REQUEST_POST = 1;
    public final static int REQUEST_IMAGE =  2;

    private int method;
    private ResponseListener listener;
    private Map<String, String> params;
    private String url;
    private JSONObject paramsObject;

    private Map<String, String> headers;

    public Request(String url, int method, Map<String, String> params, ResponseListener listener ){
        this.url = url;
        this.method = method;
        this.params = params;
        this.listener = listener;
    }

    public Request(String url, int method, JSONObject params, ResponseListener listener ){
        this.url = url;
        this.method = method;
        this.paramsObject = params;
        this.listener = listener;
    }

    public void setHeaders(Map<String,String> headers){
        this.headers = headers;
    }

    public Map<String,String> getHeaders(){
        return headers;
    }

    public int getRequestMethod(){
        return method;
    }

    public String getRequestHTTPMethod(){

         switch(this.method){
             case Request.REQUEST_GET:
             case Request.REQUEST_IMAGE:
                 return "GET";


         }
        return "POST";
    }

    public String getRequestUrl(){
        return this.url;
    }

    private String getPostParams(){
        String rtn="";
        if (params != null){
            for(String key:params.keySet()){
                try {
                    rtn+= URLEncoder.encode(key,"UTF-8")+"="+URLEncoder.encode(params.get(key),"UTF-8")+"&";
                }catch(Exception e){
                }
            }
        }else{
            rtn = paramsObject.toString();
        }

        return rtn;
    }

    public String getPostData(){
        return getPostParams();
    }

    public ResponseListener getListener(){
        return this.listener;
    }
}

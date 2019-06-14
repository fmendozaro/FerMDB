package com.fer_mendoza.fermdb.utils;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class NetworkUtils {

    public static URL parseURL(String url, HashMap<String, String> params){
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .path(url);

        for (String key: params.keySet()) {
            builder.appendQueryParameter(key, params.get(key));
        }

        URL wellUrl = null;
        try{
            wellUrl = new URL(builder.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return wellUrl;
    }
}
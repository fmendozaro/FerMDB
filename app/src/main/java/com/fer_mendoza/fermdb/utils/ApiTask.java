package com.fer_mendoza.fermdb.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

import com.fer_mendoza.fermdb.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class ApiTask extends AsyncTask<URL, Void, String> {

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

    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) urls[0].openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(s != null && !s.isEmpty()){
            System.out.println("s = " + s);
        }
    }
}

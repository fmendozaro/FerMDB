package com.fer_mendoza.fermdb;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class ApiTask extends AsyncTask<URL, Void, String> {

    private OnTaskCompleted onTaskCompleted;
    private String type;

    ApiTask(OnTaskCompleted onTaskCompleted, String type) {
        this.onTaskCompleted = onTaskCompleted;
        this.type = type;
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
            onTaskCompleted.onTaskCompleted(s, this.type, null);
        }
    }
}
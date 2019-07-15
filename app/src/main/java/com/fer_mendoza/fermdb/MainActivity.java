package com.fer_mendoza.fermdb;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.TextView;

import com.fer_mendoza.fermdb.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public String jsonString = "";
    private RecyclerView movieList;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        HashMap<String, String> params = new HashMap<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        movieList = (RecyclerView) findViewById(R.id.movie_list_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        movieList.setLayoutManager(layoutManager);
        movieList.setHasFixedSize(true);

        params.put("api_key", getApplicationContext().getString(R.string.THE_MOVIE_DB_API_TOKEN));
        params.put("sort_by","popularity.desc");

        new ApiTask().execute(NetworkUtils.parseURL("api.themoviedb.org/3/discover/movie", params));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ApiTask extends AsyncTask<URL, Void, String> {

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
                jsonString = s;
                parseMovies();
            }
        }
    }

    public void parseMovies() {
        JSONObject movieDataJson = null;
        try {
            movieDataJson = new JSONObject(jsonString);
            JSONArray movieDataArray = movieDataJson.getJSONArray("results");
            mAdapter = new MovieAdapter(10, movieDataArray);
            movieList.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}


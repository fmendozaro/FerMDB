package com.fer_mendoza.fermdb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fer_mendoza.fermdb.utils.NetworkUtils;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private RecyclerView movieList;
    private MovieAdapter mAdapter;
    private HashMap<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movieList = (RecyclerView) findViewById(R.id.movie_list_container);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        movieList.setLayoutManager(layoutManager);

        params.put("api_key", getApplicationContext().getString(R.string.THE_MOVIE_DB_API_TOKEN));
        getMoviesData("popular");

    }

    @Override
    public void onTaskCompleted(String jsonString) {
        parseMovies(jsonString);
    }

    public void getMoviesData(String segment){
        ApiTask apiTask = new ApiTask(MainActivity.this);
        apiTask.execute(NetworkUtils.parseURL("api.themoviedb.org/3/movie/"+segment, params));
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
        String segment = "";

        //noinspection SimplifiableIfStatement
        if (id == R.id.popular) {
            getSupportActionBar().setTitle("Popular");
            segment = "popular";
        }else if (id == R.id.rating){
            getSupportActionBar().setTitle("Highest rated");
            segment = "top_rated";
        }
        getMoviesData(segment);
        return super.onOptionsItemSelected(item);
    }

    public void parseMovies(String jsonString) {
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


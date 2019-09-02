package com.fer_mendoza.fermdb;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fer_mendoza.fermdb.db.AppDb;
import com.fer_mendoza.fermdb.utils.AppExecutors;
import com.fer_mendoza.fermdb.utils.NetworkUtils;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private RecyclerView movieList;
    private MovieAdapter mAdapter;
    private AppDb appDb;
    private JSONArray favResults = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appDb = AppDb.getInstance(getApplicationContext());

        movieList = (RecyclerView) findViewById(R.id.movie_list_container);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        movieList.setLayoutManager(layoutManager);

        params.put("api_key", getApplicationContext().getString(R.string.THE_MOVIE_DB_API_TOKEN));
        getMoviesData("popular");

    }

    @Override
    public void onTaskCompleted(String jsonString, String type) {
        if(type.equals("favs")){
            addToResults(jsonString);
            parseMovies(favResults);
        }else{
            parseMovies(jsonString);
        }
    }

    public void addToResults(String jsonString){
        try {
            favResults.put(new JSONObject(jsonString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getMoviesData(String segment){
        ApiTask apiTask = new ApiTask(MainActivity.this, "movie");
        apiTask.execute(NetworkUtils.parseURL("api.themoviedb.org/3/movie/"+segment, params));
    }

    public void getMoviesData(List<Long> ids){
        for (long id : ids) {
            ApiTask apiTask = new ApiTask(MainActivity.this, "favs");
            apiTask.execute(NetworkUtils.parseURL("api.themoviedb.org/3/movie/"+id, params));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        favResults = new JSONArray();
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
            getSupportActionBar().setTitle("Best rated");
            segment = "top_rated";
        }else if (id == R.id.sort_fav){
            getSupportActionBar().setTitle("Favorites");
            retrieveFavs();
            return super.onOptionsItemSelected(item);
        }
        getMoviesData(segment);
        return super.onOptionsItemSelected(item);
    }

    public void parseMovies(String jsonString) {
        JSONObject movieDataJson;
        try {
            movieDataJson = new JSONObject(jsonString);
            mAdapter = new MovieAdapter(10, movieDataJson.getJSONArray("results"));
            movieList.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseMovies(JSONArray favResults) {
        mAdapter = new MovieAdapter(10, favResults);
        movieList.setAdapter(mAdapter);
    }

    private void retrieveFavs(){
        final LiveData<List<Long>> ids = appDb.favoriteDao().findAllIds();
        ids.observe(this, new Observer<List<Long>>() {
            @Override
            public void onChanged(@Nullable List<Long> ids) {
                getMoviesData(ids);
            }
        });
    }
}


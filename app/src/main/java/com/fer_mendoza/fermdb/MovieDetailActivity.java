package com.fer_mendoza.fermdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intentClicked = getIntent();
        TextView title = findViewById(R.id.movie_title);
        TextView desc = findViewById(R.id.movie_description);
        TextView rating = findViewById(R.id.movie_rating);
        TextView release = findViewById(R.id.movie_release);
        ImageView poster = findViewById(R.id.movie_poster);
        
        if(intentClicked.hasExtra("movieData")){
            String jsonArray = intentClicked.getStringExtra("movieData");
            try {
                JSONObject movieData = new JSONObject(jsonArray);
                String posterPath = "http://image.tmdb.org/t/p/w500" + movieData.getString("poster_path");

                title.setText(movieData.getString("title"));
                desc.setText(movieData.getString("overview"));
                rating.setText("Average Rating: " + movieData.getString("vote_average"));
                release.setText("Release Date: " + movieData.getString("release_date"));
                // stage 2:  trailer and users reviews
                Picasso.get().load(posterPath).into(poster);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}

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
import android.widget.VideoView;

import com.fer_mendoza.fermdb.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity implements OnTaskCompleted {

    @Override
    public void onTaskCompleted(String response, String type) {
        switch (type){
            case "videos":
//                VideoView v = (VideoView) findVfiewById(R.id.YoutubeVideoView);
//
//                v.setVideoURI(Uri.parse(“rtsp://v4.cache3.c.youtube.com/CjYLENy73wIaLQlW_ji2apr6AxMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYOr_86Xm06e5UAw=/0/0/0/video.3gp”));
//
//                v.setMediaController(new MediaController(this)); //sets MediaController in the video view
//
//// MediaController containing controls for a MediaPlayer
//
//                v.requestFocus();//give focus to a specific view
//
//                v.start();//starts the video

                break;
            case "reviews":
//                response.
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params.put("api_key", getApplicationContext().getString(R.string.THE_MOVIE_DB_API_TOKEN));

        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intentClicked = getIntent();
        TextView title = findViewById(R.id.movie_title);
        TextView desc = findViewById(R.id.movie_description);
        TextView rating = findViewById(R.id.movie_rating);
        TextView release = findViewById(R.id.movie_release);
        ImageView poster = findViewById(R.id.movie_poster);
        VideoView video = findViewById(R.id.movie_trailer);
        ApiTask getVideosTask = new ApiTask(this, "videos");
        ApiTask getReviewsTask = new ApiTask(this, "reviews");

        if(intentClicked.hasExtra("movieData")){
            String jsonArray = intentClicked.getStringExtra("movieData");
            try {
                JSONObject movieData = new JSONObject(jsonArray);

                getVideosTask.execute(NetworkUtils.parseURL(String.format("api.themoviedb.org/3/movie/%s/videos", movieData.getString("id")) ,params));
                getReviewsTask.execute(NetworkUtils.parseURL(String.format("api.themoviedb.org/3/movie/%s/reviews", movieData.getString("id")) ,params));

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

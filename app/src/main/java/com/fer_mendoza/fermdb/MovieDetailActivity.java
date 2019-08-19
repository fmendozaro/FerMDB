package com.fer_mendoza.fermdb;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fer_mendoza.fermdb.db.AppDb;
import com.fer_mendoza.fermdb.db.Favorite;
import com.fer_mendoza.fermdb.utils.AppExecutors;
import com.fer_mendoza.fermdb.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity implements OnTaskCompleted {

    private AppDb appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDb = AppDb.getInstance(getApplicationContext());
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intentClicked = getIntent();
        TextView title = findViewById(R.id.movie_title);
        TextView desc = findViewById(R.id.movie_description);
        TextView rating = findViewById(R.id.movie_rating);
        TextView release = findViewById(R.id.movie_release);
        ImageView poster = findViewById(R.id.movie_poster);
        ImageView favBtn = findViewById(R.id.fav_movie);
//        favBtn.setImageResource(R.drawable.);

        ApiTask getVideosTask = new ApiTask(this, "videos");
        ApiTask getReviewsTask = new ApiTask(this, "reviews");

        if(intentClicked.hasExtra("movieData")){
            String jsonArray = intentClicked.getStringExtra("movieData");
            try {
                final JSONObject movieData = new JSONObject(jsonArray);

                getVideosTask.execute(NetworkUtils.parseURL(String.format("api.themoviedb.org/3/movie/%s/videos", movieData.getString("id")) ,params));
                getReviewsTask.execute(NetworkUtils.parseURL(String.format("api.themoviedb.org/3/movie/%s/reviews", movieData.getString("id")) ,params));

                String posterPath = "http://image.tmdb.org/t/p/w500" + movieData.getString("poster_path");

                title.setText(movieData.getString("title"));
                desc.setText(movieData.getString("overview"));
                rating.setText("Average Rating: " + movieData.getString("vote_average"));
                release.setText("Release Date: " + movieData.getString("release_date"));
                // stage 2:  trailer and users reviews
                Picasso.get().load(posterPath).into(poster);

                favBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("favBtn pressed");
                        try {
                            final Favorite fav = new Favorite(Long.parseLong(movieData.getString("id")));
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    appDb.favoriteDao().insert(fav);
                                    finish();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void openWebsite(String url){
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    public void onTaskCompleted(String response, String type) {
        try {
            JSONObject movieData = new JSONObject(response);
            final JSONArray dataArray = movieData.getJSONArray("results");
            TextView movieTrailers = findViewById(R.id.movie_trailers);
            TextView reviewsTxt = findViewById(R.id.movie_reviews);
            switch (type){
                case "videos":
                    movieTrailers.setText("Trailer");
                    movieTrailers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                openWebsite("https://www.youtube.com/watch?v=" + dataArray.getJSONObject(0).getString("key"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case "reviews":
                    String reviewContent = "";
                    for (int i=0; i < dataArray.length(); i++) {
                        reviewContent += String.format("Author: %s \n" +
                                "Review: %s", dataArray.getJSONObject(i).getString("author"), dataArray.getJSONObject(i).getString("content"));
                    }
                    reviewsTxt.setText(String.format("List of Reviews \n\n%s", reviewContent));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public void retrieveLike(long movieId){
//        final LiveData<Favorite> favorite = appDb.favoriteDao().findOne(movieId);
//        favorite.observe(this, new Observer<Favorite>() {
//            @Override
//            public void onChanged(@Nullable Favorite favorite) {
////                mAdapter.setFavorite(favorite)
//            }
//        });
//    }

}

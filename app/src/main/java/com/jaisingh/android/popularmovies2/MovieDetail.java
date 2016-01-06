package com.jaisingh.android.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jaisingh.android.popularmovies2.MovieModel.MovieModel;
import com.jaisingh.android.popularmovies2.QueryServer.Connection;
import com.jaisingh.android.popularmovies2.QueryServer.RetrofitAPI;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetail extends AppCompatActivity {
    String id, api_key;
    ImageView img;
    TextView title, synopsis, rating, release;
    RetrofitAPI con;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        pbar = (ProgressBar)findViewById(R.id.pbar_detail);
        pbar.setVisibility(View.VISIBLE);
        api_key = getResources().getString(R.string.api_key);
        img = (ImageView)findViewById(R.id.movieImage);
        title = (TextView)findViewById(R.id.title);
        synopsis = (TextView)findViewById(R.id.synopsis);
        rating = (TextView)findViewById(R.id.rating);
        release = (TextView)findViewById(R.id.release);
        Intent it = getIntent();
        id = String.valueOf(it.getSerializableExtra("id"));
        con = Connection.connectToServer();
        //Toast.makeText(MovieDetail.this, id+ " API KEY: "+api_key, Toast.LENGTH_SHORT).show();
        loadMovie(id);
    }

    private void loadMovie(String id) {
        //Toast.makeText(MovieDetail.this, "Requesting", Toast.LENGTH_SHORT).show();
        con.getMovieInfo(api_key, id, new Callback<MovieModel>() {
            @Override
            public void success(MovieModel movieModel, Response response) {
                pbar.setVisibility(View.INVISIBLE);
                title.setText(movieModel.getOriginalTitle());
                rating.setText("Rating: "+movieModel.getVoteAverage().toString());
                release.setText("Release Date: "+movieModel.getReleaseDate());
                synopsis.setText(movieModel.getOverview());

                String base_url = "http://image.tmdb.org/t/p/original/";
                String posterPath = movieModel.getPosterPath();
                String url = base_url+posterPath;
                Picasso.with(getApplicationContext()).load(url)
                        .resize((int)(MainActivity.width*0.8), (int)(MainActivity.width*0.8))
                        .centerCrop()
                        .into(img);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MovieDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

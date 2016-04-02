package com.jaisingh.android.popularmovies2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaisingh.android.popularmovies2.MovieModel.MovieModel;
import com.jaisingh.android.popularmovies2.MovieModel.ReviewsModel;
import com.jaisingh.android.popularmovies2.QueryServer.Connection;
import com.jaisingh.android.popularmovies2.QueryServer.RetrofitAPI;
import com.jaisingh.android.popularmovies2.Trailers.Result;
import com.jaisingh.android.popularmovies2.Trailers.TrailerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetail extends AppCompatActivity {
    String id, api_key;
    ImageView img;
    TextView title, synopsis, rating, release;
    RetrofitAPI con;
    ListView lst, reviews_lst;
    String posterPath = "dummy";
    ArrayAdapter<String> adptr;
    ArrayAdapter<String> reviews_adptr;
    ArrayList<String> list_trailers, urls_trailers, list_reviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
       //pbar = (ProgressBar)findViewById(R.id.pbar_detail);
        //pbar.setVisibility(View.VISIBLE);
        api_key = getResources().getString(R.string.api_key);
        img = (ImageView)findViewById(R.id.movieImage);
        title = (TextView)findViewById(R.id.title);
        synopsis = (TextView)findViewById(R.id.synopsis);
        rating = (TextView)findViewById(R.id.rating);
        release = (TextView)findViewById(R.id.release);
        lst = (ListView)findViewById(R.id.list_trailers);
        reviews_lst = (ListView)findViewById(R.id.list_reviews);
        list_trailers = new ArrayList<String>();
        list_reviews = new ArrayList<String>();
        urls_trailers = new ArrayList<String>();
        adptr = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, list_trailers);
        reviews_adptr = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, list_reviews);
        lst.setAdapter(adptr);
        reviews_lst.setAdapter(reviews_adptr);

        Intent it = getIntent();
        id = String.valueOf(it.getSerializableExtra("id"));
        con = Connection.connectToServer();
        //Toast.makeText(MovieDetail.this, id+ " API KEY: "+api_key, Toast.LENGTH_SHORT).show();
        loadMovie(id);
        loadVideos(id);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + urls_trailers.get(position))));
            }
        });
        lst.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        getReviews();
    }

    private void loadVideos(String id) {
        con.getTrailers(api_key, id, new Callback<TrailerModel>() {
            @Override
            public void success(TrailerModel trailerModel, Response response) {
                for(Result r: trailerModel.getResults())
                    {
                        //Toast.makeText(MovieDetail.this, r.getKey(), Toast.LENGTH_SHORT).show();
                        list_trailers.add("Trailer " + (list_trailers.size() + 1));
                        urls_trailers.add(r.getKey());
                    }
                adptr.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MovieDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMovie(String id) {
        //Toast.makeText(MovieDetail.this, "Requesting", Toast.LENGTH_SHORT).show();
        con.getMovieInfo(api_key, id, new Callback<MovieModel>() {
            @Override
            public void success(MovieModel movieModel, Response response) {
                title.setText(movieModel.getOriginalTitle());
                rating.setText("Rating: " + movieModel.getVoteAverage().toString());
                release.setText("Release Date: " + movieModel.getReleaseDate());
                posterPath = movieModel.getPosterPath();
//                Toast.makeText(MovieDetail.this, "Load: "+posterPath, Toast.LENGTH_SHORT).show();
                synopsis.setText(movieModel.getOverview());

                String base_url = "http://image.tmdb.org/t/p/w780/";
                String posterPath = movieModel.getPosterPath();
                String url = base_url + posterPath;
                Picasso.with(getApplicationContext()).load(url)
                        .resize((int) (MainActivity.width * 0.4), (int) (MainActivity.width * 0.6))
                        .centerCrop()
                        .into(img);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MovieDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addToFavorites(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("favorites", MODE_PRIVATE);
        SharedPreferences poster_pref = getApplication().getSharedPreferences("poster_path", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        SharedPreferences.Editor poster_editor = poster_pref.edit();
        if ( pref.contains(id) ) {
            Toast.makeText(MovieDetail.this, "Already in favorites", Toast.LENGTH_SHORT).show();
            return;
        }
        editor.putInt(id, 1);
        poster_editor.putString(id, posterPath);
        Toast.makeText(MovieDetail.this, "Poster Path: " + posterPath, Toast.LENGTH_SHORT).show();
        Toast.makeText(MovieDetail.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
        editor.commit();
        poster_editor.commit();
    }
    public void getReviews()
    {
        con = Connection.connectToServer();
        con.getReviews(api_key, id, new Callback<ReviewsModel>() {
            @Override
            public void success(ReviewsModel reviewsModel, Response response) {
                Toast.makeText(MovieDetail.this, "Success "+reviewsModel.getTotalResults()+" "+response.getReason(), Toast.LENGTH_SHORT).show();
                for (com.jaisingh.android.popularmovies2.MovieModel.Result r : reviewsModel.getResults())
                    {
                        list_reviews.add(r.getContent());
                        Toast.makeText(MovieDetail.this, "XXXXXXXXXXXX"+r.getContent(), Toast.LENGTH_SHORT).show();
                        reviews_adptr.notifyDataSetChanged();
                    }
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MovieDetail.this, "FAIL", Toast.LENGTH_SHORT).show();
            }
        });
        reviews_adptr.notifyDataSetChanged();
    }
}

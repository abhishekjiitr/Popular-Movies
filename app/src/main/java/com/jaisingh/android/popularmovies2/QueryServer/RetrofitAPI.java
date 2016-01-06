package com.jaisingh.android.popularmovies2.QueryServer;

import com.jaisingh.android.popularmovies2.MainActivity;
import com.jaisingh.android.popularmovies2.MovieModel.MovieModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by admin on 02-01-2016.
 */
public interface RetrofitAPI
{
    MainActivity m = new MainActivity();
    @GET("/movie/popular")
    void getPopularMovies(@Query("api_key") String KEY, Callback<ResponseModel> callback);

    @GET("/movie/top_rated")
    void getTopRatedMovies(@Query("api_key") String KEY, Callback<ResponseModel> callback);

    @GET("/movie/{id}")
    void getMovieInfo(@Query("api_key") String KEY, @Path("id") String id, Callback<MovieModel> Moviecallback);

}

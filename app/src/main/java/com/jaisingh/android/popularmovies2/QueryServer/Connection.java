package com.jaisingh.android.popularmovies2.QueryServer;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by admin on 02-01-2016.
 */
public class Connection
{
    public static RetrofitAPI connectToServer()
    {
        RestAdapter.Builder builder=new RestAdapter.Builder();
        builder.setEndpoint("http://api.themoviedb.org/3/");
        builder.setClient(new OkClient(new OkHttpClient()));
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter adptr=builder.build();
        RetrofitAPI con= adptr.create(RetrofitAPI.class);
        return con;
    }
}

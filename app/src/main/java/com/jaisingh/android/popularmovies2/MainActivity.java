package com.jaisingh.android.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaisingh.android.popularmovies2.QueryServer.Connection;
import com.jaisingh.android.popularmovies2.QueryServer.ResponseModel;
import com.jaisingh.android.popularmovies2.QueryServer.RetrofitAPI;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    public static RetrofitAPI con;
    ResponseModel respModel;
    public String api_key;
    ImageView img, img2;
    TextView lbl;
    GridView gridView;
    static int height, width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        api_key = getResources().getString(R.string.api_key);
        gridView = (GridView)findViewById(R.id.gridView);
        getPopularMovies(gridView);
        gridView.setOnItemClickListener(gridListener);
    }
    AdapterView.OnItemClickListener gridListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it = new Intent(getApplicationContext(), MovieDetail.class ) ;
            it.putExtra("id", respModel.getResults().get(position).getId());
            startActivity(it);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void getPopularMovies(View v)
    {

        con= Connection.connectToServer();
        con.getPopularMovies(api_key, new Callback<ResponseModel>() {
            @Override
            public void success(ResponseModel responseModel, Response response) {
                respModel = responseModel;
                loadGridView(respModel);
                // Toast.makeText(MainActivity.this, responseModel.status + ";" + responseModel.message, Toast.LENGTH_SHORT).show();
                //lbl.setText(responseModel.message);
                Log.i("tag", responseModel.message);
                Log.i("tag", "" + responseModel.getTotalResults());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.i("ERROR", error.toString());
            }
        });
    }
    public void getTopRatedMovies(View v)
    {

        con= Connection.connectToServer();
        con.getTopRatedMovies(api_key, new Callback<ResponseModel>() {
            @Override
            public void success(ResponseModel responseModel, Response response) {
                respModel = responseModel;
                loadGridView(respModel);
                // Toast.makeText(MainActivity.this, responseModel.status + ";" + responseModel.message, Toast.LENGTH_SHORT).show();
                //lbl.setText(responseModel.message);
                Log.i("tag", responseModel.message);
                Log.i("tag", "" + responseModel.getTotalResults());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.i("ERROR", error.toString());
            }
        });
    }

    private void loadGridView(ResponseModel responseModel) {
        List<com.jaisingh.android.popularmovies2.QueryServer.Response> listResponse = responseModel.getResults();
        //Toast.makeText(MainActivity.this, "" + listResponse.size(), Toast.LENGTH_SHORT).show();
        ImageAdapter imgAdapter = new ImageAdapter(this, listResponse);
        gridView.setAdapter(imgAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.popular_movies) {
            getPopularMovies(lbl);
            return true;
        }
        else if (id == R.id.top_rated)
        {
            getTopRatedMovies(lbl);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

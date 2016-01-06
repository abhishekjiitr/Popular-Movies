package com.jaisingh.android.popularmovies2;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.jaisingh.android.popularmovies2.QueryServer.Response;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 06-01-2016.
 */
public class ImageAdapter extends ArrayAdapter<Response>
{
    private List<Response> resp;
    private Context mContext;
    private int length;
    public ImageAdapter(Activity c, List<Response> resp) {
        super(c, 0, resp);

        mContext = c;
        this.resp = resp;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
                      //   imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        // Picasso.with(this.mContext).load("http://www.bathchronicle.co.uk/user/lw-avatar/3541022/profileSmall1407142824456.png").into(imageView);

        String base_url = "http://image.tmdb.org/t/p/original/";
        Response movie = resp.get(position);
        String posterPath = movie.getPosterPath();
        String url = base_url+posterPath;
        Picasso.with(this.mContext).load(url)
                .resize(MainActivity.width/2, MainActivity.width/2)
                .centerCrop()
                .into(imageView);

        //       imageView.setImageResource(mThumbIds[position]);


        return imageView;
    }

}

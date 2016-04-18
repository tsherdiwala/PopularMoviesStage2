package com.knoxpo.popularmoviesstage1.viewholders;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.knoxpo.popularmoviesstage1.R;
import com.knoxpo.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Tejas Sherdiwala on 3/6/2016.
 */
public class MovieVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView mMovieIV;
    private Activity mActivity;
    private Movie mMovie;

    public interface Callback{
        void onMovieClicked(Movie movie);
    }

    private Callback mCallback;

    public MovieVH(Activity activity, View itemView) {
        super(itemView);
        mActivity = activity;
        mCallback = (Callback) activity;
        mMovieIV = (ImageView)itemView.findViewById(R.id.iv_movie);
        itemView.setOnClickListener(this);
    }

    public void bindMovie(Movie movie){
        mMovie = movie;

        Picasso
                .with(mActivity)
                .load(movie.getPosterPath())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(mMovieIV);
    }

    @Override
    public void onClick(View v) {
        if(mMovie != null){
            mCallback.onMovieClicked(mMovie);
        }
    }
}

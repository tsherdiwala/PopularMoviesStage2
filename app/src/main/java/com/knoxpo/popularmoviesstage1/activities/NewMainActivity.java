package com.knoxpo.popularmoviesstage1.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.knoxpo.popularmoviesstage1.R;
import com.knoxpo.popularmoviesstage1.dailogs.ReviewDialog;
import com.knoxpo.popularmoviesstage1.dailogs.VideoDialog;
import com.knoxpo.popularmoviesstage1.fragments.DetailFragment;
import com.knoxpo.popularmoviesstage1.fragments.MovieListFragment;
import com.knoxpo.popularmoviesstage1.model.Movie;
import com.knoxpo.popularmoviesstage1.viewholders.MovieVH;

import java.util.ArrayList;

/**
 * Created by Tejas Sherdiwala on 4/17/2016.
 */
public class NewMainActivity
        extends SingleFragmentActivity
        implements MovieVH.Callback,
        DetailFragment.Callback {

    @Override
    public Fragment getContentFragment() {
        return new MovieListFragment();
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onMovieClicked(Movie movie) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            //activity is single paned
            Intent detailIntent = new Intent(
                    this,
                    NewDetailActivity.class
            );
            detailIntent.putExtra(NewDetailActivity.EXTRA_MOVIE, movie);
            startActivity(detailIntent);
        } else {
            //activity is two paned
            Fragment fragment = DetailFragment.newInstance(movie);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.detail_fragment_container,fragment).commit();
        }
    }

    @Override
    public void onRequestReviews(ArrayList<String> reviews) {
        ReviewDialog dialog = ReviewDialog.newInstance(reviews);
        dialog.show(getSupportFragmentManager(),"ReviewDialog");
    }

    @Override
    public void onRequestTrailers(ArrayList<String> trailers) {
        VideoDialog dialog = VideoDialog.newInstance(trailers);
        dialog.show(getSupportFragmentManager(),"TrailerDialog");
    }
}
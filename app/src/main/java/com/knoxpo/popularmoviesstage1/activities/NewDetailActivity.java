package com.knoxpo.popularmoviesstage1.activities;

import android.support.v4.app.Fragment;

import com.knoxpo.popularmoviesstage1.dailogs.ReviewDialog;
import com.knoxpo.popularmoviesstage1.dailogs.VideoDialog;
import com.knoxpo.popularmoviesstage1.fragments.DetailFragment;
import com.knoxpo.popularmoviesstage1.model.Movie;

import java.util.ArrayList;

/**
 * Created by Tejas Sherdiwala on 4/17/2016.
 */
public class NewDetailActivity extends SingleFragmentActivity
        implements DetailFragment.Callback {
    public static final String EXTRA_MOVIE = "DetailActivity.EXTRA_MOVIE";

    @Override
    public Fragment getContentFragment() {
        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        return DetailFragment.newInstance(movie);
    }

    @Override
    public void onRequestReviews(ArrayList<String> reviews) {
        ReviewDialog dialog = ReviewDialog.newInstance(reviews);
        dialog.show(getSupportFragmentManager(), "ReviewDialog");
    }

    @Override
    public void onRequestTrailers(ArrayList<String> trailers) {
        VideoDialog dialog = VideoDialog.newInstance(trailers);
        dialog.show(getSupportFragmentManager(), "TrailerDialog");
    }
}

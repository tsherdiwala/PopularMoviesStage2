package com.knoxpo.popularmoviesstage1.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.knoxpo.popularmoviesstage1.R;
import com.knoxpo.popularmoviesstage1.data.MovieBaseHelper;
import com.knoxpo.popularmoviesstage1.data.MovieDbSchema;
import com.knoxpo.popularmoviesstage1.model.Movie;
import com.knoxpo.popularmoviesstage1.network.ApiFetchTask;
import com.knoxpo.popularmoviesstage1.utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tejas Sherdiwala on 4/17/2016.
 */
public class DetailFragment extends Fragment {

    public static final String ARGS_MOVIE = "DetailFragment.ARGS_MOVIE";

    private Movie mMovie;

    private TextView mReleaseDateTV, mVotesTV, mOverviewTV;
    private ImageView mMovieIV;
    private Button mReviewsBtn, mVideosBtn, mAddToFavouriteBtn;
    private Callback mCallback;
    private SQLiteDatabase mDatabase;


    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == mReviewsBtn){
                mCallback.onRequestReviews(mMovie.getReviews());
            }else if(v == mVideosBtn){
                mCallback.onRequestTrailers(mMovie.getVideos());
            }else if(v == mAddToFavouriteBtn){
                addToDatabase();
            }
        }
    };

    public interface Callback {
        void onRequestReviews(ArrayList<String> reviews);
        void onRequestTrailers(ArrayList<String> trailers);
    }

    public static DetailFragment newInstance(Movie movie) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_MOVIE, movie);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) getActivity();
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mMovie = args.getParcelable(ARGS_MOVIE);

        mDatabase = new MovieBaseHelper(getActivity()).getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        init(v);

        if(mMovie.getVideos().isEmpty()){
            GetVideos getVideos = new GetVideos();
            getVideos.execute();
        }

        if(mMovie.getReviews().isEmpty()){
            GetReviews getReviews = new GetReviews();
            getReviews.execute();
        }

        mReviewsBtn.setOnClickListener(mButtonClickListener);
        mVideosBtn.setOnClickListener(mButtonClickListener);
        mAddToFavouriteBtn.setOnClickListener(mButtonClickListener);

        updateUI();
        return v;
    }

    private void init(View v) {
        mReleaseDateTV = (TextView) v.findViewById(R.id.tv_release_date);
        mVotesTV = (TextView) v.findViewById(R.id.tv_votes);
        mOverviewTV = (TextView) v.findViewById(R.id.tv_overview);

        mReviewsBtn = (Button) v.findViewById(R.id.btn_reviews);
        mVideosBtn = (Button) v.findViewById(R.id.btn_videos);
        mAddToFavouriteBtn = (Button) v.findViewById(R.id.btn_add_to_favourites);

        mMovieIV = (ImageView) v.findViewById(R.id.iv_movie);
    }

    private void updateUI() {

        mReleaseDateTV.setText(
                getString(R.string.release_date, mMovie.getReleaseDate())
        );
        mOverviewTV.setText(mMovie.getOverview());
        mVotesTV.setText(
                getString(R.string.rating, mMovie.getVoteAverage())
        );

        Picasso
                .with(getActivity())
                .load(mMovie.getPosterPath())
                .into(mMovieIV);

        getActivity().setTitle(mMovie.getTitle());

        if(!mMovie.getReviews().isEmpty()){
            mReviewsBtn.setVisibility(View.VISIBLE);
        }

        if(!mMovie.getVideos().isEmpty()){
            mVideosBtn.setVisibility(View.VISIBLE);
        }


    }

    private void addToDatabase(){
        ContentValues values = new ContentValues();
        values.put(MovieDbSchema.Columns.ID,mMovie.getId());
        values.put(MovieDbSchema.Columns.POSTER_PATH,mMovie.getPosterPath());
        values.put(MovieDbSchema.Columns.TITLE,mMovie.getTitle());
        values.put(MovieDbSchema.Columns.RELEASE_DATE,mMovie.getReleaseDate());
        values.put(MovieDbSchema.Columns.OVERVIEW,mMovie.getOverview());
        values.put(MovieDbSchema.Columns.VOTE_AVERAGE,mMovie.getVoteAverage());
        mDatabase.insert(MovieDbSchema.Table.NAME,null,values);
    }

    private class GetReviews extends ApiFetchTask<ArrayList<String>> {

        private static final String
                JSON_A_RESULTS = "results",
                JSON_S_CONTENT = "content";

        public GetReviews() {
            super(String.format(Constants.MovieDb.URL_REVIEW, mMovie.getId()));
        }

        @Override
        protected ArrayList<String> parseResponse(String response) {
            ArrayList<String> fetchedReviews = null;
            try {
                JSONObject object = new JSONObject(response);
                JSONArray reviews = object.optJSONArray(JSON_A_RESULTS);

                if (reviews != null) {
                    fetchedReviews = new ArrayList<String>();
                    for (int i = 0; i < reviews.length(); i++) {
                        JSONObject review = reviews.optJSONObject(i);
                        if (review != null) {
                            String content = review.optString(JSON_S_CONTENT);
                            fetchedReviews.add(content);
                        }
                    }
                }

            } catch (JSONException e) {
                //return null;
            } finally {
                return fetchedReviews;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            if (strings != null) {
                mMovie.addReviews(strings);
            }
            updateUI();
        }
    }

    private class GetVideos extends ApiFetchTask<ArrayList<String>> {

        private static final String
                JSON_A_RESULTS = "results",
                JSON_S_KEY = "key";

        public GetVideos() {
            super(String.format(Constants.MovieDb.URL_VIDEOS, mMovie.getId()));
        }

        @Override
        protected ArrayList<String> parseResponse(String response) {
            ArrayList<String> fetchedVideos = null;

            try {
                JSONObject object = new JSONObject(response);
                JSONArray videos = object.optJSONArray(JSON_A_RESULTS);
                if (videos != null) {
                    fetchedVideos = new ArrayList<String>();
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject video = videos.optJSONObject(i);
                        if (video != null) {
                            String key = video.optString(JSON_S_KEY);
                            fetchedVideos.add(key);
                        }
                    }
                }
            } catch (JSONException e) {

            }

            return fetchedVideos;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            if (strings != null) {
                mMovie.addVideos(strings);
            }
            updateUI();
        }
    }
}
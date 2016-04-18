package com.knoxpo.popularmoviesstage1.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.knoxpo.popularmoviesstage1.R;
import com.knoxpo.popularmoviesstage1.adapters.MoviesAdapter;
import com.knoxpo.popularmoviesstage1.data.MovieBaseHelper;
import com.knoxpo.popularmoviesstage1.data.MovieCursorWrapper;
import com.knoxpo.popularmoviesstage1.data.MovieDbSchema;
import com.knoxpo.popularmoviesstage1.model.Movie;
import com.knoxpo.popularmoviesstage1.network.ApiFetchTask;
import com.knoxpo.popularmoviesstage1.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tejas Sherdiwala on 4/17/2016.
 */
public class MovieListFragment extends Fragment {


    private static final String
            SAVED_MOVIES = "MovieListFragment.SAVED_MOVIES",
            SP_SORT_BY = "MovieListFragment.SP_SORT_BY";

    private static final int
            SPAN_COUNT = 3,
            SORT_BY_MOST_POPULAR = 0,
            SORT_BY_HIGHEST_RATED = 1,
            DEFAULT_SORT_BY = SORT_BY_MOST_POPULAR;

    private RecyclerView mMoviesRV;
    private GetPopularMoviesTask mTask;
    private ArrayList<Movie> mMovies;
    private MoviesAdapter mAdapter;

    private SQLiteDatabase mDatabase;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int currentSortBy = preferences.getInt(SP_SORT_BY, DEFAULT_SORT_BY);
        int chosenSortBy = DEFAULT_SORT_BY;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.sort_popularity:
                chosenSortBy = SORT_BY_MOST_POPULAR;
                break;
            case R.id.sort_rating:
                chosenSortBy = SORT_BY_HIGHEST_RATED;
                break;
            case R.id.show_favourites:
                getMoviesFromDatabase();
                return true;
            default:
                return false;
        }

        if (chosenSortBy != currentSortBy) {
            preferences
                    .edit()
                    .putInt(SP_SORT_BY, chosenSortBy)
                    .apply();

            getMovies();
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDatabase = new MovieBaseHelper(getActivity()).getReadableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_list,container,false);
        init(v);

        mMoviesRV.setLayoutManager(
                new GridLayoutManager(getActivity(), SPAN_COUNT)
        );
        mMoviesRV.setAdapter(mAdapter);

        ArrayList<Movie> savedMovies = null;
        if (savedInstanceState != null) {
            savedMovies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
        }

        if (savedMovies == null) {
            getMovies();
        } else {
            mMovies.addAll(savedMovies);
            mAdapter.notifyDataSetChanged();
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_MOVIES, mMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (mTask != null) {
            //stop the task to unnecessary fetch the data
            mTask.cancel(true);
        }
        super.onDestroy();
    }

    private void init(View v){
        mMoviesRV = (RecyclerView)v.findViewById(R.id.rv_movies);
        mMovies = new ArrayList<>();
        mAdapter = new MoviesAdapter(getActivity(),mMovies);
    }

    private void getMovies() {
        if(mTask!=null){
            mTask.cancel(true);
        }
        mMovies.clear();
        mAdapter.notifyDataSetChanged();

        SharedPreferences preferences
                = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int sortBy = preferences.getInt(SP_SORT_BY, DEFAULT_SORT_BY);
        if (sortBy == SORT_BY_MOST_POPULAR) {
            mTask = new GetPopularMoviesTask(Constants.MovieDb.URL_POPULAR);
        } else if (sortBy == SORT_BY_HIGHEST_RATED) {
            mTask = new GetPopularMoviesTask(Constants.MovieDb.URL_TOP_RATED);
        }

        mTask.execute();
    }

    private void getMoviesFromDatabase(){
        MovieCursorWrapper cursorWrapper = queryMovies();
        mMovies.clear();

        while(cursorWrapper.moveToNext()){
            mMovies.add(cursorWrapper.getMovie());
        }
        mAdapter.notifyDataSetChanged();
    }

    private MovieCursorWrapper queryMovies(){
        Cursor cursor = mDatabase.query(
                MovieDbSchema.Table.NAME,
                null, // Columns - null selects all columns
                null, //whereClause,
                null, //whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new MovieCursorWrapper(cursor);
    }

    private class GetPopularMoviesTask extends ApiFetchTask<ArrayList<Movie>> {

        private static final String
                JSON_A_RESULTS = "results";

        public GetPopularMoviesTask(String url) {
            super(url);
        }

        @Override
        protected ArrayList<Movie> parseResponse(String response) {
            ArrayList<Movie> movies = null;
            if (response != null) {
                try {
                    movies = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    JSONArray movieArray = object.getJSONArray(JSON_A_RESULTS);
                    for (int i = 0; i < movieArray.length(); i++) {
                        movies.add(new Movie(movieArray.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if(movies!=null){
                mMovies.addAll(movies);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

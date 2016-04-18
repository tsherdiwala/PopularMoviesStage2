package com.knoxpo.popularmoviesstage1.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.knoxpo.popularmoviesstage1.model.Movie;

/**
 * Created by asus on 4/18/2016.
 */
public class MovieCursorWrapper extends CursorWrapper {
    public MovieCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Movie getMovie(){
        long id = getLong(getColumnIndex(MovieDbSchema.Columns.ID));
        String posterPath = getString(getColumnIndex(MovieDbSchema.Columns.POSTER_PATH));
        String title = getString(getColumnIndex(MovieDbSchema.Columns.TITLE));
        String releaseDate = getString(getColumnIndex(MovieDbSchema.Columns.RELEASE_DATE));
        String overview = getString(getColumnIndex(MovieDbSchema.Columns.OVERVIEW));
        double voteAverage = getDouble(getColumnIndex(MovieDbSchema.Columns.VOTE_AVERAGE));

        return new Movie(id,title,releaseDate, posterPath, overview, voteAverage);
    }
}

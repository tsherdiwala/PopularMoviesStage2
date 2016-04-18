package com.knoxpo.popularmoviesstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.knoxpo.popularmoviesstage1.data.MovieDbSchema.Table;

/**
 * Created by asus on 4/18/2016.
 */
public class MovieBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "CrimeBaseHelper";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "movies.db";

    public MovieBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table.NAME + "(" +
                MovieDbSchema.Columns.ID + " integer primary key, " +
                MovieDbSchema.Columns.TITLE + ", " +
                MovieDbSchema.Columns.RELEASE_DATE + ", " +
                MovieDbSchema.Columns.POSTER_PATH + ", " +
                MovieDbSchema.Columns.VOTE_AVERAGE + ", " +
                MovieDbSchema.Columns.OVERVIEW +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

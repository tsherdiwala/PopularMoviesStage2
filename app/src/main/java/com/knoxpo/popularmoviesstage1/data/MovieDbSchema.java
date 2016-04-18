package com.knoxpo.popularmoviesstage1.data;

/**
 * Created by asus on 4/18/2016.
 */
public class MovieDbSchema {

    public static final class Table {
        public static final String NAME = "movies";
    }

    public static final class Columns {
        public static final String
                POSTER_PATH = "poster_path",
                TITLE = "title",
                RELEASE_DATE = "release_date",
                OVERVIEW = "overview",
                VOTE_AVERAGE = "vote_average",
                ID = "_id";
    }
}

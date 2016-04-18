package com.knoxpo.popularmoviesstage1.utils;

/**
 * Created by Tejas Sherdiwala on 3/6/2016.
 */
public class Constants {

    public static class MovieDb {

        private static final String API_HOST = "https://api.themoviedb.org/3";

        public static final String
                //URL_POPULAR = API_HOST + "/discover/movie",
                URL_POPULAR = API_HOST + "/movie/popular",
                URL_TOP_RATED = API_HOST + "/movie/top_rated",
                URL_REVIEW = API_HOST + "/movie/%s/reviews",
                URL_VIDEOS = API_HOST + "/movie/%s/videos",
                API_KEY = ""; //your api key here
    }

}

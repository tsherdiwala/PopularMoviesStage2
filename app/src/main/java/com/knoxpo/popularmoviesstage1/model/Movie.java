package com.knoxpo.popularmoviesstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 3/6/2016.
 */
public class Movie implements Parcelable {

    private static final String
            POSTER_BASE_URL = "http://image.tmdb.org/t/p/",
            POSTER_SIZE = "w185",
            JSON_S_TITLE = "title",
            JSON_S_POSTER_PATH = "poster_path",
            JSON_D_VOTE_AVERAGE = "vote_average",
            JSON_S_RELEASE_DATE = "release_date",
            JSON_S_OVERVIEW = "overview",
            JSON_N_ID = "id";

    private String
            mPosterPath,
            mTitle,
            mReleaseDate,
            mOverview;

    private double mVoteAverage;

    private long mId;

    private ArrayList<String> mReviews;
    private ArrayList<String> mVideos;

    private Movie(){
        mReviews = new ArrayList<>();
        mVideos = new ArrayList<>();
    }

    public Movie(long id, String title, String releaseDate, String posterPath, String overview, double voteAverage) {
        this();
        mPosterPath = posterPath;
        mTitle = title;
        mReleaseDate = releaseDate;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mId = id;
    }

    public Movie(JSONObject object) {
        this();
        mId = object.optLong(JSON_N_ID);
        mTitle = object.optString(JSON_S_TITLE);
        String posterPath = object.optString(JSON_S_POSTER_PATH, null);
        if (posterPath != null) {
            StringBuilder builder = new StringBuilder(POSTER_BASE_URL);
            builder
                    .append(POSTER_SIZE)
                    .append(posterPath);
            mPosterPath = builder.toString();
        }

        mReleaseDate = object.optString(JSON_S_RELEASE_DATE);
        mOverview = object.optString(JSON_S_OVERVIEW);

        mVoteAverage = object.optDouble(JSON_D_VOTE_AVERAGE);
    }

    public long getId() {
        return mId;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public ArrayList<String> getReviews() {
        return mReviews;
    }

    public ArrayList<String> getVideos() {
        return mVideos;
    }

    public void addReview(String review) {
        mReviews.add(review);
    }

    public void addReviews(List<String> reviews) {
        for (int i = 0; i < reviews.size(); i++) {
            addReview(reviews.get(i));
        }
    }

    public void addVideo(String video) {
        mVideos.add(video);
    }

    public void addVideos(List<String> videos) {
        mVideos.addAll(videos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mOverview);
        dest.writeDouble(mVoteAverage);
        dest.writeStringList(mReviews);
        dest.writeStringList(mVideos);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        this();
        mPosterPath = in.readString();
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readDouble();
        in.readStringList(mReviews);
        in.readStringList(mVideos);
    }

}
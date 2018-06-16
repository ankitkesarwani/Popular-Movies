package com.example.ankitkesarwanimr.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private Double mVoteAverage;
    private String mReleaseDate;

    public Movie() {

    }

    public Movie(String originalTitle, String posterPath, String overview, Double voteAverage, String releaseDate) {
        this.mOriginalTitle = originalTitle;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mVoteAverage = voteAverage;
        this.mReleaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        if (in.readByte() == 0) {
            mVoteAverage = null;
        } else {
            mVoteAverage = in.readDouble();
        }
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        if (originalTitle.equals("null")) {
            this.mOriginalTitle = originalTitle;
        }
    }

    public String getPosterPath() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        if (!posterPath.equals("null")) {
            this.mPosterPath = posterPath;
        }
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {

        if (!overview.equals("null")) {
            this.mOverview = overview;
        }
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        if (!voteAverage.equals("null")) {
            this.mVoteAverage = voteAverage;
        }
    }

    public String getDetailedVoteAverage() {
        return String.valueOf(getVoteAverage()) + "/10";
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        if (!releaseDate.equals("null")) {
            this.mReleaseDate = releaseDate;
        }
    }

    public String getDateFormat() {
        return DATE_FORMAT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        if (mVoteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mVoteAverage);
        }
        dest.writeString(mReleaseDate);
    }
}

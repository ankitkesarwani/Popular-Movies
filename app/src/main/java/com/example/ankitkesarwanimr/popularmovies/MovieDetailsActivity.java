package com.example.ankitkesarwanimr.popularmovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;

/**
 * Created by Ankit Kesarwani
 */

public class MovieDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private ImageView mImageView;
    private TextView mMovieName;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mMovieDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mImageView = (ImageView) findViewById(R.id.movie_details_image);
        mMovieName = (TextView) findViewById(R.id.movie_details_name);
        mReleaseDate = (TextView) findViewById(R.id.movie_details_date);
        mVoteAverage = (TextView) findViewById(R.id.movie_details_vote);
        mMovieDescription = (TextView) findViewById(R.id.movie_details_description);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.parcel_movie));

        if (movie.getOriginalTitle() != null) {
            mMovieName.setText(movie.getOriginalTitle());
        } else {
            mMovieName.setText(getString(R.string.not_found));
        }

        Picasso.with(this)
                .load(movie.getPosterPath())
                .error(R.drawable.image_not_available)
                .placeholder(R.drawable.loading)
                .into(mImageView);

        String description = movie.getOverview();

        if (description == null) {
            mMovieDescription.setTypeface(null, Typeface.ITALIC);
            description = getResources().getString(R.string.no_summary_found);
        }

        mMovieDescription.setText(description);
        mVoteAverage.setText(movie.getDetailedVoteAverage());

        String releaseDate = movie.getReleaseDate();
        if(releaseDate != null) {
            try {
                releaseDate = DateTimeHelper.getLocalizedDate(this,
                        releaseDate, movie.getDateFormat());
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Error with parsing movie release date", e);
            }
        } else {
            mReleaseDate.setTypeface(null, Typeface.ITALIC);
            releaseDate = getResources().getString(R.string.no_release_date_found);
        }
        mReleaseDate.setText(releaseDate);

    }
}

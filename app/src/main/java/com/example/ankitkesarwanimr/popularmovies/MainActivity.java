package com.example.ankitkesarwanimr.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    private GridView mGridView;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.main_movie_grid);
        mGridView.setOnItemClickListener(moviePosterClickListener);

        if (savedInstanceState == null) {
            getMovieFromTheMovieDb(getSortMethod());
        } else {

            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.parcel_movie));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }

                mGridView.setAdapter(new ImageAdapter(this, movies));
            }
        }
    }

    private void getMovieFromTheMovieDb(String sortMethod) {

        if (isNetworkAvailable()) {
            String apiKey = getString(R.string.key);

            OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {

                @Override
                public void onFetchMovieTaskCompleted(Movie[] movies) {

                    mGridView.setAdapter(new ImageAdapter(getApplicationContext(), movies));

                }
            };

            FetchMovieAsyncTask movieTask = new FetchMovieAsyncTask(onTaskCompleted, apiKey);
            movieTask.execute(sortMethod);

        } else {

            Snackbar.make(mGridView, "Internet Connection Required", Snackbar.LENGTH_LONG).show();

        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String getSortMethod() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPreferences.getString("SORT_METHOD", "POPULARITY_DESC");
    }

    private final GridView.OnItemClickListener moviePosterClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Movie movie = (Movie) parent.getItemAtPosition(position);

            Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
            intent.putExtra(getResources().getString(R.string.parcel_movie), movie);

            startActivity(intent);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, mMenu);
        mMenu = menu;

        mMenu.add(Menu.NONE, R.string.pref_sort_pop_desc_key, Menu.NONE, null).setVisible(false).setIcon(R.drawable.ic_sort_two_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mMenu.add(Menu.NONE, R.string.pref_sort_vote_avg_desc_key, Menu.NONE, null).setVisible(false).setIcon(R.drawable.ic_sort_one_light).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        updateMenu();

        return true;
    }

    private void updateMenu() {

        String sortMethod = getSortMethod();

        if (sortMethod.equals(getString(R.string.tmdb_sort_pop_desc))) {
            mMenu.findItem(R.string.pref_sort_pop_desc_key).setVisible(false);
            mMenu.findItem(R.string.pref_sort_vote_avg_desc_key).setVisible(true);
        } else {
            mMenu.findItem(R.string.pref_sort_vote_avg_desc_key).setVisible(false);
            mMenu.findItem(R.string.pref_sort_pop_desc_key).setVisible(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        int numMovieObjects = mGridView.getCount();
        if (numMovieObjects > 0) {
            Movie[] movies = new Movie[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (Movie) mGridView.getItemAtPosition(i);
            }
            outState.putParcelableArray(getString(R.string.parcel_movie), movies);
        }

        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.string.pref_sort_pop_desc_key:
                updateSharedPrefs(getString(R.string.tmdb_sort_pop_desc));
                updateMenu();
                getMovieFromTheMovieDb(getSortMethod());
                return true;
            case R.string.pref_sort_vote_avg_desc_key:
                updateSharedPrefs(getString(R.string.tmdb_sort_vote_avg_desc));
                updateMenu();
                getMovieFromTheMovieDb(getSortMethod());
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateSharedPrefs(String sortMethod) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_sort_method_key), sortMethod);
        editor.apply();

    }
}
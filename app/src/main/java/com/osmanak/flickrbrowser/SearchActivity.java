package com.osmanak.flickrbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity{

    private static final String TAG = "SearchActivity";

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activateToolbar(true);
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: starts");
        getMenuInflater().inflate(R.menu.menu_search, menu);

        //System manager provides access to system's search services
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //Get reference to Search View widget that's embedded in the Search menu toolbar.
        // since we used an action view for the search View, we call getActionView
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        //Get Search Manager to retrieve the searchable info from the xml
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);

        //Sets whether the widget shows up as an icon or inflates immediately.
        mSearchView.setIconified(false);


        //Anonymous classes to set up our search listeners
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: called");

                //Create a shared preferences object to store the query of the search
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(FLICKR_QUERY, query).apply();

                mSearchView.clearFocus();
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });

        Log.d(TAG, "onCreateOptionsMenu: returned " + true);
        return true;
    }
}

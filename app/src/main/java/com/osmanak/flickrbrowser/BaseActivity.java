package com.osmanak.flickrbrowser;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by Osman Ak on 6/23/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY = "FLICKER_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    void activateToolbar(boolean enableHome){
        Log.d(TAG, "activateToolbar: starts");

        //Gets reference to the actionbar so we can add to it
        ActionBar actionBar = getSupportActionBar();

        if(actionBar == null){

            //Inflate the actionbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            if(toolbar != null){

                //Puts the toolbar in place at the top of the screen
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if(actionBar != null){

            //Enable or disable the home button
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}

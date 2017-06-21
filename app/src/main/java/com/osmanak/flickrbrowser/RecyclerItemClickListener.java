package com.osmanak.flickrbrowser;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Osman Ak on 6/20/2017.
 */

//Extends SimpleOnItemTouchListener so we can override the onInterceptTouchEven method
class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener{
    private static final String TAG = "RecyclerItemClickListen";

    //Interface to guarantee interface implementations carry itemClick methods
    interface OnRecyclerClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;

    //Constructor. Asks for context, a reference to the RecyclerView that taps are being  detected on, and the listener
    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener listener) {
        mListener = listener;
        mGestureDetector = null;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        return super.onInterceptTouchEvent(rv, e);
    }
}

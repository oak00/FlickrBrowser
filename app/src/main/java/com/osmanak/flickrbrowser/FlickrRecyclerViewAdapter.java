package com.osmanak.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Osman Ak on 6/20/2017.
 */


class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> mPhotosList;
    private Context mContext;

    //Constructor
    public FlickrRecyclerViewAdapter(List<Photo> photosList, Context context) {
        mPhotosList = photosList;
        mContext = context;
    }

    @Override //Called by the layout manager (recycler) when it needs a new View
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");

        //Uses the LayoutInflater class to store an inflated view of the browse.xml layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override // Called by the layout manager (recycler) when it wants new data in an existing row
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {

        //Retrieve current photo object from the list
        Photo photoItem = mPhotosList.get(position);
        Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " --> " + position);

        //Retrieves a singleton Picasso object
        Picasso.with(mContext)
                //Loads an image from a URL, and stores it in the image field of the Photo object
                .load(photoItem.getImage())
                 //Sets placeholder image to be used if there is an error
                .error(R.drawable.placeholder)
                //Sets placeholder image to be used while image is downloading
                .placeholder(R.drawable.placeholder)
                //Stores the downloaded image into the imageView widget in the ViewHolder
                .into(holder.thumbnail);

        //Puts the title into the textView
        holder.title.setText(photoItem.getTitle());

    }

    @Override
    public int getItemCount() {
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.size():0 );
    }

    //Provides the adapter with a new list when the query changes
    void loadNewData(List<Photo> newPhotos){
        mPhotosList = newPhotos;

        //Notifies the recycler view that the data has changed
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position){
        return ((mPhotosList != null) && (mPhotosList.size() != 0 ) ? mPhotosList.get(position) : null);
    }

    //ViewHolder has to be available to adapter, but also has to be available for the RecyclerView, therefor it is set to package protected
    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}

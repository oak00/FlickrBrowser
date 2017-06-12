package com.osmanak.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Osman Ak on 6/11/2017.
 */

class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> mPhotoList = null;
    private String mBaseURL;
    private String mLanguage;

    //Determines how many search terms to match results by
    private boolean mMatchAll;

    //Stores callback data
    private final OnDataAvailable mCallBack;

    //Flag to specify whether class is running async
    private boolean runningOnSameThread = false;

    //Interface created to guarantee calling class contains a callback receiving method
    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    //Constructor
    public GetFlickrJsonData(OnDataAvailable callBack, String baseURL, String language, boolean matchAll) {
        Log.d(TAG, "GetFlickrJsonData: called");
        mCallBack = callBack;
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;

    }

    //Runs the asynchronous GetRawData class execution
    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;

        //Creates URI for GetRawData and executes asyc task
        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");

        if(mCallBack != null){
            mCallBack.onDataAvailable(mPhotoList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0], mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mPhotoList;

    }

    private String createUri(String searchCriteria, String lang, boolean matchAll){
        Log.d(TAG, "createUri: starts");

        //Creates a Uri which parses the given encoded URI string.
        Uri uri = Uri.parse(mBaseURL);

        //Returns helper class for building or manipulating URI references
        Uri.Builder builder = uri.buildUpon();

        //Appends parameters to be added into final URI
        builder = builder.appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1");

        //Build final URI
        uri = builder.build();

        return uri.toString();
    }

    @Override //Parses JSON data and returns Photo objects
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts. Status = "+ status);

        if(status == DownloadStatus.OK){

            //Creates a list for photos retrieved on execution
            mPhotoList = new ArrayList<>();

            try{
                //Retrieves JSON data from method parameter
                JSONObject jsonData = new JSONObject(data);

                //Creates array for all items in JSON data
                JSONArray itemsArray = jsonData.getJSONArray("items");

                //Loops through each object in array and parses its properties
                for(int i = 1; i<itemsArray.length(); i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    //Photo URL is nested in another JSON object, so we must pull that out
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    //Gets full size image link
                    String link = photoUrl.replaceFirst("_m.","_b.");

                    //Create the photo object from parsed JSON data and add it to the photo list
                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);
                    mPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());
                }
            } catch(JSONException jsone){
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error process Json data "+ jsone.getMessage());

                //Set status to failed
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        //NOTE: This code will only function properly if run non-async. See onPostExecute() for async class calls.
        if(runningOnSameThread && mCallBack != null){
            // Now inform the caller that processing is done - possibly returning null if there was an error
            mCallBack.onDataAvailable(mPhotoList, status);
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}

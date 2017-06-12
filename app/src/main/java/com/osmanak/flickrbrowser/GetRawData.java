package com.osmanak.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// Creating enum for custom download statuses
enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

/**
 * Created by Osman Ak on 6/8/2017.
 */

class GetRawData extends AsyncTask<String, Void, String> {

    //Create TAG for logging
    private static final String TAG = "GetRawData";

    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallback; //Polymorphous variable to store context

    //Interface created to guarantee calling class contains a callback receiving method
    interface OnDownloadComplete{
        void onDownloadComplete(String data,DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callback /*Parameter is a polymorphous class context */ ) {

        //Setting download status to IDLE at start
        this.mDownloadStatus = DownloadStatus.IDLE;
        mCallback = callback;
    }

    //Allows GetRawData class to be used as a non-Async task
    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: starts");

        if(mCallback != null){
            mCallback.onDownloadComplete(doInBackground(s), mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    @Override // Method passes along results of execution to calling class.
    protected void onPostExecute(String s) {
//        Log.d(TAG, "onPostExecute: parameter = " + s);

        //If a class calls GetRawData, then call it's callback method and send it the results of execution
        if(mCallback != null){
            mCallback.onDownloadComplete(s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");

    }

    @Override //doInBackground is an Async Class's execution method
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        // Checks to see if we've been given a URL in method parameter.
        if(strings == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try{
            // Starts processing URL passed in method parameter
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            // Opens HTTP connection from URL passed from method parameter
            connection = (HttpURLConnection) url.openConnection();

            //Specify what type of request our method will be making to the server
            connection.setRequestMethod("GET");
            connection.connect();

            //Return the response code from the server
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was "+ response);

            // Create buffered reader to read data from the input stream from the URL and buffers them to make it readable.
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //While the current line in the buffered reader is not empty, append the line and a new line into the String Builder.
            String line;
            while(null != (line = reader.readLine())){
                result.append(line).append("\n");
            }

            //Set download status to OK and return the built string.
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch(IOException e){
            Log.e(TAG, "doInBackground: IO Exception reading data " + e.getMessage());
        } catch(SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception. Needs permission " + e.getMessage());
        } finally {

            //End the connection
            if(connection != null){
                connection.disconnect();
            }

            //Close the reader
            if(reader != null){
                try{
                    reader.close();
                } catch(IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream " +e.getMessage());
                }
            }
        }

        //If the method has not returned in the TRY block, and no exceptions were thrown, then we will return null.
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

}

package com.knoxpo.popularmoviesstage1.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.knoxpo.popularmoviesstage1.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 3/6/2016.
 */
public class ApiFetchTask<T> extends AsyncTask<Void, Void, T> {

    private static final String
            TAG = ApiFetchTask.class.getSimpleName(),
            PARAM_API_KEY = "api_key";

    private String mUrl;
    private HashMap<String, String> mQueryParams;

    public ApiFetchTask(String url) {
        this(url, new HashMap<String, String>());
    }

    public ApiFetchTask(String url, HashMap<String, String> queryParams) {
        mUrl = url;
        mQueryParams = queryParams;
        mQueryParams.put(PARAM_API_KEY, Constants.MovieDb.API_KEY);
    }

    public final void addParam(String key,String value){
        mQueryParams.put(key,value);
    }


    @Override
    protected T doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        URL url = null;
        String responseString = null;
        Uri.Builder uriBuilder = Uri.parse(mUrl).buildUpon();

        for (Map.Entry<String, String> param : mQueryParams.entrySet()) {
            try {
                uriBuilder
                        .appendQueryParameter(
                                param.getKey(),
                                URLEncoder.encode(param.getValue(), "UTF-8")
                        );
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Coud not encode param: " + param.getKey() + " with value : " + param.getValue());
            }
        }


        Uri builtUri = uriBuilder.build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                responseString = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                //forecastJsonStr = null;
                responseString = null;
            }
            responseString = buffer.toString();
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return parseResponse(responseString);
    }

    protected T parseResponse(String response){
        if(response == null){
            return null;
        }else{
            return (T)response;
        }
    }

}

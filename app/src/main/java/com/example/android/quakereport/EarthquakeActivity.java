package com.example.android.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    private static final String LOG_TAG = EarthquakeActivity.class.getName();
    //Adapter for the list of earthquakes
    private EarthquakeAdapter mAdapter;

   // URL for earthquake data from the USGS dataset
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);



        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes the list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        //Start the AsyncTask to fetch the earthquake data
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //Find the current earthquake that was clicked on
                Earthquake currentEarthquake =mAdapter.getItem(position);

                //Convert the String URL into URI object (to pass into the intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                //Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);

                //Send the intent to start new activity
                startActivity(websiteIntent);

            }
        });
    }
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            //Do not perform the request if there are no URLs, or the first URl is null.
            if(urls.length < 1 || urls[0] == null){
                return null;
            }

            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Earthquake> data) {

           // super.onPostExecute(earthquakeList);
            //Clear the adapter of previous earthquake data
            mAdapter.clear();
            //If there is a valid list of {@link Earthquakes} then add them to the adapter's
            //data set. This will trigger the ListView to update.
            if(data!=null && !data.isEmpty()){
                mAdapter.addAll(data);
            }
        }
    }
}
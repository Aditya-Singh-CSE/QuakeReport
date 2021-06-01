package com.example.android.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeActivity.class.getName();
    //Adapter for the list of earthquakes
    private EarthquakeAdapter mAdapter;

    private TextView mEmptyStateTextView;

   // URL for earthquake data from the USGS dataset
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you are using multiple loaders.
     */
    private static final int EARthquake_LOader_ID = 1;

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

        mEmptyStateTextView = (TextView)findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

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
        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo !=null && networkInfo.isConnected())
        {
            //Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            //Initialise the loader. Pass in the int ID constant defined above and pass in null for
            //the bundle. Pass in this activity for the LoaderCallback parameter (which is valid
            //because this activity implements the LoaderCallback interface).
            loaderManager.initLoader(EARthquake_LOader_ID, null,this);
        }
        else
        {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connetion error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


    }
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i ,Bundle bundle)
    {
        //Create a new loader for the given URL
        return new EarthquakeLoader(this,USGS_REQUEST_URL);
    }
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader,List<Earthquake> earthquakes)
    {
        View loadingIndicator = findViewById((R.id.loading_indicator));
        loadingIndicator.setVisibility(View.GONE);
        //Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);
        //Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}, then add them to the adapter's
        //data set. This will trigger the ListView to update.
        if(earthquakes !=null && !earthquakes.isEmpty())
        {
            mAdapter.addAll(earthquakes);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader)
    {
        //Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
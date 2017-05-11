
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>{


    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    //private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10";
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    Earthquake_adapter mAdapter;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(LOG_TAG,"____On Create____");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        updateUi();

        ConnectivityManager connmngr= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connmngr.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){


            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        }
        else{

            ProgressBar prog=(ProgressBar) findViewById(R.id.loading_spinner);
            prog.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }





    }

//    private class QuakeAsyntask extends AsyncTask<String, Void, String>{
//
//        @Override
//        protected String doInBackground(String... urls) {
//
//            if(urls.length<0 || urls[0]==null){
//                return null;
//            }
//
//
//            return extract_json(urls[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String json_res) {
//
//            updateUi(json_res);
//
//        }
//    }

    private void updateUi(){

       ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

         mAdapter = new Earthquake_adapter(this,earthquakes);
e
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake current_eq=mAdapter.getItem(position);
                Uri uri=Uri.parse(current_eq.getUrl());

                Intent intent=new Intent(Intent.ACTION_VIEW,uri);

                startActivity(intent);

            }
        });

    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "100");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());

    }
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        Log.i(LOG_TAG,"____On LoderFinished____");
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        ProgressBar prog=(ProgressBar) findViewById(R.id.loading_spinner);
        prog.setVisibility(View.GONE);



        mAdapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

        Log.i(LOG_TAG,"______On LoderReset______");


        mAdapter.clear();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

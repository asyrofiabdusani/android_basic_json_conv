package com.example.asyrofiabdusani.earthquakeapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**eartquakeactivity.java
 *
 */
public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>>{
    private static final String LOG_TAG = MainActivity.class.getName();
    private TextView mEmptyTextView;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private EarthquakeAdapter mAdapter;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.rootlist);
        mEmptyTextView =(TextView)findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyTextView);

        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = (Earthquake) mAdapter.getItem(position);
                Uri webUri = Uri.parse(currentEarthquake.getmUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                startActivity(webIntent);
            }
        });

        earthquakeAsynkTask task = new earthquakeAsynkTask();
        task.execute(USGS_REQUEST_URL);
        LoaderManager loaderManager=getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);

        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=connManager.getActiveNetworkInfo();

        if (netInfo!=null&&netInfo.isConnected()){
            LoaderManager loaderManager1=getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);

        }else {
            View loading=findViewById(R.id.loading_indicator);
            loading.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_inet);
        }
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {

        return new EarthquakeLoader(this,USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_earthquake);
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
    }

    private class earthquakeAsynkTask extends AsyncTask<String,Void,List<Earthquake>>{
        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            if (urls.length<1||urls[0]==null){
                return null;
            }
            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;

        }
        @Override
        protected void onPostExecute(List<Earthquake> data) {
            // Bersihkan adapter dari data gempa sebelumnya
            mAdapter.clear();

            // Jika ada daftar {@link Earthquake} yang valid, tambahkan ke data set adapter.
            // Ini akan memicu pembaruan ListView..
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }

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
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

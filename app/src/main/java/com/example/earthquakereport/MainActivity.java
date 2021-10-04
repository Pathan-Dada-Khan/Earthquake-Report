package com.example.earthquakereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Quake>> {

    private QuakeAdapter adapter;
    Dialog dialog;
    NetworkInfo networkInfo;
    View noResult;

    private static final int EARTHQUAKE_ID = 1;
    private static final String USGS_REQUEST_URL=
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new QuakeAdapter(this,new ArrayList<Quake>());
        ListView lv=(ListView)findViewById(R.id.list);
        lv.setAdapter(adapter);

        noResult = (View)findViewById(R.id.noResult);
        noResult.setVisibility(View.GONE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quake current = adapter.getItem(position);
                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(current.getUrl()));
                startActivity(i);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.indicator);
            loadingIndicator.setVisibility(View.GONE);
            noInternet();
        }
    }

    @Override
    public Loader<List<Quake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default)
        );

        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","200");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);

        return new QuakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Quake>> loader, List<Quake> data) {
        View loaderIndicator = findViewById(R.id.indicator);
        loaderIndicator.setVisibility(View.GONE);
        adapter.clear();
        if(data!=null && !data.isEmpty()){
            adapter.addAll(data);
        }
        else{
            noResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Quake>> loader) {
        adapter.clear();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void noInternet(){
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.no_internet);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.no_internet));
        }
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_setting){
            Intent settingIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
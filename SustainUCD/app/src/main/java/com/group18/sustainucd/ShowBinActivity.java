package com.group18.sustainucd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.group18.sustainucd.Database.Bin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class ShowBinActivity extends AppCompatActivity {

    public static final String PICTURE_PATH = "Path";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static Bitmap bitmapToShow;

    private final String TAG = "ShowBinActivity";
    private final String mapsLabel = "Bin";
    private Bin binToShow;

    private AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        appBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click");
            }
        });
        InitBin();
        ((ImageView) findViewById(R.id.headerImageView)).setImageBitmap(bitmapToShow);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:"+binToShow.latitude+","+binToShow.longitude
                        +"?z=18&q="+binToShow.latitude+","+binToShow.longitude+"("+mapsLabel+")");

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void InitBin() {
        binToShow = new Bin();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            binToShow.pictureFileName = (String) extras.get(PICTURE_PATH);
            binToShow.latitude = (Double) extras.get(LATITUDE);
            binToShow.longitude = (Double) extras.get(LONGITUDE);
            binToShow.bitmap = bitmapToShow;
            Log.d(TAG, "latitude: " + binToShow.latitude + " longitude: " + binToShow.longitude);
        }
    }

    private Intent GetMapIntent() {
        Uri gmmIntentUri = Uri.parse("geo:"+binToShow.latitude+","+binToShow.longitude
                +"?z=18&q="+binToShow.latitude+","+binToShow.longitude+"("+mapsLabel+")");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        return mapIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_single_main, menu);
        return true;
    }

    //Event triggered on click on the top-right info icon (it's a menu item)
    //This will call the info activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO call info activity
        return super.onOptionsItemSelected(item);
    }
}

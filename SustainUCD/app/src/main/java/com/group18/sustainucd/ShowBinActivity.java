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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        InitBin();
        ((ImageView) findViewById(R.id.headerImageView)).setImageBitmap(bitmapToShow);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = Utils.GetMapIntent(binToShow, mapsLabel);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_show_bin, menu);
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

package com.group18.sustainucd.showBin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.R;
import com.group18.sustainucd.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Activity that shows all the information about a bin. Who starts this activity will pass all this
 * information. This class implements a toolbar, an option menu and a floating button.
 * An imageview that shows the bin picture is implemented inside the toolbar. Because a bitmap object
 * can be very big, Android doesn't permit to share this object via Extras. The bitmap is set
 * statically by the activity that starts this activity.
 */
public class ShowBinActivity extends AppCompatActivity {

    public static final String PICTURE_PATH = "Path";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static Bitmap bitmapToShow;

    private final String TAG = "ShowBinActivity";
    private final String mapsLabel = "Bin";
    private Bin binToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        InitBin();
        ((ImageView) findViewById(R.id.headerImageView)).setImageBitmap(bitmapToShow);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.done_fab);
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
        switch (item.getItemId()) {
            //If the button pressed is the back arrow
            //then will happen the same behavior of onBackPressed()
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

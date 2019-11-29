package com.group18.sustainucd.addBin;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.group18.sustainucd.R;
import com.group18.sustainucd.Utils;

/**
 * This activity shows the picture taken by the user and implements a clickable imageviews to let
 * the user select what kind of things can go inside this bin. This class implements the toolbar
 * and the option menu logic. The rest is implemented in a fragment.
 */
public class AddBinActivity extends AppCompatActivity {

    public static final String PICTURE_PATH = "PicturePath";
    private static String TAG = "AddBinActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_bin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Add the menu with the location button functionality
        //if the user has an app that can show the new bin location
        if (Utils.HasLocationApp(getPackageManager())) {
            getMenuInflater().inflate(R.menu.menu_add_bin, menu);
            return true;
        }
        return false;
    }

}

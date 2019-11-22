package com.group18.sustainucd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class SingleMainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int BIN_ADDED_SUCCESSFULLY = 2;
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static final String TAG = "SingleMainActivity";

    private String newPicturePath;
    //When you take a photo this file will store the photo. If the user add the bin successfully,
    //then this file reference will be null and will be re-instantiated when the user wants to add
    //another bin. If the user take a photo but the bin is not added for some reason this file
    //reference won't change. The file is deleted if it isn't null when the activity is destroyed.
    private static File nextBinPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Ask for access fine location permission, if not already granted
        if (!Permissions.HasAccessFineLocationPermission(getApplicationContext()))
            Permissions.AskAccessFineLocationPermission(this, 0);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ask for external storage permission, if not already granted
                if (!Permissions.HasExternalStoragePermission(getApplicationContext()))
                    Permissions.AskExternalStoragePermission(SingleMainActivity.this, REQUEST_EXTERNAL_STORAGE);
                else    //Take the photo with the phone camera app
                    TakePhoto();
            }
        });
    }

    private void TakePhoto() {
        //Intent for phone camera app
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check again if the permission has been granted
        //and if the intent will resolve to an activity
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            try {
                if (nextBinPicture == null)
                    nextBinPicture = BinImageHelper.CreateBinImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (nextBinPicture != null) {
                Uri imageURI = FileProvider.getUriForFile(this,
                        "com.group18.sustainucd.fileprovider", nextBinPicture);

                newPicturePath = nextBinPicture.getAbsolutePath();
                Log.d(TAG, "Picture path: "+ newPicturePath);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    //Start Add Bin Activity
                    Intent addBinIntent = new Intent(this, AddBinActivity.class);
                    addBinIntent.putExtra(AddBinActivity.PICTURE_PATH, newPicturePath);
                    startActivityForResult(addBinIntent, BIN_ADDED_SUCCESSFULLY);
                }
                break;
            case BIN_ADDED_SUCCESSFULLY:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(findViewById(R.id.fab), "Bin added successfully!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    nextBinPicture = null;
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            TakePhoto();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //The activity will be destroyed and there's a temp file that should be destroyed
        if (isFinishing() && nextBinPicture != null) {
            Log.d(TAG, "Activity will be destroyed, deleting the temp file");
            nextBinPicture.delete();
        }
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

package com.group18.sustainucd.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.group18.sustainucd.MainInfoActivity;
import com.group18.sustainucd.addBin.AddBinActivity;
import com.group18.sustainucd.BinImageHelper;
import com.group18.sustainucd.Permissions;
import com.group18.sustainucd.R;
import com.group18.sustainucd.userBins.UserBinsActivity;

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

/**
 * Home screen activity. This activity shows the bins that are near the user. It's the launcher
 * activity. It asks to the user to access the fine location and the external storage.
 * It implements the floating action button that will call the user camera app when the user clicks
 * it. It implements a static file used to store the next picture. If the user takes a photo the file
 * is not deleted but will be overwritten the next time the user will take a new photo. When the user
 * takes a photo and adds the bin, this file will become null and next time the user will take a
 * photo the file will be recreated with a new path (using BinImageHelper class).
 * If the user takes a photo but doesn't add the bin and then the app is destroyed by the OS or by
 * the user then the file is deleted.
 */
public class MainActivity extends AppCompatActivity {

    public static final int BIN_ADDED_SUCCESSFULLY = 2;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 0;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static final String TAG = "MainActivity";

    private String newPicturePath;
    //When you take a photo this file will store the photo. If the user add the bin successfully,
    //then this file reference will be null and will be re-instantiated when the user wants to add
    //another bin. If the user take a photo but the bin is not added for some reason this file
    //reference won't change. The file is deleted if it isn't null when the activity is destroyed.
    private static File nextBinPictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.done_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ask for fine location permission
                if (!Permissions.HasAccessFineLocationPermission(getApplicationContext()))
                    Permissions.AskAccessFineLocationPermission(MainActivity.this, REQUEST_ACCESS_FINE_LOCATION);
                else if (!Permissions.HasExternalStoragePermission(getApplicationContext()))
                    //Ask for external storage permission, if not already granted
                    Permissions.AskExternalStoragePermission(MainActivity.this, REQUEST_EXTERNAL_STORAGE);
                else    //Take the photo with the phone camera app
                    TakePhoto();
            }
        });
    }

    /** Create the temp file if it's not already created and call the camera app.
     *  The picture will be stored in the file
     * */
    private void TakePhoto() {
        //Intent for phone camera app
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check again if the permission has been granted
        //and if the intent will resolve to an activity
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            try {
                if (nextBinPictureFile == null)
                    nextBinPictureFile = BinImageHelper.CreateBinImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (nextBinPictureFile != null) {
                Uri imageURI = FileProvider.getUriForFile(this,
                        "com.group18.sustainucd.fileprovider", nextBinPictureFile);

                newPicturePath = nextBinPictureFile.getAbsolutePath();
                Log.d(TAG, "Picture path: "+ newPicturePath);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        //source: https://developer.android.com/training/camera/photobasics
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
                    Snackbar.make(findViewById(R.id.done_fab), "Bin added successfully!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    nextBinPictureFile = null;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            TakePhoto();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy isFinishing: "+isFinishing());
        //The activity or the entire app will be destroyed and there's a temp file that should be destroyed
        if (isFinishing() && nextBinPictureFile != null) {
            Log.d(TAG, "Activity will be destroyed, deleting the temp file");
            nextBinPictureFile.delete();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Event triggered on click on one of the top-right menu icons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:  //Face icon. Should go to UserBinsActivity
                Intent showBinIntent = new Intent(this, UserBinsActivity.class);
                startActivity(showBinIntent);
                break;
            case R.id.action_info:  //Info icon. Should go to MainInfoActivity
                Intent infosIntent = new Intent(this, MainInfoActivity.class);
                startActivity(infosIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

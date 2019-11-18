package com.group18.sustainucd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.group18.sustainucd.Database.BinsManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class SingleMainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int BIN_ADDED_SUCCESSFULLY = 2;

    private String new_picture_path;

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
                    Permissions.AskExternalStoragePermission(SingleMainActivity.this, 1);
                //Take the photo with the phone camera app
                TakePhoto();
            }
        });
    }

    private void TakePhoto() {
        //Intent for phone camera app
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check again if the permission has been granted
        //and if the intent will resolve to an activity
        if (Permissions.HasExternalStoragePermission(this) &&
                takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            File binImageFile = null;
            try {
                binImageFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (binImageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(this, "com.group18.sustainucd.fileprovider", binImageFile);

                new_picture_path = binImageFile.getAbsolutePath();
                Log.d("SingleMainActivity", new_picture_path);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "BIN_";
        //Get the external storage directory. Because it's external I can copy the pictures later
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        //new File(storageDir+"/"+imageFileName+".jpg");// File.createTempFile(imageFileName,".jpg", storageDir);

        return imageFile;
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
                    addBinIntent.putExtra(AddBinActivity.PICTURE_PATH, new_picture_path);
                    startActivityForResult(addBinIntent, BIN_ADDED_SUCCESSFULLY);
                }
                break;
            case BIN_ADDED_SUCCESSFULLY:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(findViewById(R.id.fab), "Bin added successfully!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
        }
    }
}

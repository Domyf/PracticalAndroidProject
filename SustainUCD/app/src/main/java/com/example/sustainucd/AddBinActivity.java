package com.example.sustainucd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class AddBinActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    private ImageView binImageView;
    private TextView locationTextView;
    private int binCounter = 1;
    private String currentPhotoPath;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_bin);
        //UI Initialization
        binImageView = findViewById(R.id.binImageView);
        locationTextView = findViewById(R.id.locationTextView);
        //Location initialization
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    public void TakePhoto(View view)
    {
        //Ask for external storage permission
        Permissions.AskExternalStoragePermission(this, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        //Ask for access fine location permission
        Permissions.AskAccessFineLocationPermission(this, 1);

        //Intent for camera app
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check again if the permission has been granted
        //and if the intent will resolve to an activity
        if (Permissions.HasExternalStoragePermission(this) &&
                takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (imageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(this, "com.example.sustainucd.fileprovider", imageFile);

                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void GetAndSetLocation()
    {
        client.getLastLocation().addOnSuccessListener(AddBinActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    locationTextView.setText(latitude+","+longitude);
                }
            }

        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "BIN_" + binCounter;
        //Get the public storage directory. Because it's public I can copy the pictures later
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir+"/"+imageFileName+".jpg");// File.createTempFile(imageFileName,".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    private void setImageView() {
        // Get the dimensions of the View
        int targetW = binImageView.getWidth();
        int targetH = binImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        binImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            GetAndSetLocation();
            setImageView();
        }
    }
}

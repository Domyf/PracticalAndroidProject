package com.group18.sustainucd.ui.addBin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.Database.BinDao;
import com.group18.sustainucd.Database.BinsDatabase;
import com.group18.sustainucd.Permissions;
import com.group18.sustainucd.R;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AddBinFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    //View Model
    private AddBinViewModel addBinViewModel;
    //UI
    private TextView locationTextView;
    private ImageView binImageView;
    private Button takePhotoBtn;
    //Location client
    private FusedLocationProviderClient client;

    private int binCounter = 1;
    private Bin newBin;
    private String currentPhotoPath;

    /*  The onCreateView method is called when the Fragment should
        create its view, via XML layout inflation in this case.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //View Model initialization
        addBinViewModel = ViewModelProviders.of(this).get(AddBinViewModel.class);
        //Root view initialization
        View root = inflater.inflate(R.layout.fragment_add_bin, container, false);

        //Location initialization
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        //Model observation
        Observe();

        return root;
    }

    /*  This event is triggered after a successfull onCreateView() event.
        The view setup should be done here.
    */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI initialization
        locationTextView = view.findViewById(R.id.locationTextView);
        binImageView = view.findViewById(R.id.binImageView);
        takePhotoBtn = view.findViewById(R.id.takePhotoBtn);
        SetOnClickListeners();
    }


    /*  This method will setup all on click listeners that are needed.
        The button for taking a photo and the button for the addition
        of the bin into the database.
     */
    private void SetOnClickListeners()
    {
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask for external storage permission, if needed
                if (!Permissions.HasExternalStoragePermission(getActivity()))
                    Permissions.AskExternalStoragePermission(getActivity(), PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                //Ask for access fine location permission, if needed
                if (!Permissions.HasAccessFineLocationPermission(getActivity()))
                    Permissions.AskAccessFineLocationPermission(getActivity(), 1);
                //Take the photo with the phone camera app
                TakePhoto();
            }
        });
    }

    /*  View Model observation. After this method any change on the model
        will reflect on the view.
     */
    private void Observe()
    {
        addBinViewModel.getLocationText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                locationTextView.setText(s);
            }
        });
        addBinViewModel.getImageBitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binImageView.setImageBitmap(bitmap);
            }
        });
    }

    public void TakePhoto()
    {
        //Intent for phone camera app
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check again if the permission has been granted
        //and if the intent will resolve to an activity
        if (Permissions.HasExternalStoragePermission(getActivity()) &&
                takePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (imageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(getActivity(), "com.group18.sustainucd.fileprovider", imageFile);

                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void GetAndSetLocation()
    {
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    addBinViewModel.setLocationText(latitude+","+longitude);
                }
            }

        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "BIN_" + binCounter;
        //Get the external storage directory. Because it's external I can copy the pictures later
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir+"/"+imageFileName+".jpg");// File.createTempFile(imageFileName,".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    private void setBinImageView() {
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
        addBinViewModel.setImageBitmap(bitmap);
    }

    private void AddBin() {
        //Get instance of the bins database and the DAO
        //Because the instance is single for the entire app, this is not
        //an expensive method call but just reference return.
        BinsDatabase database = BinsDatabase.getInstance(getActivity());
        BinDao binDao = database.binDao();

        binDao.insertBin(newBin);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            GetAndSetLocation();
            setBinImageView();
        }
    }
}
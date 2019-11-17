package com.group18.sustainucd.ui.addBin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.group18.sustainucd.AddBinActivity;
import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.Database.BinDao;
import com.group18.sustainucd.Database.BinsDatabase;
import com.group18.sustainucd.Permissions;
import com.group18.sustainucd.R;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AddBinFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    //View Model
    private AddBinViewModel addBinViewModel;
    //UI
    private TextView locationTextView;
    private ImageView binImageView;
    private Button takePhotoBtn;
    private Button addBinBtn;
    //Location client
    private FusedLocationProviderClient client;

    private Bin newBin;
    private File binImageFile;
    private boolean locationAcquired;
    private boolean pictureTaken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationAcquired = false;
        pictureTaken = false;
        newBin = new Bin();
        //Location initialization
        client = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    /**  The onCreateView method is called when the Fragment should
        create its view, via XML layout inflation in this case.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //View Model initialization
        addBinViewModel = ViewModelProviders.of(this).get(AddBinViewModel.class);
        //Root view initialization
        View root = inflater.inflate(R.layout.fragment_add_bin, container, false);

        //Model observation
        Observe();

        return root;
    }

    /**  This event is triggered after a successfull onCreateView() event.
        The view setup is done here.
    */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI initialization
        locationTextView = view.findViewById(R.id.locationTextView);
        binImageView = view.findViewById(R.id.binImageView);
        takePhotoBtn = view.findViewById(R.id.takePhotoBtn);
        addBinBtn = view.findViewById(R.id.addBinBtn);
        SetOnClickListeners();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO modify this after choosing between tabbed, single or bottombar main activity
        if (getActivity().getIntent().hasExtra(AddBinActivity.PICTURE_PATH)) {
            String picturePath = getActivity().getIntent().getStringExtra(AddBinActivity.PICTURE_PATH);
            Log.d("AddBinFragment", picturePath);
            binImageFile = new File(picturePath);
            addBinBtn.setVisibility(View.VISIBLE);
            takePhotoBtn.setVisibility(View.GONE);
            GetAndSetLocation();
            binImageView.post(new Runnable() {
                @Override
                public void run() {
                    setBinImageView();
                }
            });
            pictureTaken = true;
        }
    }

    /**  This method will setup all on click listeners that are needed.
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
        addBinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictureTaken && locationAcquired)
                    AddBin();
            }
        });
    }

    /**  View Model observation. After this method any change on the model
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
            try {
                if (binImageFile == null)
                    binImageFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (binImageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(getActivity(), "com.group18.sustainucd.fileprovider", binImageFile);

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
                    newBin.latitude = location.getLatitude();
                    newBin.longitude = location.getLongitude();
                    addBinViewModel.setLocationText("Location acquired successfully!");
                    locationAcquired = true;
                }
            }

        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "BIN_";
        //Get the external storage directory. Because it's external I can copy the pictures later
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        //new File(storageDir+"/"+imageFileName+".jpg");// File.createTempFile(imageFileName,".jpg", storageDir);

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

        Bitmap bitmap = BitmapFactory.decodeFile(binImageFile.getAbsolutePath(), bmOptions);

        newBin.pictureFileName = binImageFile.getName();
        addBinViewModel.setImageBitmap(bitmap);
    }

    private void AddBin() {
        Log.d("AddBinFragment", "latitude: "+newBin.latitude
                +" longitude: "+newBin.longitude
                +" picture file name: "+newBin.pictureFileName);
        InsertTask(getContext(), newBin);
        //TODO modify this after choosing between tabbed, single or bottombar main activity
        if (getActivity().getIntent().hasExtra(AddBinActivity.PICTURE_PATH)) {
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
        }
    }

    private static void InsertTask(final Context context, final Bin newBin) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                BinDao binDao = BinsDatabase.getInstance(context).binDao();
                binDao.insertBin(newBin);
                Log.d("AddBinFragment", "Bin added");
                return null;
            }
        }.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            addBinBtn.setVisibility(View.VISIBLE);
            GetAndSetLocation();
            setBinImageView();
            pictureTaken = true;
        }
    }

}
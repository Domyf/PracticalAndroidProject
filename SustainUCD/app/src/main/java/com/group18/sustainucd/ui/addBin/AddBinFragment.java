package com.group18.sustainucd.ui.addBin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.group18.sustainucd.AddBinActivity;
import com.group18.sustainucd.BinImageHelper;
import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.Database.BinsManager;
import com.group18.sustainucd.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;

public class AddBinFragment extends Fragment {

    //View Model
    private AddBinViewModel addBinViewModel;
    //UI
    private ImageView binImageView;
    private Button addBinBtn;
    //Location client
    private FusedLocationProviderClient client;

    private Bin newBin;
    private File binImageFile;
    private boolean locationAcquired;
    private boolean pictureTaken;

    private final String mapsLabel = "Bin";

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
        if (getActivity().getIntent().hasExtra(AddBinActivity.PICTURE_PATH)) {
            String picturePath = getActivity().getIntent().getStringExtra(AddBinActivity.PICTURE_PATH);
            binImageFile = new File(picturePath);
            newBin.pictureFileName = BinImageHelper.GetImageName(binImageFile);
            GetAndSetLocation();
        }
        //Scale and set the bitmap
        root.post(new Runnable() {
            @Override
            public void run() {
                if (!pictureTaken)
                    new ScalePictureTask(binImageView, binImageFile.getAbsolutePath(), binImageView.getWidth(), binImageView.getHeight()).execute();
            }
        });
        //Model observation
        Observe();

        return root;
    }

    /** This event is triggered after a successfull onCreateView() event.
        The view setup is done here.
    */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI initialization
        binImageView = view.findViewById(R.id.binImageView);
        addBinBtn = view.findViewById(R.id.addBinBtn);
        SetOnClickListeners();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /** This method will setup all on click listeners that are needed.
        The button for taking a photo and the button for the addition
        of the bin into the database.
    */
    private void SetOnClickListeners()
    {
        addBinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictureTaken && locationAcquired)
                    AddBin();
            }
        });
    }

    /** View Model observation. After this method any change on the model
        will reflect on the view.
     */
    private void Observe()
    {
        addBinViewModel.getImageBitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binImageView.setImageBitmap(bitmap);
            }
        });
    }

    private void GetAndSetLocation()
    {
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    newBin.latitude = location.getLatitude();
                    newBin.longitude = location.getLongitude();
                    locationAcquired = true;
                    // Has options menu if the user has Google Maps or other location app
                    // and there are no problems with the location client
                    if (HasLocationApp()) {
                        setHasOptionsMenu(true);
                    }
                }
            }

        });
    }

    private void AddBin() {
        Log.d("AddBinFragment", "latitude: "+newBin.latitude
                +" longitude: "+newBin.longitude
                +" picture file name: "+newBin.pictureFileName);
        BinsManager.Insert(getContext(), newBin);
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    //Event triggered after clicking on the menu item. This will open google maps
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent mapIntent = GetMapIntent();
        // Attempt to start an activity that can handle the Intent
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    private Intent GetMapIntent() {
        Uri gmmIntentUri = Uri.parse("geo:"+newBin.latitude+","+newBin.longitude
                +"?z=18&q="+newBin.latitude+","+newBin.longitude+"("+mapsLabel+")");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        return mapIntent;
    }

    private boolean HasLocationApp() {
        //Simple example location, just for test
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?q=37.7749,-122.4194(Bin)");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        return mapIntent.resolveActivity(getActivity().getPackageManager()) != null;
    }

    private class ScalePictureTask extends AsyncTask<Void, Void, Bitmap> {
        private ImageView imageView;
        private String path;
        private int imageViewWidth;
        private int imageViewHeight;

        public ScalePictureTask(ImageView imageView, String path, int binImageWidth, int binImageHeight) {
            this.imageView = imageView;
            this.path = path;
            this.imageViewWidth = binImageWidth;
            this.imageViewHeight = binImageHeight;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap resized = BitmapFactory.decodeFile(path);
            if ( imageViewHeight > 0 && imageViewWidth > 0) {
                int width = resized.getWidth();
                int height = resized.getHeight();
                float ratioBitmap = (float) width / (float) height;
                float ratioMax = (float) imageViewWidth / (float) imageViewHeight;

                int finalWidth = imageViewWidth;
                int finalHeight = imageViewHeight;
                if (ratioMax > ratioBitmap) {
                    finalWidth = (int) ((float)imageViewHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float)imageViewWidth / ratioBitmap);
                }
                resized = Bitmap.createScaledBitmap(resized, finalWidth, finalHeight, true);
            }

            //Bitmap resized = Bitmap.createScaledBitmap(bitmap, imageViewWidth, imageViewHeight, true);
            //Save bitmap on file
            OutputStream fOut = null;
            try {
                fOut = new FileOutputStream(new File(path));
                resized.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resized;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            pictureTaken = true;

        }
    }
}
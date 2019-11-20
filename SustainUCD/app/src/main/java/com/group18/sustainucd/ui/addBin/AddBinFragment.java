package com.group18.sustainucd.ui.addBin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
    private TextView locationTextView;
    private ImageView binImageView;
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
        if (getActivity().getIntent().hasExtra(AddBinActivity.PICTURE_PATH)) {
            String picturePath = getActivity().getIntent().getStringExtra(AddBinActivity.PICTURE_PATH);
            binImageFile = new File(picturePath);
            newBin.pictureFileName = binImageFile.getName();
            GetAndSetLocation();
        }
        //Get the dimensions of the imageview and then scale and set the bitmap
        root.post(new Runnable() {
            @Override
            public void run() {
                int width = binImageView.getWidth();
                int height = binImageView.getHeight();
                new ScalePictureTask(binImageView, binImageFile.getAbsolutePath(), width, height).execute();
                pictureTaken = true;
            }
        });
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

    private void AddBin() {
        Log.d("AddBinFragment", "latitude: "+newBin.latitude
                +" longitude: "+newBin.longitude
                +" picture file name: "+newBin.pictureFileName);
        BinsManager.Insert(getContext(), newBin);
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
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
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/imageViewWidth, photoH/imageViewHeight);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            new SaveImageOnFile(bitmap, binImageFile).execute();
        }
    }

    private class SaveImageOnFile extends AsyncTask<Void, Void, Void> {
        private Bitmap bitmap;
        private File imageFile;

        public SaveImageOnFile(Bitmap bitmap, File file) {
            this.bitmap = bitmap;
            this.imageFile = file;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Save scaled bitmap
            OutputStream fOut = null;
            try {
                fOut = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                fOut.flush();
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
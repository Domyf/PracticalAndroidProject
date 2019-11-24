package com.group18.sustainucd.home;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.Database.BinsManager;
import com.group18.sustainucd.R;
import com.group18.sustainucd.showBin.ShowBinActivity;

import java.util.List;

/**
 * This Fragment implements the user interface and the logic for the home screen
 * A list of bins that are near is showed. First bin is the nearest. The last known location is taken
 * in background. It implements also a floating action button used to take a photo and then add
 * a new bin.
 */
public class HomeFragment extends Fragment implements BinsListAdapter.OnClickListener,
        BinsManager.BinsDatabaseListener, OnSuccessListener<Location> {

    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerView;
    private BinsListAdapter adapter;
    //Location client
    private FusedLocationProviderClient client;
    private int howManyBinsToShow = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Location initialization
        client = LocationServices.getFusedLocationProviderClient(getContext());
        adapter = new BinsListAdapter(this, getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Lookup the recyclerview in activity layout
        recyclerView = (RecyclerView) root.findViewById(R.id.bins_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Get location and then look for nearest bins
        GetLocation();

        return root;
    }

    @Override
    public void OnBinClick(int position) {
        Log.d(TAG, "Bin clicked: "+position);
        Intent showBinIntent = new Intent(getActivity(), ShowBinActivity.class);
        ShowBinActivity.bitmapToShow = adapter.getBinAtPosition(position).bitmap;

        showBinIntent.putExtra(ShowBinActivity.PICTURE_PATH, adapter.getBinAtPosition(position).pictureFileName);
        showBinIntent.putExtra(ShowBinActivity.LATITUDE, adapter.getBinAtPosition(position).latitude);
        showBinIntent.putExtra(ShowBinActivity.LONGITUDE, adapter.getBinAtPosition(position).longitude);
        startActivity(showBinIntent);
    }

    @Override
    public void OnBinsDatabaseLoaded() {
        Log.d(TAG, "Database loaded");
        SetBinsToShow();
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
    }

    private void GetLocation()
    {
        client.getLastLocation().addOnSuccessListener(this);
    }

    private void SetBinsToShow() {
        BinsManager.CalculateDistances(adapter.getCurrentLatitude(), adapter.getCurrentLongitude());
        List<Bin> binsToShow = BinsManager.GetNearestKBins(howManyBinsToShow,
                adapter.getCurrentLatitude(), adapter.getCurrentLongitude());

        adapter.SetList(binsToShow);
        Log.d(TAG, adapter.getItemCount()+" bins");
    }

    //Success on location request
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            adapter.SetLocation(location.getLatitude(), location.getLongitude());
            if (!BinsManager.HasBeenInitialized())
                BinsManager.Initialize(getActivity(), HomeFragment.this);
            else
                OnBinsDatabaseLoaded();
        }
    }

}
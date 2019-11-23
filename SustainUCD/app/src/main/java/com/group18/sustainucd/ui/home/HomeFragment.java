package com.group18.sustainucd.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group18.sustainucd.AddBinActivity;
import com.group18.sustainucd.BinsListAdapter;
import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.Database.BinsManager;
import com.group18.sustainucd.R;
import com.group18.sustainucd.ShowBinActivity;

import java.util.List;

public class HomeFragment extends Fragment implements BinsListAdapter.OnClickListener, BinsManager.BinsDatabaseListener {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private BinsListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Lookup the recyclerview in activity layout
        recyclerView = (RecyclerView) root.findViewById(R.id.bins_recycler_view);
        // Set layout manager to position the items if it has already the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Initialize the database
        if (!BinsManager.HasBeenInitialized())
            BinsManager.Initialize(getActivity(), this);
        else
            InitializeBinsList();
        return root;
    }

    @Override
    public void OnBinClick(int position) {
        Log.d(TAG, "Bin clicked: "+position);
        Intent showBinIntent = new Intent(getActivity(), ShowBinActivity.class);
        ShowBinActivity.bitmapToShow = adapter.getBinOnPosition(position).bitmap;

        showBinIntent.putExtra(ShowBinActivity.PICTURE_PATH, adapter.getBinOnPosition(position).pictureFileName);
        showBinIntent.putExtra(ShowBinActivity.LATITUDE, adapter.getBinOnPosition(position).latitude);
        showBinIntent.putExtra(ShowBinActivity.LONGITUDE, adapter.getBinOnPosition(position).longitude);
        startActivity(showBinIntent);
    }

    @Override
    public void OnBinsDatabaseLoaded() {
        Log.d(TAG, "Database loaded");
        InitializeBinsList();
    }

    public void InitializeBinsList()
    {
        List<Bin> binsToShow = BinsManager.GetAllBins();
        // Create adapter passing bins list
        adapter = new BinsListAdapter(binsToShow, this, getContext());
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        Log.d("HomeFragment", adapter.getItemCount()+" bins");
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);
    }
}
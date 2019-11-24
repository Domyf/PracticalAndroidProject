package com.group18.sustainucd;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.Database.BinsManager;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserBinsFragment extends Fragment implements UserBinsAdapter.OnClickListener {

    private static final String TAG = "UserBinsFragment";
    private RecyclerView recyclerView;
    private UserBinsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_bins, container, false);
        adapter = new UserBinsAdapter(this, getContext());
        recyclerView = (RecyclerView) root.findViewById(R.id.user_bins_recycler_view);
        SetBinsToShow();
        return root;
    }

    private void SetBinsToShow() {
        List<Bin> binsToShow = BinsManager.GetUserBins();
        //List<Bin> binsToShow = BinsManager.GetAllBins();
        adapter.SetList(binsToShow);// Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d(TAG, adapter.getItemCount()+" bins");
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
    public void OnDeleteBtnClick(int position) {
        //TODO ask if the user is sure or not with a dialog
        BinsManager.Delete(getContext(), adapter.getBinAtPosition(position));
        //TODO update recycleview
        Log.d(TAG, "Delete button clicked");
    }
}

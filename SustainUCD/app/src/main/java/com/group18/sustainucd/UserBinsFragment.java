package com.group18.sustainucd;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.Database.BinsManager;

import java.io.File;
import java.util.List;

/**
 * A fragment containing the view of the user bins list.
 */
public class UserBinsFragment extends Fragment implements UserBinsAdapter.OnClickListener {

    private static final String TAG = "UserBinsFragment";
    private RecyclerView recyclerView;
    private UserBinsAdapter adapter;
    private AlertDialog dialog;     //Dialog to show to ask confirm
    private int binToDelete;        //Index of the bin that should be deleted if the user confirms

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_bins, container, false);
        SetupDialog();
        adapter = new UserBinsAdapter(this, getContext());
        recyclerView = (RecyclerView) root.findViewById(R.id.user_bins_recycler_view);
        SetBinsToShow();
        return root;
    }

    private void SetBinsToShow() {
        List<Bin> binsToShow = BinsManager.GetUserBins();
        adapter.setList(binsToShow);// Attach the adapter to the recyclerview to populate items
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

    private void SetupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Pressing on cancel button does nothing. Pressing on ok button will delete the bin
        builder.setPositiveButton(R.string.delete_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Delete();
            }
        });
        builder.setNegativeButton(R.string.delete_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Nothing to do
            }
        });
        builder.setTitle(R.string.delete_dialog_title);

        //Create the dialog
        dialog = builder.create();
    }

    @Override
    public void OnDeleteBtnClick(int position) {
        //Set the position of the bin that should be deleted
        binToDelete = position;
        //Ask if the user is sure or not with a dialog
        dialog.show();
    }

    private void Delete() {
        BinsManager.Delete(getContext(), adapter.getBinAtPosition(binToDelete));
        String path = BinImageHelper.GetBinImagePath(getContext(), adapter.getBinAtPosition(binToDelete).pictureFileName);
        new File(path).delete();
        adapter.deleteBinAt(binToDelete);
        adapter.notifyItemRemoved(binToDelete);
        Log.d(TAG, "Bin deleted");
    }
}

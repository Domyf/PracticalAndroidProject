package com.group18.sustainucd.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group18.sustainucd.BinsListAdapter;
import com.group18.sustainucd.Database.Bin;
import com.group18.sustainucd.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements BinsListAdapter.OnClickListener {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Lookup the recyclerview in activity layout
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.bins_recycler_view);

        List<Bin> binsToShow = new ArrayList<>();
        binsToShow.add(new Bin());
        binsToShow.add(new Bin());
        binsToShow.add(new Bin());
        binsToShow.add(new Bin());
        binsToShow.add(new Bin());
        // Create adapter passing in the sample user data
        BinsListAdapter adapter = new BinsListAdapter(binsToShow, this);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.e("HomeFragment", adapter.getItemCount()+" bins");
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void OnBinClick(int position) {
        Log.e("HomeFragment", "Bin clicked: "+position);
    }
}
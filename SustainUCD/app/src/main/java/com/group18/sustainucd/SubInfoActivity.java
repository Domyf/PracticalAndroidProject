package com.group18.sustainucd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;

import com.group18.sustainucd.home.BinsListAdapter;

import java.util.ArrayList;
import java.util.List;

/** This activity shows a list of cards with a textview. The content showed in the list depends on
 *  the data passed by the calling activity */
public class SubInfoActivity extends AppCompatActivity implements RecycleInfoAdapter.OnClickListener {

    public static final int PAPER = 1;
    public static final int FOOD = 2;
    public static final int PLASTIC = 3;
    public static final int GLASS = 4;
    public static final int BATTERY = 5;
    public static final int ELECTRONICS = 6;

    private RecyclerView recyclerView;
    private RecycleInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_info);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        int category = (Integer) extras.get(MainInfoActivity.WHAT_TO_SHOW);
        List list = (List) extras.get(MainInfoActivity.DATA);

        adapter = new RecycleInfoAdapter( this, list);
        recyclerView = (RecyclerView) findViewById(R.id.sub_info_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SetTitleByCategory(category);
    }

    /** Set the title of the activity based on the category */
    private void SetTitleByCategory(int category) {
        switch (category) {
            case PAPER:
                getSupportActionBar().setTitle(R.string.paper_label);
                break;
            case PLASTIC:
                getSupportActionBar().setTitle(R.string.plastic_label);
                break;
            case FOOD:
                getSupportActionBar().setTitle(R.string.food_label);
                break;
            case GLASS:
                getSupportActionBar().setTitle(R.string.glass_label);
                break;
            case BATTERY:
                getSupportActionBar().setTitle(R.string.battery_label);
                break;
            case ELECTRONICS:
                getSupportActionBar().setTitle(R.string.electronic_label);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //If the button pressed is the back arrow
            //then will happen the same behavior of onBackPressed()
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Event triggered when the user clicks on a card
    @Override
    public void OnElementClick(int position) {
        RecycleData data = adapter.getDataAtPosition(position);
        Intent intent = new Intent(SubInfoActivity.this, MaterialInfoActivity.class);
        intent.putExtra(MaterialInfoActivity.DATA, data);
        startActivity(intent);
    }
}

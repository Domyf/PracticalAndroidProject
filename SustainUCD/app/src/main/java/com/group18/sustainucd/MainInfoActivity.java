package com.group18.sustainucd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainInfoActivity extends AppCompatActivity {

    public static final String WHAT_TO_SHOW = "What";
    public static final String DATA = "Data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SetCards();

    }

    private void SetCards() {
        CardView plasticCardView = findViewById(R.id.pasticCardView);
        plasticCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.PLASTIC);
                ArrayList<RecycleData> data = new ArrayList<>();
                data.add(new RecycleData(R.string.plastic_pete, R.string.plastic_pete_desc));
                data.add(new RecycleData(R.string.plastic_hdpe, R.string.plastic_hdpe_desc));
                data.add(new RecycleData(R.string.plastic_pvc, R.string.plastic_pvc_desc));
                data.add(new RecycleData(R.string.plastic_ldpe, R.string.plastic_ldpe_desc));
                data.add(new RecycleData(R.string.plastic_pp, R.string.plastic_pp_desc));
                data.add(new RecycleData(R.string.plastic_ps, R.string.plastic_ps_desc));
                intent.putExtra(DATA, data);
                startActivity(intent);
            }
        });
        CardView paperCardView = findViewById(R.id.paperCardView);
        paperCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.PAPER);
                ArrayList<RecycleData> data = new ArrayList<>();
                data.add(new RecycleData(R.string.paper_normal, R.string.paper_normal_desc));
                data.add(new RecycleData(R.string.paper_coated, R.string.paper_coated_desc));
                intent.putExtra(DATA, data);
                startActivity(intent);
            }
        });
        CardView foodCardView = findViewById(R.id.foodCardView);
        foodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, MaterialInfoActivity.class);
                //intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.FOOD);
                RecycleData data = new RecycleData(R.string.food_all, R.string.food_all_desc);
                intent.putExtra(MaterialInfoActivity.DATA, data);
                startActivity(intent);
            }
        });
        CardView glassCardView = findViewById(R.id.glassCardView);
        glassCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.GLASS);
                ArrayList<RecycleData> data = new ArrayList<>();
                data.add(new RecycleData(R.string.glass_bottles, R.string.glass_bottles_desc));
                data.add(new RecycleData(R.string.glass_other, R.string.glass_other_desc));
                intent.putExtra(DATA, data);
                startActivity(intent);
            }
        });
        CardView batteryCardView = findViewById(R.id.batteryCardView);
        batteryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.BATTERY);
                ArrayList<RecycleData> data = new ArrayList<>();
                data.add(new RecycleData(R.string.battery_lead, R.string.battery_lead_desc));
                data.add(new RecycleData(R.string.battery_nickel, R.string.battery_nickel_desc));
                data.add(new RecycleData(R.string.battery_nickelMetal, R.string.battery_nickelMetal_desc));
                data.add(new RecycleData(R.string.battery_lithium, R.string.battery_lithium_desc));
                data.add(new RecycleData(R.string.battery_lion, R.string.battery_lion_desc));
                data.add(new RecycleData(R.string.battery_alkaline, R.string.battery_alkaline_desc));
                intent.putExtra(DATA, data);
                startActivity(intent);
            }
        });
        CardView electronicCardView = findViewById(R.id.electronicCardView);
        electronicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, MaterialInfoActivity.class);
                //intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.ELECTRONICS);
                RecycleData data = new RecycleData(R.string.electronic_all, R.string.electronic_all_desc);
                intent.putExtra(MaterialInfoActivity.DATA, data);
                startActivity(intent);
            }
        });
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

}

package com.group18.sustainucd.userBins;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.group18.sustainucd.MainInfoActivity;
import com.group18.sustainucd.R;

/**
 * Activity that shows the user contribution to the app. The bins the have been added by the user
 * are showed in this activity. This class implement the toolbar and option menu logic. The rest
 * is implemented into a fragment.
 */
public class UserBinsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bins);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_user_bins, menu);
        return true;
    }

    //Event triggered on click on the menu item in the toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent infosIntent = new Intent(this, MainInfoActivity.class);
                startActivity(infosIntent);
                break;
            //If the button pressed is the back arrow
            //then will happen the same behavior of onBackPressed()
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

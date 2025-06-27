package com.example.wanderwell.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wanderwell.R;
import com.example.wanderwell.UI.VacationList;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        getSupportActionBar().hide(); // hide manually
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                //Intent moves you to the next screen
                //The intent should take you to the page to View and Add Vacations
                Intent intent = new Intent(MainActivity.this, VacationList.class);
                intent.putExtra("BonusVacation", "Trip to the beach");
                startActivity(intent);
            }
        });
    }
}
package com.moritzsus.chessflow.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.moritzsus.chessflow.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button analysisBoardButton = findViewById(R.id.analysisBoardButton);
        Button addDrillsButton = findViewById(R.id.addDrillsButton);
        Button practiceDrillsButton = findViewById(R.id.practiceDrillsButton);
        analysisBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AnalysisBoardActivity.class));
            }
        });
        addDrillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddDrillsActivity.class));
            }
        });
        practiceDrillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PracticeDrillsActivity.class));
            }
        });
    }
}
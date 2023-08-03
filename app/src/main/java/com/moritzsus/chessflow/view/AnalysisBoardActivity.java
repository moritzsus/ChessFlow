package com.moritzsus.chessflow.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.moritzsus.chessflow.R;
import com.moritzsus.chessflow.viewmodel.ChessBoardViewModel;

public class AnalysisBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_board);

        Button backButton = findViewById(R.id.backFromAnalysisButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
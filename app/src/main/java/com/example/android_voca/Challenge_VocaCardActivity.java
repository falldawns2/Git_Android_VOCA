package com.example.android_voca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Challenge_VocaCardActivity extends AppCompatActivity {

    TextView txt1, txt2,txt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge__voca_card);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);

        Intent intent = getIntent();

        txt1.setText(intent.getExtras().getString("VocaNoteName"));
        txt2.setText(intent.getExtras().getString("ChapterName"));
        txt3.setText(Boolean.toString(intent.getExtras().getBoolean("Shuffle")));

    }
}
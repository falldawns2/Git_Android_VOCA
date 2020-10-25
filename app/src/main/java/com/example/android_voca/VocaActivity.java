package com.example.android_voca;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class VocaActivity extends AppCompatActivity {

    TextView textView_VocaNoteName,textView_ChapterName;

    Toolbar toolbar;
    ActionBar actionBar;

    public String VocaNoteName; //단어장
    public String ChapterNoteName; //챕터

    int save;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca);
        //setContentView(R.layout.cardview_voca);

        textView_VocaNoteName = (TextView) findViewById(R.id.textView_VocaNoteName);
        textView_ChapterName = (TextView) findViewById(R.id.textView_ChapterName);

        Intent intent = getIntent();

        textView_VocaNoteName.setText("단어장 명 : " + intent.getExtras().getString("VocaNoteName"));
        textView_ChapterName.setText("챕터 명 : " + intent.getExtras().getString("ChapterNoteName"));

        //단어장명
        VocaNoteName = intent.getExtras().getString("VocaNoteName");
        //챕터명
        ChapterNoteName = intent.getExtras().getString("ChapterNoteName");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        // 타이틀 컬러를 처음과 마지막 색상을 동일하게 //
        toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        toolbarLayout.setExpandedTitleColor(Color.WHITE);

        save = MainActivity.save;

        if(save == 1) {

        } else {
            toolbarLayout.setTitle(VocaNoteName  + " (" + ChapterNoteName + ")");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.save = 0;
        MainActivity.tag = "single";
        MainActivity.PageNum = 1;
    }
}

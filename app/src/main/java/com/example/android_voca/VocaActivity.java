package com.example.android_voca;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

public class VocaActivity extends AppCompatActivity {

    TextView textView_VocaNoteName,textView_ChapterName;

    Toolbar toolbar;
    ActionBar actionBar;

    public String VocaNoteName; //단어장
    public String ChapterNoteName; //챕터

    int save;

    ////////
    RecyclerView recyclerView;
    VocaAdapter adapter;

    //카드뷰 내용 : 단어, 뜻, 예문, 해석
    public static String[] Arr_Voca = {};
    public static String[] Arr_Mean = {};
    public static String[] Arr_Sentence = {};
    public static String[] Arr_Interpritation = {};

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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Voca);

        QUERY_VOCA();

        List<Voca> list = getList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.context_main);
        recyclerView.setLayoutManager(layoutManager);

        //싱글 선택 어댑터 //
        adapter = new VocaAdapter(getApplicationContext(), list);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnVocaNoteItemClickListener() {
            @Override
            public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemChapterClick(ChapterAdapter.ItemViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position) {
                Voca item = adapter.getItem(position);

                String voca = item.getVoca();
                String mean = item.getMean();
                String Sen = item.getSentence();
                String Inter = item.getInterpretation();

                Toast.makeText(VocaActivity.this, voca + ", " + mean + ", "
                        + Sen + ", " + Inter, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemBoardClick(BoardAdapter.ItemViewHolder holder, View view, int position) {

            }
        });
    }

    public void QUERY_VOCA() {
        // 일단 임시로 단어 내용 넣는다. //

        Arr_Voca = new String[20];
        Arr_Mean = new String[20];
        Arr_Sentence = new String[20];
        Arr_Interpritation = new String[20];

        for (int i = 0; i< 20; i++) {
            Arr_Voca[i] = "단어 " + i;
            Arr_Mean[i] = "뜻 " + i;
            Arr_Sentence[i] = "예문 " + i;
            Arr_Interpritation[i] = "해석 " + i;
        }

        Arr_Voca[0] = "Speech";
        Arr_Mean[0] = "연설, 말, 언어";
        Arr_Sentence[0] = "man alone has the gift of speech";
        Arr_Interpritation[0] = "인간만이 말할 줄 안다";

        Arr_Voca[1] = "test1";
        Arr_Mean[1] = "테스트1";
        Arr_Sentence[1] = null;
        Arr_Interpritation[1] = null;

    }

    private List<Voca> getList() {
        List<Voca> list = new ArrayList<>();

        for(int i = 0; i < Arr_Voca.length; i++) {
            Voca model = new Voca();

            model.setVoca(Arr_Voca[i]);
            model.setMean(Arr_Mean[i]);
            model.setSentence(Arr_Sentence[i]);
            model.setInterpretation(Arr_Interpritation[i]);

            list.add(model);
        }
        return list;
    }

    public void showToast(String message) {
        Toast.makeText(VocaActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.save = 0;
        MainActivity.tag = "single";
        MainActivity.PageNum = 1;
    }
}

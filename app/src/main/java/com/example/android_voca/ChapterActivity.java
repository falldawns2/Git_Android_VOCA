package com.example.android_voca;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;

    public String VocaNoteName;
    //public String CreateDate;
    public String VocaCount;

    //
    TextView textView_VocaNoteName;
    //TextView Note_CreateDate;
    TextView textView_VocaCount;

    ///
    int save;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        //menuInflater.inflate(R.menu.note_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            /*case R.id.btnEdit:
                break;
            case R.id.test1:
                break;*/
            case android.R.id.home:
                finish();
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //toolbar.setNavigationIcon(R.drawable.button_hamburger_size);

        //NoteAddActivity.java - > 저장
        save = MainActivity.save;

        textView_VocaNoteName = findViewById(R.id.textView_VocaNoteName);
        //Note_CreateDate = findViewById(R.id.Note_CreateDate);
        textView_VocaCount = findViewById(R.id.textView_VocaCount);

        if(save == 1) {
            Intent intent = getIntent();
            //textView_VocaNoteName.setText(intent.getExtras().getString("vocanotename"));
                //Note_CreateDate.setText(intent.getExtras().getString("createDate"));
            //textView_VocaCount.setText(intent.getExtras().getString("vocacount"));
        } else {

            VocaNoteName = ((Fragment_VocaNote)Fragment_VocaNote.context_Frag_Main).VocaNoteName;
            //CreateDate = ((Fragment_Main)Fragment_Main.context_Frag_Main).CreateDate;
            VocaCount = ((Fragment_VocaNote)Fragment_VocaNote.context_Frag_Main).VocaCount;

            textView_VocaNoteName.setText(VocaNoteName);
            //Note_CreateDate.setText(CreateDate);
            textView_VocaCount.setText(VocaCount);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.save = 0;
    }
}
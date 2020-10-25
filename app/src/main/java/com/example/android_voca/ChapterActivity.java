package com.example.android_voca;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;

    public String VocaNoteName;
    public String ChapterNoteName;
    public String VocaCount;

    //
    TextView textView_VocaNoteName;
    //TextView Note_CreateDate;
    TextView textView_VocaCount;

    ///
    int save;

    ////////
    RecyclerView recyclerView;
    VocaNoteAdapter adapter;
    //Note_MultiSelectionAdapter multi_adapter;

    ///
    public static Fragment_VocaNote context_Frag_Main;

    // 카드뷰 내용 : 단어장 명 , 총 단어 수
    public static String[] Arr_VocaNoteName = {};
    public static int[] Arr_VocaCount = {};


    //플로팅 버튼
    FloatingActionButton fab;

    //커스텀 다이얼로그
    CustomDialog_VocaNote CustomDialog;

    public static Context context_Chapter;

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

        context_Chapter = ChapterActivity.this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        // 타이틀 컬러를 처음과 마지막 색상을 동일하게 했다. //
        toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        toolbarLayout.setExpandedTitleColor(Color.WHITE);


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

            //단어장 프래그먼트에서 단어장명 가져옴.
            toolbarLayout.setTitle(VocaNoteName);
        }

        /////////////
        context_Frag_Main = Fragment_VocaNote.context_Frag_Main;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        QUERY_VocaNote();

        List<VocaNote> list = getList();


        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.context_main);
        recyclerView.setLayoutManager(layoutManager);

        // 싱글 선택 어댑터 //
        adapter = new VocaNoteAdapter(context_Frag_Main, list);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnVocaNoteItemClickListener() {
            @Override
            public void onItemClick(VocaNoteAdapter.ViewHolder holder, View view, int position) {

                MainActivity.PageNum = 2; //단어 페이지로 이동

                VocaNote item = adapter.getItem(position);
                //VocaNoteName = item.getVocaNoteName();
                ChapterNoteName = item.getChapterName();
                VocaCount = "총 단어 수 :" + String.valueOf(item.getVocaCount());

                Toast.makeText(getApplicationContext(), "선택된 단어장 : " + VocaNoteName + ", " + item.getChapterName(), Toast.LENGTH_SHORT).show();


                ///
                Intent intent = new Intent(getApplicationContext(), VocaActivity.class);
                intent.putExtra("VocaNoteName",VocaNoteName);
                intent.putExtra("ChapterNoteName",item.getChapterName());
                startActivity(intent);
            }
        });


        //플로팅 버튼 테스트
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChapterActivity.this, "챕터 추가 이벤트", Toast.LENGTH_SHORT).show();
                CustomDialog = new CustomDialog_VocaNote(ChapterActivity.this,
                        new CustomDialogSelectClickListener() {
                            @Override
                            public void onPositiveClick() {
                                Log.e("test","OK");
                            }

                            @Override
                            public void onNegativeClick() {
                                Log.e("test","cancel");
                            }
                        });
                CustomDialog.setCanceledOnTouchOutside(true);
                CustomDialog.setCancelable(true);
                CustomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                CustomDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.save = 0;
        MainActivity.tag = "single";
        MainActivity.PageNum = 0;
    }

    ///////////
    public void QUERY_VocaNote() {
        // 일단 임시로 넣는다 나중에 DB에서 연동 //

        Arr_VocaNoteName = new String[20];
        Arr_VocaCount = new int[20];

        Arr_VocaNoteName[19] = "챕터 1";
        Arr_VocaCount[19] = 5;

        Arr_VocaNoteName[18] = "챕터 2";
        Arr_VocaCount[18] = 58;

        Arr_VocaNoteName[17] = "챕터 3";
        Arr_VocaCount[17] = 1;

        Arr_VocaNoteName[16] = "챕터 4";
        Arr_VocaCount[16] = 42;

        Arr_VocaNoteName[15] = "챕터 5";
        Arr_VocaCount[15] = 25;

        Arr_VocaNoteName[14] = "챕터 6";
        Arr_VocaCount[14] = 5;

        Arr_VocaNoteName[13] = "챕터 7";
        Arr_VocaCount[13] = 58;

        Arr_VocaNoteName[12] = "챕터 8";
        Arr_VocaCount[12] = 1;

        Arr_VocaNoteName[11] = "챕터 9";
        Arr_VocaCount[11] = 42;

        Arr_VocaNoteName[10] = "챕터 10";
        Arr_VocaCount[10] = 25;

        Arr_VocaNoteName[9] = "챕터 11";
        Arr_VocaCount[9] = 5;

        Arr_VocaNoteName[8] = "챕터 12";
        Arr_VocaCount[8] = 58;

        Arr_VocaNoteName[7] = "챕터 13";
        Arr_VocaCount[7] = 1;

        Arr_VocaNoteName[6] = "챕터 14";
        Arr_VocaCount[6] = 42;

        Arr_VocaNoteName[5] = "챕터 15";
        Arr_VocaCount[5] = 25;

        Arr_VocaNoteName[4] = "챕터 16";
        Arr_VocaCount[4] = 5;

        Arr_VocaNoteName[3] = "챕터 17";
        Arr_VocaCount[3] = 58;

        Arr_VocaNoteName[2] = "챕터 18";
        Arr_VocaCount[2] = 1;

        Arr_VocaNoteName[1] = "챕터 19";
        Arr_VocaCount[1] = 42;

        Arr_VocaNoteName[0] = "챕터 20";
        Arr_VocaCount[0] = 25;
    }

    private List<VocaNote> getList() {
        List<VocaNote> list = new ArrayList<>();

        for(int i = 0; i< Arr_VocaNoteName.length; i++) {
            VocaNote model = new VocaNote();
            model.setChapterName(Arr_VocaNoteName[i]); // 챕터명 //
            //model.setCrDateNote(Arr_CREATE_DATE[i]);
            model.setVocaCount(Arr_VocaCount[i]); // 총 단어 수 //
            list.add(model);
        }

        return list;
    }

    public void selectedClick() {
        List list = adapter.getSelectedItem();

        if(list.size() > 0 ) {
            StringBuilder sb = new StringBuilder();
            for(int index = 0; index < list.size(); index++) {
                VocaNote model = (VocaNote) list.get(index);
                sb.append(model.getChapterName()).append("\n");
            }
            showToast(sb.toString());

        } else {
            showToast("체크 안됨");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void restart() {
        Toast.makeText(context_Chapter, "test", Toast.LENGTH_SHORT).show();
    }

}
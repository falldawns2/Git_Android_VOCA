package com.example.android_voca;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VocaActivity extends AppCompatActivity {

    TextView textView_VocaNoteName, textView_ChapterName;

    Toolbar toolbar;
    ActionBar actionBar;

    public String VocaNoteName; //단어장
    public String ChapterNoteName; //챕터

    int save;

    ////////
    RecyclerView recyclerView;
    VocaAdapter adapter;

    boolean isLoading = false; //핸들러

    Retrofit retrofit;
    POSTApi postApi;

    final String svcName = "Service_VocaNote.svc/";
    final String TAG = "VocaActivity";

    List<Voca> list;
    List<Voca> list_20;

    //postResponse
    int POST_Response;

    //noSearch == 20보다 작다 이제 더이상 없다는 뜻
    boolean noSearch = false;

    //핸들러 반복 횟수
    static int handler_count;

    int LastPosition; //dy;

    //카드뷰 내용 : 단어, 뜻, 예문, 해석
    public static String[] Arr_Voca = {};
    public static String[] Arr_Mean = {};
    public static String[] Arr_Sentence = {};
    public static String[] Arr_Interpritation = {};

    //단어 검색 webView -activity_voca.xml
    public static RelativeLayout HiddenLayout_WebView;
    public static WebView webView;

    public static FloatingActionButton fabClose;


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

        //단어 검색을 위해
        HiddenLayout_WebView = (RelativeLayout)findViewById(R.id.HiddenLayout_WebView);
        webView = (WebView) findViewById(R.id.webView);
        fabClose = (FloatingActionButton) findViewById(R.id.fabClose);

        list = new ArrayList<>();
        list_20 = new ArrayList<>();
        //textView_VocaNoteName = (TextView) findViewById(R.id.textView_VocaNoteName);
        //textView_ChapterName = (TextView) findViewById(R.id.textView_ChapterName);

        Intent intent = getIntent();

        //textView_VocaNoteName.setText("단어장 명 : " + intent.getExtras().getString("VocaNoteName"));
        //textView_ChapterName.setText("챕터 명 : " + intent.getExtras().getString("ChapterNoteName"));

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

        if (save == 1) {

        } else {
            toolbarLayout.setTitle(VocaNoteName + " (" + ChapterNoteName + ")");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Voca);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.context_main);
        recyclerView.setLayoutManager(layoutManager);

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);

        handler_count = 1;

        QUERY_VOCA_ONE(handler_count, MainActivity.Session_ID, VocaNoteName, ChapterNoteName, postApi);

        //list = getList();

        initAdapter();

        initScrollListener();
    }

    public void initAdapter() {
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

            @Override
            public void onItemGroupClick(GroupAdapter.GroupItemViewHolder holder, View view, int position) {

            }
        });
    }

    public void QUERY_VOCA_ONE(int Page_NO, String userid, String VocaNoteName, String ChapterName, POSTApi postApi) {
        Call<List<Voca>> call = postApi.GetVoca(new Voca(Page_NO, 20, userid, VocaNoteName, ChapterName, "CreateDate desc"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Voca voca : call.execute().body()) {
                        list.add(new Voca(
                                voca.getVoca(),
                                voca.getMean(),
                                voca.getSentence(),
                                voca.getInterpretation()
                        ));

                        list_20.add(new Voca(
                                voca.getVoca(),
                                voca.getMean(),
                                voca.getSentence(),
                                voca.getInterpretation()
                        ));
                    }
                } catch (IOException e) {

                }
            }
        }).start();

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        POST_Response = list_20.size();
        if (POST_Response < 20) {
            noSearch = true;
        }
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG, "onScrollStateChanged: ");

                if (recyclerView.getScrollY() > LastPosition)
                    MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                else
                    MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LastPosition = dy;

                Log.d(TAG, "onScrolled: ");

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (!noSearch) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                                == list.size() - 1) {
                            adapter.notifyItemInserted(list.size() - 1);
                            ++handler_count;

                            QUERY(handler_count, MainActivity.Session_ID, VocaNoteName, ChapterNoteName, postApi);
                            GetAddData();

                            isLoading = true;

                            //Toast.makeText(VocaActivity.this, "스크롤 감지", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void GetAddData() {
        Log.d(TAG, "GetAddData: ");

        list.add(null);

        recyclerView.scrollToPosition(list.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: " + handler_count);

                list.remove(list.size() - 1); //2초 뒤 프로그레스바 배열에서 지움
                int scrollPosition = list.size(); //현재 위치 저장
                adapter.notifyItemRemoved(scrollPosition); //프로그레스바 사라짐
                int currentSize = scrollPosition; //현재 위치 = 스크롤 위치 = 마지막 값 0 ~ 9면 9
                int nextLimit = currentSize + POST_Response; //9에서 가져올 값 더해 19

                //현재 값 9에서 ~ 19 까지 반복해서 전체 배열에서 현재 배열에 10개 담는다.
                Copy(currentSize, nextLimit);
                adapter.notifyDataSetChanged(); //새로고침

                isLoading = false; //쓰레드
            }
        }, 2000);
    }

    public void Copy(int i, int j) {
        for (int a = i; a < list_20.size(); a++) {
            if (a == list_20.size()) {
                return;
            }
            list.add(new Voca(

                    list_20.get(a).getVoca(),
                    list_20.get(a).getMean(),
                    list_20.get(a).getSentence(),
                    list_20.get(a).getInterpretation()));
        }
    }

    public void QUERY(int Page_NO, String userid, String VocaNoteName, String ChapterName, POSTApi postApi) {
        Call<List<Voca2>> call = postApi.GetVoca(new Voca2(Page_NO, 20, userid, VocaNoteName, ChapterName, "CreateDate desc"));

        POST_Response = list_20.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Voca2 voca2 : call.execute().body()) {
                        list_20.add(new Voca(
                                voca2.getVoca(),
                                voca2.getMean(),
                                voca2.getSentence(),
                                voca2.getInterpretation()
                        ));
                    }
                } catch (IOException e) {

                }
            }
        }).start();

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (POST_Response < 20) {
            noSearch = true;
        }
    }

    /*private List<Voca> getList() {
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
    }*/

    public void showToast(String message) {
        Toast.makeText(VocaActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.save = 0;
        MainActivity.tag = "single";
        MainActivity.PageNum = 1;
        /*Intent intent = new Intent(VocaActivity.this, ChapterActivity.class);

        startActivity(intent);*/
    }
}

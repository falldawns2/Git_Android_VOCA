package com.example.android_voca;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VocaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

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

    //플로팅 버튼
    FloatingActionButton fab;

    //커스텀 다이얼로그
    CustomDialog_Voca CustomDialog;

    LinearLayout linearLayout;

    //새로고침
    SwipeRefreshLayout mSwipeRefreshLayout_Voca;

    VocaADD add;
    Call<VocaADD> add_Call;

    //단어 수정 필요 시
    static int UPDATE_VOCA = 0;

    //연속 추가
    static boolean bool_onADDClick = false;
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

        linearLayout = (LinearLayout) findViewById(R.id.Voca_Main_Panel);

        //새로고침
        mSwipeRefreshLayout_Voca = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_Voca);

        mSwipeRefreshLayout_Voca.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout_Voca.setRefreshing(true);
                //3초 후 해당 adapter 갱신하고 로딩중 보여줌. setRefreshing(false)

                //핸들러 사용 : 일반 쓰레드는 메인 스레드가 가진 UI에 접근 불가
                //핸들러로 메시지큐에 메시지 전달 - > 루퍼를 이용하여 순서대로 UI에 접근

                //반대로 메인 쓰레드에서 일반 쓰레드에 접근하기 위해서는 루퍼를 만듦.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //해당 어댑터를 서버와 통신한 값
                        handler_count = 1;
                        RefreshAdapter();
                        QUERY_VOCA_ONE(handler_count, MainActivity.Session_ID, VocaNoteName, ChapterNoteName, postApi);
                        initAdapter();
                        mSwipeRefreshLayout_Voca.setRefreshing(false);
                    }
                }, 100);
            }
        });

        //플로팅 버튼
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VocaActivity.this, "단어 추가 이벤트", Toast.LENGTH_SHORT).show();

                retrofit = new Retrofit(postApi);
                postApi = retrofit.setRetrofitInit(svcName);
                bool_onADDClick = false;

                CustomDialog = new CustomDialog_Voca(VocaActivity.this,
                        new CustomDialogVocaClickListener() {
                            @Override
                            public void onPositiveClick() {
                                Log.e(TAG, "OK" );

                                if (UPDATE_VOCA == 1) {

                                    //업데이트 할 비동기 호출 하자.
                                    add = new VocaADD(MainActivity.Session_ID,
                                            CustomDialog_Voca.InsertVoca.getText().toString(),
                                            CustomDialog_Voca.InsertMean.getText().toString(),
                                            CustomDialog_Voca.InsertSentence.getText().toString(),
                                            CustomDialog_Voca.InsertInterpretation.getText().toString()
                                    );

                                    add_Call = postApi.UpdateVoca(add);
                                    add_Call.enqueue(new Callback<VocaADD>() {
                                        @Override
                                        public void onResponse(Call<VocaADD> call, Response<VocaADD> response) {
                                            if(!response.isSuccessful()) {
                                                Log.e(TAG, "onResponse: " + response.code() );
                                                return;
                                            }

                                            VocaADD postResponse = response.body();

                                            //0: 성공, 1: 실패
                                            if(postResponse.getValue() == 0) { //성공
                                                //단어 수정 성공
                                                Log.e(TAG, "단어 수정 성공" );

                                                Snackbar snackbar = Snackbar.make(linearLayout,"단어 수정했어요.", Snackbar.LENGTH_LONG);
                                                View view = snackbar.getView();
                                                TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                                tv.setTextColor(ContextCompat.getColor(VocaActivity.this, R.color.White));
                                                view.setBackgroundColor(ContextCompat.getColor(VocaActivity.this, R.color.snack_Background_Success));
                                                CustomDialog.dismiss();
                                                snackbar.show();

                                                /*
                                                //새로고침 구현 필요
                                                onRefresh();
                                                */
                                            } else if (postResponse.getValue() == 1) { //실패

                                                Snackbar snackbar = Snackbar.make(linearLayout,"오류!", Snackbar.LENGTH_LONG);
                                                View view = snackbar.getView();
                                                TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                                tv.setTextColor(ContextCompat.getColor(VocaActivity.this, R.color.White));
                                                view.setBackgroundColor(ContextCompat.getColor(VocaActivity.this, R.color.snack_Background_Error));
                                                snackbar.show();
                                                CustomDialog.dismiss();
                                                //CustomDialog_Voca.tvTitle.setText("단어 써주세요!");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<VocaADD> call, Throwable t) {
                                            Log.e(TAG, "onFailure: " + t.getMessage() );
                                        }
                                    });


                                    //다시 단어 추가 상태로 변경
                                    UPDATE_VOCA = 0;
                                    CustomDialog.dismiss();
                                    onRefresh();
                                    return;
                                }

                                add = new VocaADD(MainActivity.Session_ID,
                                        VocaNoteName,
                                        ChapterNoteName,
                                        CustomDialog_Voca.InsertVoca.getText().toString(),
                                        CustomDialog_Voca.InsertMean.getText().toString(),
                                        CustomDialog_Voca.InsertSentence.getText().toString(),
                                        CustomDialog_Voca.InsertInterpretation.getText().toString()
                                        );

                                add_Call = postApi.InsertVoca(add);
                                add_Call.enqueue(new Callback<VocaADD>() {
                                    @Override
                                    public void onResponse(Call<VocaADD> call, Response<VocaADD> response) {
                                        if(!response.isSuccessful()) {
                                            Log.e(TAG, "onResponse: " + response.code() );
                                            return;
                                        }

                                        VocaADD postResponse = response.body();
                                        //0 : 성공, 1 : 단어 빈칸, 2 : 뜻 빈칸, 3 : 중복 존재
                                        if (postResponse.getValue() == 0) { //성공

                                            //단어 추가 성공
                                            Log.e(TAG, "단어 추가 성공" );

                                            Snackbar snackbar = Snackbar.make(linearLayout,"단어 추가했어요.", Snackbar.LENGTH_LONG);
                                            View view = snackbar.getView();
                                            TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                            tv.setTextColor(ContextCompat.getColor(VocaActivity.this, R.color.White));
                                            view.setBackgroundColor(ContextCompat.getColor(VocaActivity.this, R.color.snack_Background_Success));
                                            if (!bool_onADDClick) {
                                                CustomDialog.dismiss();
                                                bool_onADDClick = false;
                                            }
                                            snackbar.show();

                                            //새로고침 구현 필요
                                            onRefresh();

                                        } else if (postResponse.getValue() == 1) { // 단어 빈칸
                                            Snackbar snackbar = Snackbar.make(linearLayout,"단어를 써주세요.", Snackbar.LENGTH_LONG);
                                            View view = snackbar.getView();
                                            TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                            tv.setTextColor(ContextCompat.getColor(VocaActivity.this, R.color.White));
                                            view.setBackgroundColor(ContextCompat.getColor(VocaActivity.this, R.color.snack_Background_Error));
                                            snackbar.show();
                                            CustomDialog_Voca.tvTitle.setText("단어 써주세요!");
                                        } else if (postResponse.getValue() == 2) { //뜻 빈칸
                                            Snackbar snackbar = Snackbar.make(linearLayout,"뜻을 써주세요.", Snackbar.LENGTH_LONG);
                                            View view = snackbar.getView();
                                            TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                            tv.setTextColor(ContextCompat.getColor(VocaActivity.this, R.color.White));
                                            view.setBackgroundColor(ContextCompat.getColor(VocaActivity.this, R.color.snack_Background_Error));
                                            snackbar.show();
                                            CustomDialog_Voca.tvTitle.setText("뜻 써주세요!");
                                        } else { //중복 존재
                                            Snackbar snackbar = Snackbar.make(linearLayout,"중복! 단어를 수정해주세요.", Snackbar.LENGTH_LONG);
                                            View view = snackbar.getView();
                                            TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                            tv.setTextColor(ContextCompat.getColor(VocaActivity.this, R.color.White));
                                            view.setBackgroundColor(ContextCompat.getColor(VocaActivity.this, R.color.snack_Background_Error));
                                            snackbar.show();
                                            CustomDialog_Voca.tvTitle.setText("중복 수정!");

                                            //단어는 수정 안되고
                                            CustomDialog_Voca.InsertVoca.setEnabled(false);
                                            CustomDialog_Voca.InsertVoca.setText(postResponse.getUpdate_Voca());
                                            //나머지 수정 가능
                                            CustomDialog_Voca.InsertMean.setText(postResponse.getUpdate_Mean());
                                            CustomDialog_Voca.InsertSentence.setText(postResponse.getUpdate_Sentence());
                                            CustomDialog_Voca.InsertInterpretation.setText(postResponse.getUpdate_Interpretation());

                                            // 이 다음부터는 확인을 누르면 업데이트 해야 한다.
                                            //업데이트 변수
                                            UPDATE_VOCA = 1;
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<VocaADD> call, Throwable t) {
                                        Log.e(TAG, "onFailure: " + t.getMessage() );
                                    }
                                });
                            }

                            @Override
                            public void onNegativeClick() {
                                Log.e(TAG, "cancel" );
                            }

                            @Override
                            public void onADDClick() {
                                //단어 연속으로 추가 한다.
                                //TODO: 연속 추가도 코딩해야함
                                bool_onADDClick = true;
                                onPositiveClick();
                                CustomDialog_Voca.InsertVoca.setText("");
                                CustomDialog_Voca.InsertVoca.requestFocus(); //포커스
                                CustomDialog_Voca.InsertMean.setText("");
                                CustomDialog_Voca.InsertSentence.setText("");
                                CustomDialog_Voca.InsertInterpretation.setText("");

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
    public void onRefresh() {
        mSwipeRefreshLayout_Voca.setRefreshing(true);
        //3초 후 해당 adapter 갱신하고 로딩중 보여줌. setRefreshing(false)

        //핸들러 사용 : 일반 쓰레드는 메인 스레드가 가진 UI에 접근 불가
        //핸들러로 메시지큐에 메시지 전달 - > 루퍼를 이용하여 순서대로 UI에 접근

        //반대로 메인 쓰레드에서 일반 쓰레드에 접근하기 위해서는 루퍼를 만듦.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //해당 어댑터를 서버와 통신한 값
                handler_count = 1;
                RefreshAdapter();
                QUERY_VOCA_ONE(handler_count, MainActivity.Session_ID, VocaNoteName, ChapterNoteName, postApi);
                initAdapter();
                mSwipeRefreshLayout_Voca.setRefreshing(false);
            }
        }, 100);
    }

    private void RefreshAdapter() {
        recyclerView.removeAllViewsInLayout();
        //recyclerView.setAdapter(adapter);
        list.clear();
        list_20.clear();
        noSearch = false; //이 값이 서버 요청을 할지 말지 정한다.
        //initAdapter();
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
            Thread.sleep(1000);
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
        Log.e(TAG, "" + POST_Response );

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
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 요청 할때마다 20개씩 가져올 테니 20 * (요청 값) 하면 20 40 60 .. 늘어난다.
        // 만약 가져온 값 list .size 가 그 20개씩 가져온 것보다 적은 값이라면 다음은 없으므로 요청하지 못하게 한다.

        if (POST_Response < 20 * handler_count) { //20개 불렀는데 더 적은 수가 왔다 = 다음 페이지 없음 - > 서버 요청 x
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

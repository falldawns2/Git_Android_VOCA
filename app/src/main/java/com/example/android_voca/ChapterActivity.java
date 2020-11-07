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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

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
    ChapterAdapter adapter;

    boolean isLoading = false; //핸들러

    Retrofit retrofit;
    POSTApi postApi;

    final String svcName = "Service_VocaNote.svc/";
    final String TAG = "ChapterActivity";

    List<Chapter> list; // 10개씩
    List<Chapter> list_20;

    //postResponse 크기 구하기
    int POST_Response;

    //noSearch == 20보다 작다 이제 더이상 없다는 뜻
    boolean noSearch = false;

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

    //핸들러 반복 횟수
    static int handler_count;


    int LastPosition; //dy;
    List<Chapter> ccc;
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
        list = new ArrayList<>();
        list_20 = new ArrayList<>();

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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Chapter);

        //QUERY_VocaNote();
        //List<VocaNote> list = getList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);

        handler_count = 1;


        QUERY_Chapter_ONE(handler_count, MainActivity.Session_ID,VocaNoteName,postApi);

        initAdapter(); //리싸이클러 뷰 어댑터 생성



        initScrollListener(); //리싸이클러 뷰 이벤트 발생



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

    private void initAdapter() {
        // 싱글 선택 어댑터 //
        adapter = new ChapterAdapter(this,list);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnVocaNoteItemClickListener() {
            @Override
            public void onItemBoardClick(BoardAdapter.ItemViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemChapterClick(ChapterAdapter.ItemViewHolder holder, View view, int position) {
                MainActivity.PageNum = 2; //단어 페이지로 이동

                Chapter item = adapter.getItem(position);
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

            @Override
            public void onItemGroupClick(GroupAdapter.GroupItemViewHolder holder, View view, int position) {

            }
        });
    }

    //부분 배열에 새로운 값 20개 담아온다.
    private void GetAddData() {
        Log.d(TAG, "GetAddData: ");
        list.add(null); //빈 공간 추가

        recyclerView.scrollToPosition(list.size() - 1); //그 위치 자동으로 스크롤 내려줌.

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
        for (int a = i; a< list_20.size(); a++) {
            if (a == list_20.size()) {
                return;
            }
            list.add(new Chapter(

                    list_20.get(a).getChapterName(),
                    list_20.get(a).getVocaCount()));
        }
    }
    //리싸이클러 뷰 이벤트
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG, "onScrollStateChanged: ");

                //만약 스크롤을 내리고 있다면 fab 버튼을 오른쪽으로 이동시킴
                //아니라면 다시 가운데로 이동시킴

                if(recyclerView.getScrollY() > LastPosition)
                    MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                else
                    MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LastPosition = dy;

                Log.d(TAG, "onScrolled: ");

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(!isLoading) {
                    //리니어 레이아웃 매니저가 null이거나 마지막 아이템 포지션이 부분배열 사이즈 - 1 값과 동일하면
                    //GetAddData()을 통해 list(부분 배열) 에다가 새로운 값 20개를 받아온다.
                    //isLoading 은 쓰레드 관련

                    if(!noSearch) { //더 이상 20개 이상 값이 없다 = 서버에서 요청하지 말아라.
                        if(linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                            == list.size() - 1) {
                            adapter.notifyItemInserted(list.size() - 1); //그 공간에 프로그레스 바를 넣는다.
                            ++handler_count;

                            QUERY(handler_count, MainActivity.Session_ID,VocaNoteName,postApi);
                            GetAddData(); //새 데이터 받아온다. (전체 배열에서 20개씩)

                            isLoading = true;
                            Toast.makeText(ChapterActivity.this, "스크롤 감지", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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

    public void QUERY_Chapter_ONE(int Page_NO, String userid, String VocaNoteName,POSTApi postApi) {
        /*Call<List<Chapter>> call = postApi.GetChapter(new Chapter(Page_NO,20,userid, VocaNoteName,"CrDateNote desc"));

        call.enqueue(new Callback<List<Chapter>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if(response.isSuccessful()) {
                    Log.e(TAG,"QUERY_Chapter_ONE_Not success");
                    Toast.makeText(ChapterActivity.this, "test", Toast.LENGTH_SHORT).show();
                    //return;
                }

                List<Chapter> postResponse = response.body();


                if(postResponse != null) {
                    for (Chapter chapter : postResponse) {
                        list.add(new Chapter(

                                chapter.getChapterName(),
                                chapter.getVocaCount()));

                        list_20.add(new Chapter(

                                chapter.getChapterName(),
                                chapter.getVocaCount()));
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {

            }
        });*/

        Call<List<Chapter>> call = postApi.GetChapter(new Chapter(Page_NO,20,userid, VocaNoteName,"CrDateNote desc"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Log.e(TAG, "run: "+ call.execute().body().get(0).getChapterName());

                    for(Chapter chapter : call.execute().body()) {
                        list.add(new Chapter(
                                chapter.getChapterName(),
                                chapter.getVocaCount()
                        ));
                        list_20.add(new Chapter(

                                chapter.getChapterName(),
                                chapter.getVocaCount()));
                    }
                } catch (IOException e) {

                }
            }
        }).start();

        try {
            Thread.sleep(100);
        }catch (Exception e) {
            e.printStackTrace();
        }

        /*for(int i = 0; i< 20; i++) {
            list.add(new Chapter("test" + i,i+""));
        }*/
    }

    public void QUERY(int Page_NO, String userid, String VocaNoteName, POSTApi postApi) {
        Call<List<Chapter2>> call = postApi.GetChapter(new Chapter2(Page_NO, 20, userid,
                VocaNoteName, "CrDateNote desc"));
        /*call.enqueue(new Callback<List<Chapter2>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Chapter2>> call, Response<List<Chapter2>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: not success");
                    return;
                }

                List<Chapter2> postResponse = response.body();
                POST_Response = postResponse.size();

                if(POST_Response < 20) { //20개 불렀는데 더 적은 수가 왔다 = 다음 페이지 없음 - > 서버 요청 x
                    noSearch = true;
                }

                if(postResponse != null) {
                    for(Chapter2 chapter2 : postResponse) {
                        list_20.add(new Chapter(

                                chapter2.getChapterName(),
                                chapter2.getVocaCount()));
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<Chapter2>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+ t.getMessage());
            }
        });*/

        /*try {
            POST_Response = call.execute().body().size();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        POST_Response = list_20.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(Chapter2 chapter2 : call.execute().body()) {
                        list_20.add(new Chapter(
                                chapter2.getChapterName(),
                                chapter2.getVocaCount()
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

        if(POST_Response < 20) { //20개 불렀는데 더 적은 수가 왔다 = 다음 페이지 없음 - > 서버 요청 x
            noSearch = true;
        }
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
            VocaNote model = new VocaNote(1,2,"0","t");
            //model.setChapterName(Arr_VocaNoteName[i]); // 챕터명 //
            //model.setCrDateNote(Arr_CREATE_DATE[i]);
            //model.setVocaCount(Arr_VocaCount[i]); // 총 단어 수 //
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
                //sb.append(model.getChapterName()).append("\n");
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
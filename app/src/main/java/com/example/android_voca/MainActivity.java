package com.example.android_voca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    static String Session_ID; //유저 ID 세션 저장
    static String Session_Nickname;
    final String TAG = "MainActivity";

    private POSTApi postApi;
    private final String svcName = "Service_Account.svc/";
    Retrofit retrofit;
    AccountInfo accountInfo; //member 테이블 정보
    Call<AccountInfo> accountInfo_Call;

    private final String svcName_ADD = "Service_VocaNote.svc/";
    ADD add; //추가
    Call<ADD> add_Call;

    private static final int REQ_CODE_OVERLAY_PERMISSION = 1;
    Intent foregroundServiceIntent;
    private static final int MESSAGE_PERMISSION_GRANTED = 1111;
    private static final int MESSAGE_PERMISSION_DENIED = 1112;

    //menu 부분
    private DisplayMetrics metrics; //매트릭스로 디스플레이 크기 측정
    private LinearLayout MenuPanel; //메뉴 내용물이 보일 창
    private LinearLayout MainPanel; //매인 화면
    private FrameLayout.LayoutParams MainPanelParameters; //매인화면 파라미터
    private FrameLayout.LayoutParams MenuPanelParameters; //메뉴화면 파라미터
    private int PanelWidth; //디스플레이 크기 값 저장
    private boolean is_Panel_Expanded; //패널 움직임 여부 (메뉴가 아닌 매인 패널이 움직인다)
    //is_Panel_Expanded 를  static 으로 했으나 플로팅버튼에 의해 값이 저장되므로 없앰.
    private Button btn_menu; //메뉴가 보여지게 할 햄버거 버튼

    //페이저
    ViewPager pager;

    private View Frag_VocaNote;

    Switch Btn_Permission; //플로팅 버튼 허가 해지

    static int Stop = 0;

    //하단 탭
    private TabLayout tabLayout;

    //activity_bottom_menu.xml
    LinearLayout Linear_VocaNote;
    LinearLayout Linear_Challenge;
    LinearLayout Linear_Board;
    LinearLayout Linear_Group;

    //pager position value
    int Position;

    ///
    public static Context context_main;
    public static Boolean Edit_Activation; //체크박스 활성화

    public static String tag;

    public static FloatingActionButton fab;

    public static BottomAppBar bottom_bar;

    //VocaNote 추가 - > VocaNote 보기 액티비티
    public static int save = 0;  //이 값은 편집 모드 판단한다.
    public static int PageNum = 0; //단어장 : 0, 챕터 : 1, 단어 : 2;

    SearchView searchView;

    //커스텀 다이얼로그
    CustomDialog_VocaNote CustomDialog;


    //보조 메뉴//
    View cardview_activity_menu; //프로필 카드

    View cardview_activity_menu_Panel; //번역기
    View cardview_activity_menu_Panel2; //다크 모드
    View cardview_activity_menu_Panel3; //설정
    View cardview_activity_menu_Panel4; //별점 주기

    TextView txt_CardView_Menu_Panel;
    TextView txt_CardView_Menu_Panel2;
    TextView txt_CardView_Menu_Panel3;
    TextView txt_CardView_Menu_Panel4;

    //보조 메뉴 프로필 이미지 //
    ImageView ImageView_Menu_ProfileImage;

    //보조 메뉴 로그아웃 버튼 (x 모양) //
    ImageView ImageView_Menu_Logout;

    //보조 메뉴 닉네임 //
    TextView TextView_Menu_NickName;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        clearCache();
        //tag = "multi";
        tag = "single";

        context_main = MainActivity.this;
        Edit_Activation = false; //기본값 비활성화

        final Intent intent = getIntent(); //로그인 액티비티

        Session_ID = intent.getExtras().getString("Session_ID");

        //AccountInfo accountInfo1 = (AccountInfo)intent.getSerializableExtra("accountInfo");

        //받아온 값을 여기서 닉네임을 찾아서 다시 뿌려야 한다. (DB 연결 시)
        //TextView test = findViewById(R.id.test);
        //test.setText(accountInfo.Session_ID + accountInfo.Name + accountInfo.Nickcname);

       /*CoordinatorLayout coordinator = (CoordinatorLayout)findViewById(R.id.coordinator);
       AppBarLayout app_Bar = findViewById(R.id.app_bar);
       CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) app_Bar.getLayoutParams();
       AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
       if (behavior != null) {
           behavior.onNestedFling(coordinator, app_Bar, null, 0, params.height, true);
       }*/

       //AppBarLayout app_Bar = findViewById(R.id.app_bar);
       //app_Bar.setExpanded(true, true);


       final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

       // 타이틀 컬러를 처음과 마지막 색상을 동일하게 했다. //
       toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
       toolbarLayout.setExpandedTitleColor(Color.WHITE);
       // ----------------------------------------------//

       toolbarLayout.setTitle("단어장"); //단어장, 도전, 게시판, 그룹
       //getWindow().setStatusBarColor(Color.parseColor("#ffffff")); //#fff9eb 이 부분 최 생단 status 바  (액션 바 보다 더 위) (다크 모드일때 사용하자)
       //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);


       //페이저 기능
       pager = findViewById(R.id.pager);
       pager.setOffscreenPageLimit(4);

       Tab_PagerAdapter adapter = new Tab_PagerAdapter(getSupportFragmentManager());

       // 프래그먼트 1 (단어장) //
       Fragment_VocaNote fragment_vocaNote = new Fragment_VocaNote();
       adapter.addItem(fragment_vocaNote);

       // 프래그먼트 2 (도전) - > 암기카드, 퀴즈 등 //
       Fragment_Challenge fragment_challenge = new Fragment_Challenge();
       adapter.addItem(fragment_challenge);

       // 프래그먼트 3 (게시판) //
       Fragment_Board fragment_board = new Fragment_Board();
       adapter.addItem(fragment_board);

       // 프래그먼트 4 (그룹) //
       Fragment_Group fragment_group = new Fragment_Group();
       adapter.addItem(fragment_group);

       pager.setPageTransformer(true, new DepthPageTransformer());
       pager.setAdapter(adapter);

       // activity_bottom_menu.xml 아래 버튼 //
       Linear_VocaNote = findViewById(R.id.Linear_VocaNote);
       Linear_Challenge = findViewById(R.id.Linear_Challenge);
       Linear_Board = findViewById(R.id.Linear_Board);
       Linear_Group = findViewById(R.id.Linear_Group);

       bottom_bar = (BottomAppBar) findViewById(R.id.bottom_bar);

       //FloatingActionButton fab
       fab = (FloatingActionButton) findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Fab_Click();
           }
       });

       pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        Position = position;
                        toolbarLayout.setTitle("단어장");
                        toolbarLayout.setContentScrimColor(Color.parseColor("#ff4340")); // 스크롤 시 변하는 중인 색상 //
                        toolbarLayout.setBackgroundColor(Color.parseColor("#ff4340")); // 스크롤 다 되고 변화된 색상 //
                        toolbar.setBackgroundColor(Color.parseColor("#ff4340")); // 툴바 색상 //
                        //fab.setColorFilter(getResources().getColor(R.color.Black)); //플러스 버튼 색상 변경됨//
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff4340"))); // 플로팅 버튼 뒷 색상 변경 //
                        searchView.setQueryHint("단어 검색");
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fab_Click();
                            }
                        });
                        break;
                    case 1:
                        Position = position;
                        toolbarLayout.setTitle("도전");
                        toolbarLayout.setContentScrimColor(Color.parseColor("#252526"));
                        toolbarLayout.setBackgroundColor(Color.parseColor("#252526"));
                        toolbar.setBackgroundColor(Color.parseColor("#252526"));
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff4340"))); // 단어장 추가 이므로 색상 그대로 //
                        searchView.setQueryHint("단어 검색");
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "플로팅 : 도전", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 2:
                        Position = position;
                        toolbarLayout.setTitle("게시판");
                        toolbarLayout.setContentScrimColor(Color.parseColor("#31a3e4"));
                        toolbarLayout.setBackgroundColor(Color.parseColor("#31a3e4"));
                        toolbar.setBackgroundColor(Color.parseColor("#31a3e4"));
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#31a3e4")));
                        searchView.setQueryHint("게시판 검색");
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "플로팅 : 게시판", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 3:
                        Position = position;
                        toolbarLayout.setTitle("그룹");
                        toolbarLayout.setContentScrimColor(Color.parseColor("#a1e88b"));
                        toolbarLayout.setBackgroundColor(Color.parseColor("#a1e88b"));
                        toolbar.setBackgroundColor(Color.parseColor("#a1e88b"));
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a1e88b")));
                        searchView.setQueryHint("그룹 검색");
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "플로팅 : 그룹", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });

       View.OnClickListener onClickListener = new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               switch (view.getId()) {
                   case R.id.Linear_VocaNote:
                       pager.setCurrentItem(0, false);

                       //tag = "single";
                       //restart();

                       break;
                   case R.id.Linear_Challenge:
                       pager.setCurrentItem(1, false);
                       //tag = "single";
                       //restart();

                       break;
                   case R.id.Linear_Board:
                       pager.setCurrentItem(2, false);

                       //fragment_vocanote.selectedClick();

                       break;
                   case R.id.Linear_Group:
                       pager.setCurrentItem(3, false);
                       break;


               }
           }
       };

       Linear_VocaNote.setOnClickListener(onClickListener);
       Linear_Challenge.setOnClickListener(onClickListener);
       Linear_Board.setOnClickListener(onClickListener);
       Linear_Group.setOnClickListener(onClickListener);
       //fab.setOnClickListener(onClickListener);

       Frag_VocaNote = getLayoutInflater().inflate(R.layout.fragment_vocanote, null, false);


       // 메트릭스로 보조메뉴 보일 길이 정하기 //

       metrics = new DisplayMetrics(); //메트릭스 객체
       getWindowManager().getDefaultDisplay().getMetrics(metrics); //디스플레이 값 저장
       PanelWidth = (int) ((metrics.widthPixels) * 0.85); //메뉴가 적당히 보일때까지 밀려진 길이 측정

       MainPanel = (LinearLayout) findViewById(R.id.MainPanel);
       MainPanelParameters = (FrameLayout.LayoutParams) MainPanel.getLayoutParams(); //xml 에서 정의한 값 가져옴
       MainPanelParameters.width = metrics.widthPixels; //가져온 값에서 width 값 재정의
       MainPanel.setLayoutParams(MainPanelParameters); //재정의한 값 재적용

       MenuPanel = (LinearLayout) findViewById(R.id.MenuPanel);
       MenuPanelParameters = (FrameLayout.LayoutParams) MenuPanel.getLayoutParams(); //위와 동일
       MenuPanelParameters.width = PanelWidth;

       MenuPanel.setLayoutParams(MenuPanelParameters);

       // 메뉴 패널 터치 이벤트 일단 보류 //


       //보조 메뉴 //
       cardview_activity_menu = findViewById(R.id.cardview_activity_menu);

       cardview_activity_menu_Panel = findViewById(R.id.cardview_activity_menu_panel);
       cardview_activity_menu_Panel2 = findViewById(R.id.cardview_activity_menu_panel2);
       cardview_activity_menu_Panel3 = findViewById(R.id.cardview_activity_menu_panel3);
       cardview_activity_menu_Panel4 = findViewById(R.id.cardview_activity_menu_panel4);

       txt_CardView_Menu_Panel = cardview_activity_menu_Panel.findViewById(R.id.txt_cardValue);
       txt_CardView_Menu_Panel2 = cardview_activity_menu_Panel2.findViewById(R.id.txt_cardValue);
       txt_CardView_Menu_Panel3 = cardview_activity_menu_Panel3.findViewById(R.id.txt_cardValue);
       txt_CardView_Menu_Panel4 = cardview_activity_menu_Panel4.findViewById(R.id.txt_cardValue);

       txt_CardView_Menu_Panel.setText("번역기");
       txt_CardView_Menu_Panel2.setText("다크모드");
       txt_CardView_Menu_Panel3.setText("설정");
       txt_CardView_Menu_Panel4.setText("별점 주기");

       cardview_activity_menu_Panel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(MainActivity.this, "번역기", Toast.LENGTH_SHORT).show();
           }
       });
       cardview_activity_menu_Panel2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(MainActivity.this, "다크 모드", Toast.LENGTH_SHORT).show();
           }
       });
       cardview_activity_menu_Panel3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(MainActivity.this, "설정", Toast.LENGTH_SHORT).show();
           }
       });
       cardview_activity_menu_Panel4.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(MainActivity.this, "별점 주기", Toast.LENGTH_SHORT).show();
           }
       });

       //보조 메뉴 -> 프로필 이미지 //

       ImageView_Menu_ProfileImage = (ImageView) findViewById(R.id.ImageView_Menu_ProfileImage);

       //retrofit DB에서 가져온 이미지

       //Retrofit
       retrofit = new Retrofit(postApi);
       postApi = retrofit.setRetrofitInit(svcName); //반환된 인터페이스 받음

       accountInfo = new AccountInfo(Session_ID);
       accountInfo_Call = postApi.ProfileImage(accountInfo);
       accountInfo_Call.enqueue(new Callback<AccountInfo>() {
           @Override
           public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
               if(!response.isSuccessful()) {
                   Log.e("onResponse not", response.code()+"");
                   return;
               }

               AccountInfo postResponse = response.body();

               //glide 이미지 가져오기
               Glide.with(MainActivity.this)
                       .load("http://121.131.90.130/IIS_ASP_NET/ProfileImage/" + postResponse.getProfileImageName())
                       .override(150,150)
                       .into(ImageView_Menu_ProfileImage);
           }

           @Override
           public void onFailure(Call<AccountInfo> call, Throwable t) {
               Log.e("OnFailure",t.getMessage()+"");
           }
       });

       //보조 메뉴 --> 로그아웃 버튼

       ImageView_Menu_Logout = (ImageView) findViewById(R.id.ImageView_Menu_Logout);

       ImageView_Menu_Logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               Session_ID = null; //로그아웃
               startActivity(intent);
           }
       });

       //보조 메뉴 --> 닉네임 가져오기
       TextView_Menu_NickName = (TextView) findViewById(R.id.TextView_Menu_NickName);

       //Retrofit
       retrofit = new Retrofit(postApi);
       postApi = retrofit.setRetrofitInit(svcName);

       accountInfo = new AccountInfo(Session_ID);
       accountInfo_Call = postApi.NickName(accountInfo);
       accountInfo_Call.enqueue(new Callback<AccountInfo>() {
           @Override
           public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
               if(!response.isSuccessful()) {
                   Log.e("onResponse not", response.code() + "");
                   return;
               }

               AccountInfo postResponse = response.body();
               TextView_Menu_NickName.setText(postResponse.getNickcname());
               Session_Nickname = TextView_Menu_NickName.getText().toString();
           }

           @Override
           public void onFailure(Call<AccountInfo> call, Throwable t) {
               Log.e("Onfailure", t.getMessage() + "");
           }
       });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCache();
    }

    private void clearCache() {
        final File cacheDirFile = this.getCacheDir();
        if (null != cacheDirFile && cacheDirFile.isDirectory()) {
            clearSubCacheFiles(cacheDirFile);
        }
    }
    private void clearSubCacheFiles(File cacheDirFile) {
        if (null == cacheDirFile || cacheDirFile.isFile()) {
            return;
        }
        for (File cacheFile : cacheDirFile.listFiles()) {
            if (cacheFile.isFile()) {
                if (cacheFile.exists()) {
                    cacheFile.delete();
                }
            } else {
                clearSubCacheFiles(cacheFile);
            }
        }
    }

    //TODO: fab 단어장 추가
    public void Fab_Click() {
        //각각의 페이지 상태에 따라 fab 버튼의 할 일이 다르다.
        //Intent intent = new Intent(getApplicationContext(), VocaNoteAddActivity.class);
        save = 0; // save = 1;
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //startActivity(intent);
        Toast.makeText(MainActivity.this, "save=0, 단어장 추가 이벤트", Toast.LENGTH_SHORT).show();

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName_ADD);

        CustomDialog = new CustomDialog_VocaNote(MainActivity.this,
                new CustomDialogSelectClickListener() {
                    @Override
                    public void onPositiveClick() {
                        Log.e("test","OK");

                        add = new ADD(CustomDialog_VocaNote.InsertVocaNoteName.getText().toString(),
                                Session_ID,Session_Nickname,"false");
                        add_Call = postApi.InsertVocaNote(add);
                        add_Call.enqueue(new Callback<ADD>() {
                            @Override
                            public void onResponse(Call<ADD> call, Response<ADD> response) {
                                if (!response.isSuccessful()) {
                                    Log.e(TAG, "onResponse: " + response.code());
                                    return;
                                }

                                ADD postResponse = response.body();

                                if (postResponse.getValue() == 0) { //성공
                                    //단어장 추가 성공함 새로고침 필요
                                    Log.e(TAG, "단어장 추가 성공" );

                                    Snackbar snackbar = Snackbar.make(MainPanel,"단어장 추가했습니다.", Snackbar.LENGTH_LONG);
                                    View view = snackbar.getView();
                                    TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    tv.setTextColor(ContextCompat.getColor(context_main, R.color.White));
                                    view.setBackgroundColor(ContextCompat.getColor(context_main, R.color.snack_Background_Success));
                                    snackbar.show();

                                    //위로 당기면 새로고침 되는 기능 구현 필요
                                    ((Fragment_VocaNote)Fragment_VocaNote.context_Frag_Main).onRefresh();
                                    //((Fragment_VocaNote)Fragment_VocaNote.context_Frag_Main).mSwipeRefreshLayout.setRefreshing(false);
                                } else if (postResponse.getValue() == 1) { // 한 글자 이상
                                    Log.e(TAG, "한 글자 이상 쓰세요" );

                                    Snackbar snackbar = Snackbar.make(MainPanel,"두 글자 이상 쓰세요.", Snackbar.LENGTH_LONG);
                                    View view = snackbar.getView();
                                    TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    tv.setTextColor(ContextCompat.getColor(context_main, R.color.White));
                                    view.setBackgroundColor(ContextCompat.getColor(context_main, R.color.snack_Background_Error));
                                    snackbar.show();

                                } else { //중복 존재함 == 2
                                    Log.e(TAG, "중복 존재함");

                                    Snackbar snackbar = Snackbar.make(MainPanel,"중복이 존재해요.", Snackbar.LENGTH_LONG);
                                    View view = snackbar.getView();
                                    TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    tv.setTextColor(ContextCompat.getColor(context_main, R.color.White));
                                    view.setBackgroundColor(ContextCompat.getColor(context_main, R.color.snack_Background_Error));
                                    snackbar.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ADD> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t.getMessage());
                            }
                        });
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

    // child 뷰들이 활성화 비활성화 한다. //
    public static void ViewGroup_Enable_Toggle(ViewGroup viewGroup, boolean Enable) {
       int ChildActivity_Count = viewGroup.getChildCount();
       int i;
       for(i = 0; i < ChildActivity_Count; i++) {
           View view = viewGroup.getChildAt(i);

           if (view.getId() != android.R.id.home) { //R.id.btn_Menu
               view.setEnabled(Enable);

               if (view instanceof ViewGroup) {
                   ViewGroup_Enable_Toggle((ViewGroup)view, Enable);
               }
           }
       }
    }

    // 페이저 기능 //

    class Tab_PagerAdapter extends FragmentStatePagerAdapter {
       ArrayList<Fragment> items = new ArrayList<>();

       public Tab_PagerAdapter(FragmentManager fm) {
           super(fm);
       }

       public void addItem(Fragment item) {
           items.add(item);
       }

       @NonNull
       @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    //ActionBar Menu inflater // --> 검색 버튼


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_icon);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("단어 검색"); //상황에 따라 검색이 달라야한다.

        searchView.setOnClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //글씨 들어가면 mainActivity 에서 메모지 불러옴.
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //Left Hamburger Button
                if (!is_Panel_Expanded) {
                    is_Panel_Expanded = true; //else로 넘기기 위해
                    MainPanel.animate()
                            .x(PanelWidth)
                            .setDuration(300)
                            .start();

                    //매인 레이아웃 안보이게
                    //버튼을 누르면 레이아웃을 안눌리게 처리한다
                    //옆으로 밀려난 매인 화면은 무조건 Empty 레이아웃이 감싸고 그 값만 터치 이벤트로 (empty 화면은 투명 레이ㅏ아웃)
                    //다시 원래 화면으로 돌아올 수 있다.

                    //FrameLayout ViewGroup = (FrameLayout) findViewById(R.id.Frame_RelativeLayout).getParent(); //레이아웃 정보 가져옴
                    androidx.coordinatorlayout.widget.CoordinatorLayout ViewGroup =
                            (androidx.coordinatorlayout.widget.CoordinatorLayout) findViewById(R.id.pager).getParent();
                    ViewGroup_Enable_Toggle(ViewGroup, false);
                    //findViewById(R.id.btn_Menu).setEnabled(false); //따로 있는 버튼도 안보여야한다.(이 부분은 액션바로 대체할 예정)

                    //투명 레이아웃 보이개
                    ((LinearLayout) findViewById(R.id.Frame_Empty_LinearLayout)).setVisibility(View.VISIBLE);
                    findViewById(R.id.Frame_Empty_LinearLayout).setEnabled(true); //활성화
                    findViewById(R.id.Frame_Empty_LinearLayout).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            MainPanel.animate()
                                    .x(0)
                                    .setDuration(300)
                                    .start();
                            is_Panel_Expanded = false;

                            //빈 레이아웃을 터치 함으로써 다시 원상태로 돌아옴 빈 레이아웃은 다시 비활성화
                            //FrameLayout ViewGroup = (FrameLayout) findViewById(R.id.Frame_RelativeLayout).getParent();
                            androidx.coordinatorlayout.widget.CoordinatorLayout ViewGroup =
                                    (androidx.coordinatorlayout.widget.CoordinatorLayout) findViewById(R.id.pager).getParent();
                            ViewGroup_Enable_Toggle(ViewGroup, true);
                            //findViewById(R.id.btn_Menu).setEnabled(true); //따로 있는 버튼도 안보여야한다.(이 부분은 액션바로 대체할 예정)

                            ((LinearLayout) findViewById(R.id.Frame_Empty_LinearLayout)).setVisibility(View.GONE);
                            findViewById(R.id.Frame_Empty_LinearLayout).setEnabled(false);

                            return true;
                        }
                    });
                } else {
                    MainPanel.animate()
                            .x(0)
                            .setDuration(300)
                            .start();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void restart() {
        Edit_Activation = true;

        //페이저 기능

        Tab_PagerAdapter adapter = new Tab_PagerAdapter(getSupportFragmentManager());
        adapter.notifyDataSetChanged();

        final Fragment_VocaNote fragment_vocaNote = new Fragment_VocaNote();
        adapter.addItem(fragment_vocaNote);

        Fragment_Challenge fragment_favorites = new Fragment_Challenge();
        adapter.addItem(fragment_favorites);

        Fragment_Board fragment_board = new Fragment_Board();
        adapter.addItem(fragment_board);

        Fragment_Group fragment_group = new Fragment_Group();
        adapter.addItem(fragment_group);

        final ChapterActivity chapterActivity = new ChapterActivity();

        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "testestestsetests", Toast.LENGTH_SHORT).show();
                fragment_vocaNote.selectedClick();
            }
        });
    }

}
package com.example.android_voca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    //String Session_ID;

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

    private View Frag_Main;

    Switch Btn_Permission; //플로팅 버튼 허가 해지

    static int Stop = 0;

    //하단 탭
    private TabLayout tabLayout;

    //activity_bottom_menu.xml
    LinearLayout Linear_ALL;
    LinearLayout Linear_Favorites;
    LinearLayout Linear_Tag;
    LinearLayout Linear_Lock;

    //pager position value
    int Position;

    ///
    public static Context context_main;
    public static Boolean Edit_Activation; //체크박스 활성화

    public FloatingActionButton fab;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context_main = MainActivity.this;
        Edit_Activation = false; //기본값 비활성화

        Intent intent = getIntent(); //로그인 액티비티

        //Session_ID = intent.getExtras().getString("Session_ID");

        AccountInfo accountInfo = (AccountInfo)intent.getSerializableExtra("accountInfo");

        //받아온 값을 여기서 닉네임을 찾아서 다시 뿌려야 한다. (DB 연결 시)
        TextView test = findViewById(R.id.test);
        test.setText(accountInfo.Session_ID + accountInfo.Name + accountInfo.Nickcname);

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

       toolbarLayout.setTitle("단어장"); //단어장, 도전, 게시판, 그룹
       getWindow().setStatusBarColor(Color.parseColor("#fff9eb"));
       getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);

       //FloatingActionButton fab
       fab = (FloatingActionButton) findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //각각의 페이지 상태에 따라 fab 버튼의 할 일이 다르다.

           }
       });

       //페이저 기능
       pager = findViewById(R.id.pager);
       pager.setOffscreenPageLimit(4);

       //아직 구현 안했는데 머리 아프다. //
    }
}
package com.example.android_voca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

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

       final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

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
                        break;
                    case 1:
                        Position = position;
                        toolbarLayout.setTitle("도전");
                        break;
                    case 2:
                        Position = position;
                        toolbarLayout.setTitle("게시판");
                        break;
                    case 3:
                        Position = position;
                        toolbarLayout.setTitle("그룹");
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
                       break;
                   case R.id.Linear_Challenge:
                       pager.setCurrentItem(1, false);
                       break;
                   case R.id.Linear_Board:
                       pager.setCurrentItem(2, false);
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
}
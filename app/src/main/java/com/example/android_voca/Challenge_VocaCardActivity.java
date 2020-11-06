package com.example.android_voca;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Challenge_VocaCardActivity extends AppCompatActivity {

    TextView txt1, txt2,txt3;

    Retrofit retrofit;
    POSTApi postApi;
    final String svcName = "Service_VocaNote.svc/" ;
    final String TAG = "Chall_VocaCardActivity";

    int POST_Response;

    ViewPager viewPager;

    VocaCardAdapter vocaCardAdapter;
    List<VocaCard> cards;

    Integer[] colors = null; //나중에 색깔넣을 예정
    ArgbEvaluator argbEvaluator = new ArgbEvaluator(); //이것도 색깔 관련

    String VocaNoteName, ChapterName, OrderBy;
    Boolean Shuffle;

    CardView CardView_Title;
    TextView textView_title;
    RelativeLayout Relative_layout;

    Button arrowBtn;
    ConstraintLayout expandableView;

    //네이버 검색
    Button Search_Voca;

    public static RelativeLayout HiddenLayout_WebView;
    public static WebView webView;
    public static FloatingActionButton fab;


    //랜덤 객체
    private static Random random = new Random();

    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge__voca_card);

        /*txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);

        Intent intent = getIntent();

        txt1.setText(intent.getExtras().getString("VocaNoteName"));
        txt2.setText(intent.getExtras().getString("ChapterName"));
        txt3.setText(Boolean.toString(intent.getExtras().getBoolean("Shuffle")));*/

        Intent intent = getIntent();
        VocaNoteName = intent.getExtras().getString("VocaNoteName");
        ChapterName = intent.getExtras().getString("ChapterName");
        Shuffle = intent.getExtras().getBoolean("Shuffle");

        if(Shuffle) OrderBy = "NEWID() desc";
        else OrderBy = "CreateDate desc";

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);


        //Retrofit을 이용하여 단어 뜻을 받아와 뿌린다.
        cards = new ArrayList<>();

        CardView_Title = (CardView) findViewById(R.id.CardView_Title);
        textView_title = (TextView) findViewById(R.id.textView_title);
        expandableView = (ConstraintLayout) findViewById(R.id.expandableView);

        arrowBtn = (Button) findViewById(R.id.arrowBtn);
        BtnOnCLickListener btnOnCLickListener = new BtnOnCLickListener();
        arrowBtn.setOnClickListener(btnOnCLickListener);


        HiddenLayout_WebView = findViewById(R.id.HiddenLayout_WebView);
        webView = findViewById(R.id.webView);
        fab = findViewById(R.id.fab);

        Get_Voca_Mean();
        textView_title.setText(VocaNoteName + "(" + 1 + "/" +cards.size() + ")");
        SetAdapter();
    }

    public void Get_Voca_Mean() {
        Call<List<VocaCard>> call = postApi.GetVocaMean(new VocaCard(MainActivity.Session_ID,
                VocaNoteName,ChapterName,OrderBy));
        /*call.enqueue(new Callback<List<VocaCard>>() {
            @Override
            public void onResponse(Call<List<VocaCard>> call, Response<List<VocaCard>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: not success");
                    return;
                }

                List<VocaCard> postResponse = response.body();

                if(postResponse != null) {
                    for (VocaCard vocaCard : postResponse) {
                        cards.add(new VocaCard(
                                vocaCard.getVoca(),
                                vocaCard.getMean()
                        ));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<VocaCard>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(VocaCard vocaCard : call.execute().body()) {
                        cards.add(new VocaCard(
                                vocaCard.getVoca(),
                                vocaCard.getMean()
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

    }
    public void SetAdapter() {
        vocaCardAdapter = new VocaCardAdapter(cards, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(vocaCardAdapter);
        viewPager.setPadding(130,0,130,0);

        /*//네이버 검색
        //Search_Voca = viewPager.findViewById(R.id.Search_Voca);
        View view =  getLayoutInflater().inflate(R.layout.cardview_activity_challenge_voca_card,viewPager,false);
        View view2 = LayoutInflater.from(this).inflate(R.layout.cardview_activity_challenge_voca_card,viewPager,false);
        Search_Voca = view2.findViewById(R.id.Search_Voca);
        Search_Voca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //webview 검색된 결과를 보여준다
                Log.e(TAG, "onClick: " );
            }
        });*/

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };


        Integer[] Random_Color_temp = {
            Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256))
        };


        Integer[] RandomTemp = new Integer[cards.size()];

        int i;
        for (i = 0; i < cards.size(); i++) {
            RandomTemp[i] = Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256));
        }

        //colors = colors_temp;

        //colors = Random_Color_temp;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*if (position < (vocaCardAdapter.getCount() - 1) && position < (colors.length - 1)) {

                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator
                                .evaluate(
                                        positionOffset,
                                        colors[position],
                                        colors[position + 1]));
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }*/

                if (position < (vocaCardAdapter.getCount() - 1) && position < (RandomTemp.length - 1)) {
                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator
                                    .evaluate(
                                            positionOffset,
                                            RandomTemp[position],
                                            RandomTemp[position + 1]

                                    )
                    );
                } else {
                    viewPager.setBackgroundColor(RandomTemp[RandomTemp.length - 1]);
                }

            }

            @Override
            public void onPageSelected(int position) {
                /*
                * viewPager.setBackgroundColor(
                        Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256))
                );*/

                textView_title.setText(VocaNoteName + "(" + (position + 1) + "/" + cards.size() + ")");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class BtnOnCLickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.arrowBtn:
                  if(expandableView.getVisibility() == View.GONE) {
                      TransitionManager.beginDelayedTransition(CardView_Title, new AutoTransition());;
                      expandableView.setVisibility(View.VISIBLE);
                      arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                  } else {
                      TransitionManager.beginDelayedTransition(CardView_Title, new AutoTransition());
                      expandableView.setVisibility(View.GONE);
                      arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                  }
                  break;
            }
        }
    }
}
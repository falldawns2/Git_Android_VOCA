package com.example.android_voca;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Challenge_VocaCardActivity extends AppCompatActivity {

    TextView txt1, txt2,txt3;

    Retrofit retrofit;
    POSTApi postApi;
    final String svcName = "Service_VocaNote.svc/" ;
    final String TAG = "Chall_VocaCardActivity";

    ViewPager viewPager;

    VocaCardAdapter vocaCardAdapter;
    List<VocaCard> cards;

    Integer[] colors = null; //나중에 색깔넣을 예정
    ArgbEvaluator argbEvaluator = new ArgbEvaluator(); //이것도 색깔 관련

    String VocaNoteName, ChapterName, OrderBy;
    Boolean Shuffle;

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
        Get_Voca_Mean();
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

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (vocaCardAdapter.getCount() - 1) && position < (colors.length - 1)) {

                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator
                                .evaluate(
                                        positionOffset,
                                        colors[position],
                                        colors[position + 1]));
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
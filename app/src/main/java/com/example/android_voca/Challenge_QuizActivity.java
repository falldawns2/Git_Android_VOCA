package com.example.android_voca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

public class Challenge_QuizActivity extends AppCompatActivity {

    //Intent
    String VocaNoteName, ChapterName, OrderBy;
    Boolean Shuffle;

    Retrofit retrofit;
    POSTApi postApi;
    final String svcName = "Service_VocaNote.svc/";
    final String TAG = "Chall_QuizActivity";

    int POST_Response;

    List<VocaCard> cards;

    //activity_challenge__quiz.xml
    //단어 뜻
    TextView txt_Voca, txt_Mean1, txt_Mean2, txt_Mean3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge__quiz);

        Intent intent = getIntent();
        VocaNoteName = intent.getExtras().getString("VocaNoteName");
        ChapterName = intent.getExtras().getString("ChapterName");
        Shuffle = intent.getExtras().getBoolean("Shuffle");

        if(Shuffle) OrderBy = "NEWID() desc";
        else OrderBy = "CreateDate desc";

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);

        //Retrofit 을 이용 단어 뜻 가져옴
        cards = new ArrayList<>();

        Get_Voca_Mean();

        txt_Voca = (TextView) findViewById(R.id.txt_Voca);
        txt_Mean1 = (TextView) findViewById(R.id.txt_Mean1);
        txt_Mean2 = (TextView) findViewById(R.id.txt_Mean2);
        txt_Mean3 = (TextView) findViewById(R.id.txt_Mean3);

        Set();
    }

    public void Get_Voca_Mean() {
        Call<List<VocaCard>> call = postApi.GetVocaMean(new VocaCard(MainActivity.Session_ID,
                VocaNoteName, ChapterName, OrderBy));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (VocaCard vocaCard : call.execute().body()) {
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

    public void Set() {

        Random random = new Random();

        txt_Voca.setText(cards.get(0).getVoca());
        txt_Mean1.setText(cards.get(random.nextInt(cards.size())).getMean());
        txt_Mean2.setText(cards.get(random.nextInt(cards.size())).getMean());
        txt_Mean3.setText(cards.get(random.nextInt(cards.size())).getMean());
    }
}
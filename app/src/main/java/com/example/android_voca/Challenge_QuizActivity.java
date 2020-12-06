package com.example.android_voca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
    //타이머
    TextView txt_timer;

    //단어 뜻
    TextView txt_Voca, txt_Mean1, txt_Mean2, txt_Mean3;

    //타이머
    static TimerTask timerTask_10;
    static TimerTask timerTask_4;

    //타이머 카운트
    int count = 10;

    //단어 카운트 (배열 순서대로)
    static int Voca_Count = 0;

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

        txt_timer = (TextView) findViewById(R.id.txt_timer);

        Set();

        timerTask_10 = NewTimerTask_10();

        //Timer 생성
        final Timer timer = new Timer();
        timer.schedule(timerTask_10, 0, 1000);
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

        txt_Voca.setText(cards.get(Voca_Count).getVoca());

        txt_Mean1.setText(cards.get(random.nextInt(cards.size())).getMean());
        txt_Mean2.setText(cards.get(random.nextInt(cards.size())).getMean());
        txt_Mean3.setText(cards.get(random.nextInt(cards.size())).getMean());

        int a = random.nextInt(3);
        switch (a) {
            case 0:
                txt_Mean1.setText(cards.get(Voca_Count).getMean());
                break;
            case 1:
                txt_Mean2.setText(cards.get(Voca_Count).getMean());
                break;
            case 2:
                txt_Mean3.setText(cards.get(Voca_Count).getMean());
                break;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클릭한 Mean 버튼 (1,2,3 중 하나)
                TextView txt_Mean = (TextView) findViewById(view.getId());

                timerTask_10.cancel();
                Log.e(TAG, "onClick: " + "cancel" );

                if(timerTask_4 != null)
                    timerTask_4.cancel();

                if (txt_Mean.getText().equals(cards.get(Voca_Count).getMean())) {
                    //정답
                    //정확도 올라가고 정답 색상
                    txt_Mean.setBackgroundResource(R.drawable.text_style_true);
                } else {
                    //오답
                    //오답 색상
                    txt_Mean.setBackgroundResource(R.drawable.text_style_false);
                }

                timerTask_4 = NewTimerTask_4();
                count = 4;
                //Timer 생성
                final Timer timer2 = new Timer();
                timer2.schedule(timerTask_4, 0, 1000);
            }
        };

        txt_Mean1.setOnClickListener(onClickListener);
        txt_Mean2.setOnClickListener(onClickListener);
        txt_Mean3.setOnClickListener(onClickListener);
    }

    public TimerTask NewTimerTask_10() {
        TimerTask tempTask = new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "run: " + String.valueOf(count));
                //쓰레드 UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //정답 오답 색상 초기화
                        txt_Mean1.setBackgroundResource(R.drawable.text_style);
                        txt_Mean2.setBackgroundResource(R.drawable.text_style);
                        txt_Mean3.setBackgroundResource(R.drawable.text_style);

                        if (count == 0) {
                            //10초 이므로 타이머 중단 후 4초 타이머 실행
                            timerTask_10.cancel();
                            timerTask_4 = NewTimerTask_4();
                            count = 4;
                            //Timer 생성
                            final Timer timer2 = new Timer();
                            timer2.schedule(timerTask_4, 0, 1000);
                        }
                        txt_timer.setText(String.valueOf(count));
                    }
                });

                count--;
            }
        };
        return tempTask;
    }

    public TimerTask NewTimerTask_4() {
        TimerTask tempTask = new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "run Task 4 : " + String.valueOf(count) );

                //쓰레드 UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count == 0) {
                            //일단 4초 타이머 중단
                            timerTask_4.cancel();

                            //Voca_Count 값이 배열 크기와 동일하면 중단해야 한다.
                            if(Voca_Count + 1 == cards.size()) {
                                Voca_Count = 0; //초기화 후 리턴
                                return;
                            }
                            //텍스트 변경 후 다시 10초 타이머 시작
                            Random random = new Random();

                            //단어는 순서대로 다음 단어
                            //뜻은 랜덤으로 섞은 후 하나는 정답 배치

                            Voca_Count++;
                            txt_Voca.setText(cards.get(Voca_Count).getVoca());

                            int a = random.nextInt(3);
                            if(a == 0) {
                                txt_Mean1.setText(cards.get(Voca_Count).getMean());

                                txt_Mean2.setText(cards.get(random.nextInt(cards.size())).getMean());
                                txt_Mean3.setText(cards.get(random.nextInt(cards.size())).getMean());
                            } else if (a == 1) {
                                txt_Mean2.setText(cards.get(Voca_Count).getMean());

                                txt_Mean1.setText(cards.get(random.nextInt(cards.size())).getMean());
                                txt_Mean3.setText(cards.get(random.nextInt(cards.size())).getMean());
                            } else {
                                txt_Mean3.setText(cards.get(Voca_Count).getMean());

                                txt_Mean1.setText(cards.get(random.nextInt(cards.size())).getMean());
                                txt_Mean2.setText(cards.get(random.nextInt(cards.size())).getMean());
                            }

                            timerTask_10 = NewTimerTask_10();
                            count = 10;
                            //Timer 생성
                            final Timer timer = new Timer();
                            timer.schedule(timerTask_10, 0, 1000);
                        }

                        txt_timer.setText(String.valueOf(count));
                    }
                });

                count--;
            }
        };
        return tempTask;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerTask_10.cancel();
        timerTask_4.cancel();
    }
}
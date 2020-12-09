package com.example.android_voca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    //파스텔 톤
    Integer[] Integer_Colors;

    //최상위 레이아웃
    ConstraintLayout constraintLayout;

    //정답률
    TextView txt_Answer_Rate;

    static double static_rate;
    double var_rate = 0; //정답률 카운트

    //배열 위치 카운트
    TextView txt_list_length;

    //TTS
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge__quiz);

        constraintLayout = findViewById(R.id.constraintLayout);
        static_rate = 0;

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

        txt_Answer_Rate = (TextView) findViewById(R.id.txt_Answer_Rate);

        txt_list_length = (TextView) findViewById(R.id.txt_list_length);

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

        tts = new TextToSpeech(Challenge_QuizActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.ENGLISH);
            }
        });

        Random random = new Random();

        //정해둔 파스텔톤 컬러
        Integer_Colors = new Integer[8];
        Integer_Colors[0] = Color.argb(255,255,254,170);
        Integer_Colors[1] = Color.argb(255,224,251,156);
        Integer_Colors[2] = Color.argb(255,161,255,221);
        Integer_Colors[3] = Color.argb(255,162,234,255);
        Integer_Colors[4] = Color.argb(255,239,202,254);
        Integer_Colors[5] = Color.argb(255,175,224,255);
        Integer_Colors[6] = Color.argb(255,255,204,213);
        Integer_Colors[7] = Color.argb(255,198,211,255);

        constraintLayout.setBackgroundColor(Integer_Colors[random.nextInt(8)]);


        txt_Voca.setText(cards.get(Voca_Count).getVoca());

        txt_Mean1.setText(cards.get(random.nextInt(cards.size())).getMean());
        txt_Mean2.setText(cards.get(random.nextInt(cards.size())).getMean());
        txt_Mean3.setText(cards.get(random.nextInt(cards.size())).getMean());

        txt_list_length.setText("(" + String.valueOf(Voca_Count + 1) + "/" + cards.size() + ")");

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
                    //정확도 유지, 정답 색상
                    txt_Mean1.setText("");
                    txt_Mean2.setText("");
                    txt_Mean3.setText("");

                    txt_Mean.setText(cards.get(Voca_Count).getMean());

                    txt_Mean.setBackgroundResource(R.drawable.text_style_true);

                } else {
                    //오답
                    //오답일 경우 정답을 빨간색으로 한다.
                    //오답 색상
                    txt_Mean1.setText("");
                    txt_Mean2.setText("");
                    txt_Mean3.setText("");

                    txt_Mean.setText(cards.get(Voca_Count).getMean());
                    txt_Mean.setBackgroundResource(R.drawable.text_style_false);

                    var_rate = static_rate + 1;
                    txt_Answer_Rate.setText(String.valueOf(100 - Math.round(var_rate / cards.size() * 100)) + "%");
                    static_rate = var_rate;
                }

                //TTS
                String TTS_Text = cards.get(Voca_Count).getVoca();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ttsGreater21(TTS_Text);
                else ttsUnder20(TTS_Text);

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
                        txt_list_length.setText("(" + String.valueOf(Voca_Count + 1) + "/" + cards.size() + ")");
                        //정답 오답 색상 초기화
                        txt_Mean1.setEnabled(true);
                        txt_Mean2.setEnabled(true);
                        txt_Mean3.setEnabled(true);
                        txt_Mean1.setBackgroundResource(R.drawable.text_style);
                        txt_Mean2.setBackgroundResource(R.drawable.text_style);
                        txt_Mean3.setBackgroundResource(R.drawable.text_style);

                        if (count <= 0) {
                            //10초 이므로 타이머 중단 후 4초 타이머 실행
                            timerTask_10.cancel();

                            //TTS
                            String TTS_Text = cards.get(Voca_Count).getVoca();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                ttsGreater21(TTS_Text);
                            else ttsUnder20(TTS_Text);

                            //아무것도 안누름 - > 오답
                            if(txt_Mean1.getText().equals(cards.get(Voca_Count).getMean())) {

                                txt_Mean1.setBackgroundResource(R.drawable.text_style_false);

                                txt_Mean2.setText("");
                                txt_Mean3.setText("");

                                var_rate = static_rate + 1;
                                txt_Answer_Rate.setText(String.valueOf(100 - Math.round(var_rate / cards.size() * 100)) + "%");
                                static_rate = var_rate;
                            }

                            else if (txt_Mean2.getText().equals(cards.get(Voca_Count).getMean())) {

                                txt_Mean2.setBackgroundResource(R.drawable.text_style_false);

                                txt_Mean1.setText("");
                                txt_Mean3.setText("");

                                var_rate = static_rate + 1;
                                txt_Answer_Rate.setText(String.valueOf(100 - Math.round(var_rate / cards.size() * 100)) + "%");
                                static_rate = var_rate;
                            }

                            else {

                                txt_Mean3.setBackgroundResource(R.drawable.text_style_false);

                                txt_Mean1.setText("");
                                txt_Mean2.setText("");

                                var_rate = static_rate + 1;
                                txt_Answer_Rate.setText(String.valueOf(100 - Math.round(var_rate / cards.size() * 100)) + "%");
                                static_rate = var_rate;
                            }

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
                        txt_Mean1.setEnabled(false);
                        txt_Mean2.setEnabled(false);
                        txt_Mean3.setEnabled(false);

                        if (count <= 0) {
                            //일단 4초 타이머 중단
                            timerTask_4.cancel();

                            //Voca_Count 값이 배열 크기와 동일하면 중단해야 한다.
                            if(Voca_Count + 1 == cards.size()) {
                                Voca_Count = 0; //초기화 후 리턴
                                //도전 페이지로 이동 후 스낵바로 정확도 결과 보여준다.

                                Snackbar snackbar = Snackbar.make(constraintLayout,"정확도 : " + txt_Answer_Rate.getText().toString(), 100000)
                                        .setAction("확인", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                onBackPressed();
                                            }
                                        });
                                View view = snackbar.getView();
                                TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                tv.setTextColor(ContextCompat.getColor(Challenge_QuizActivity.this, R.color.White));
                                //view.setBackgroundColor(ContextCompat.getColor(Challenge_QuizActivity.this, R.color.snack_Background_Success));
                                snackbar.show();
                                return;
                            }
                            //텍스트 변경 후 다시 10초 타이머 시작
                            Random random = new Random();

                            //단어는 순서대로 다음 단어
                            //뜻은 랜덤으로 섞은 후 하나는 정답 배치

                            Voca_Count++;
                            constraintLayout.setBackgroundColor(Integer_Colors[random.nextInt(8)]);
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

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    public void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerTask_10.cancel();
        timerTask_4.cancel();
        Voca_Count = 0;
    }
}
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;

public class Challenge_SpellingActivity extends AppCompatActivity {

    //Intent
    String VocaNoteName, ChapterName, OrderBy;
    Boolean Shuffle;

    Retrofit retrofit;
    POSTApi postApi;
    final String svcName = "Service_VocaNote.svc/";
    final String TAG = "Chall_SpellingActivity";

    int POST_Response;

    List<VocaCard> cards;

    TextView txt_Answer_Rate, txt_Mean, txt_hint;
    EditText Edit_Insert_Voca;
    Button btnCancel, btnPass, btnNext;

    //단어 카운트
    //int count = 10;

    //단어 카운트 (베열 순서대로)
    static int Voca_Count = 0;

    //파스텔 톤
    Integer[] Integer_Colors;

    //최상위 레이아웃
    ConstraintLayout constraintLayout;

    static double static_rate;
    double var_rate = 0; //정답률 카운트

    //타이머
    static TimerTask timerTask;

    //타이머 카운트
    int count = 4;

    Random random;

    //배열 위치 카운트
    TextView txt_list_length;

    //TTS
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge__spelling);

        random = new Random();

        constraintLayout = findViewById(R.id.constraintLayout);

        static_rate = 0;

        Intent intent = getIntent();
        VocaNoteName = intent.getExtras().getString("VocaNoteName");
        ChapterName = intent.getExtras().getString("ChapterName");
        Shuffle = intent.getExtras().getBoolean("Shuffle");

        if (Shuffle) OrderBy = "NEWID() desc";
        else OrderBy = "CreateDate desc";

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);

        //Retrofit 을 이용하여 단어 뜻 가져옴
        cards = new ArrayList<>();

        Get_Voca_Mean();

        txt_Answer_Rate = (TextView) findViewById(R.id.txt_Answer_Rate); //정답률
        txt_Mean = (TextView) findViewById(R.id.txt_Mean); //뜻 맞추기
        txt_hint = (TextView) findViewById(R.id.txt_hint); //정답 힌트 (오답 시 보여줌)

        txt_list_length = (TextView) findViewById(R.id.txt_list_length);

        Edit_Insert_Voca = (EditText) findViewById(R.id.Edit_Insert_Voca); // 단어 스펠링 입력

        btnCancel = (Button) findViewById(R.id.btnCancel); //나가기
        btnPass = (Button) findViewById(R.id.btnPass); //모름 넘기기
        btnNext = (Button) findViewById(R.id.btnNext); //다음 넘어가기

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

        tts = new TextToSpeech(Challenge_SpellingActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.ENGLISH);
            }
        });

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

        txt_Mean.setText(cards.get(Voca_Count).getMean());

        txt_list_length.setText("(" + String.valueOf(Voca_Count + 1) + "/" + cards.size() + ")");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다음
                //정답이 맞는지 확인

                if (view.getId() == R.id.btnNext || view.getId() == R.id.btnPass) {
                    if (!Edit_Insert_Voca.getText().toString().contains(cards.get(Voca_Count).getVoca())) {
                        //단어 스펠링이 존재하지 않음.

                        //정확도 감소
                        var_rate = static_rate + 1;
                        txt_Answer_Rate.setText(String.valueOf(100 - Math.round(var_rate / cards.size() * 100)) + "%");
                        static_rate = var_rate;

                        //4초간 정답 보여줌
                        txt_hint.setText(cards.get(Voca_Count).getVoca());

                        //4초간 멈췄다가 다음 페이지로 이동
                        //4초 타이머 돌리는 동안 버튼 비활성화
                        btnPass.setEnabled(false);
                        btnNext.setEnabled(false);

                        timerTask = NewTimerTask();

                        //Timer 생성
                        final Timer timer = new Timer();
                        timer.schedule(timerTask, 0, 1000);


                        Toasty.error(Challenge_SpellingActivity.this, "오답..", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.success(Challenge_SpellingActivity.this, "정답!!", Toast.LENGTH_SHORT, true).show();
                        Next_Voca();
                    }

                    //TTS
                    String TTS_Text = cards.get(Voca_Count).getVoca();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        ttsGreater21(TTS_Text);
                    else ttsUnder20(TTS_Text);

                } else if (view.getId() == R.id.btnCancel) {

                    //나가기 리턴
                    onBackPressed();
                    return;
                }
            }
        };

        btnPass.setOnClickListener(onClickListener);
        btnNext.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }

    public void Next_Voca() {
        Edit_Insert_Voca.setText("");
        //만약 마지막 단어가 아니라면
        if (!(Voca_Count + 1 == cards.size())) {
            //1. 배경색 변경
            constraintLayout.setBackgroundColor(Integer_Colors[random.nextInt(8)]);
            //2. static 단어 카운트 증가 후 배열 값 이동
            txt_Mean.setText(cards.get(++Voca_Count).getMean());
            txt_list_length.setText("(" + String.valueOf(Voca_Count + 1) + "/" + cards.size() + ")");
        } else { //마지막 단어에 도달

            txt_list_length.setText("(" + String.valueOf(Voca_Count + 1) + "/" + cards.size() + ")");
            Voca_Count = 0; //초기화 후 종료
            Snackbar snackbar = Snackbar.make(constraintLayout, "정확도 : " + txt_Answer_Rate.getText().toString(), 100000)
                    .setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });

            View view1 = snackbar.getView();
            TextView tv = (TextView) view1.findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(ContextCompat.getColor(Challenge_SpellingActivity.this, R.color.White));
            snackbar.show();

            btnCancel.setEnabled(false);
            btnNext.setEnabled(false);
            btnPass.setEnabled(false);
            return;
        }
    }
    //4초간 정답 보여준 후 다음 단어로 이동
    public TimerTask NewTimerTask() {
        TimerTask tempTask = new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "run: " + String.valueOf(count));

                //쓰레드 UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(count <= 0) {
                            //타이머 종료 후 값 변경
                            timerTask.cancel();

                            btnNext.setText("다음");
                            btnNext.setEnabled(true);
                            btnPass.setEnabled(true);

                            txt_hint.setText("");

                            count = 4;

                            //틀리든 맞든 다음 단어로 이동함.
                            Next_Voca();
                            return;
                        }

                        btnNext.setText(String.valueOf(count));
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
        timerTask.cancel();
        Voca_Count = 0;
    }
}
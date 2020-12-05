package com.example.android_voca;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Struct;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class VocaCardAdapter  extends PagerAdapter {

    private List<VocaCard> cards;
    private Context context;
    TextView Voca, Mean;
    TextView Sentence, Interpretation;
    Button Search_Voca;

    //tts 관련
    CardView cardView;
    Button btnTTS;

    TextToSpeech tts;

    //Challenge_VocaCardActivity.java
    RelativeLayout HiddenLayout_WebView;
    WebView webView;
    FloatingActionButton fab;


    final String TAG = "VocaCardAdapter";

    //체크박스
    CheckBox check_sentence, check_interpretation;

    public VocaCardAdapter(List<VocaCard> cards, Context context) {
        this.cards = cards;
        this.context = context;
    }

    @Override
    public int getCount() {
        //return 0;
        return cards.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        //return false;
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_activity_challenge_voca_card, container, false);


        Voca = view.findViewById(R.id.textView_Voca);
        Mean = view.findViewById(R.id.textView_Mean);
        Sentence = view.findViewById(R.id.textView_Sentence);
        Interpretation = view.findViewById(R.id.textView_Interpretation);

        Voca.setText(cards.get(position).getVoca());
        Mean.setText(cards.get(position).getMean());
        Sentence.setText(cards.get(position).getSentence());
        Interpretation.setText(cards.get(position).getInterpretation());

        //체크박스
        //check_sentence = findViewById(R.id.check_sentence);
        check_sentence = Challenge_VocaCardActivity.check_sentence;
        //check_interpretation = findViewById(R.id.check_interpretation);
        check_interpretation = Challenge_VocaCardActivity.check_interpretation;

        /*check_sentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_sentence.isChecked()) { //예문 체크됨
                    check_interpretation.setEnabled(true);//해석 활성화

                    //예문이 보이도록 하는 코드
                    Sentence.setVisibility(View.VISIBLE);
                } else {
                    check_interpretation.setEnabled(false);

                    //예문 안보이게
                    Sentence.setVisibility(View.INVISIBLE);

                }
            }
        });*/

        if(check_sentence.isChecked()) {
            check_interpretation.setEnabled(true);

            Sentence.setVisibility(View.VISIBLE);
        } else {
            Sentence.setVisibility(View.INVISIBLE);
        }

        if(check_interpretation.isChecked()) {
            Interpretation.setVisibility(View.VISIBLE);
        } else {
            Interpretation.setVisibility(View.INVISIBLE);
        }

        //tts
        cardView = view.findViewById(R.id.CardView);
        btnTTS = Challenge_VocaCardActivity.btnTTS;

        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.ENGLISH);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TTS_Text = cards.get(position).getVoca();
                String TTS_Text2 = cards.get(position).getMean();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ttsGreater21(TTS_Text,TTS_Text2);
                else ttsUnder20(TTS_Text,TTS_Text2);
            }
        };

        cardView.setOnClickListener(onClickListener);
        //btnTTS.setOnClickListener(onClickListener); //이 버튼 나중에 사용

        /*cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TTS_Text = cards.get(position).getVoca();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ttsGreater21(TTS_Text);
                else ttsUnder20(TTS_Text);
            }
        });*/


        //Challenge_VocaCardActivity.java
        HiddenLayout_WebView = Challenge_VocaCardActivity.HiddenLayout_WebView;
        webView = Challenge_VocaCardActivity.webView;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient()); //새 창 띄우기 방지


        //네이버 검색
        Search_Voca = view.findViewById(R.id.Search_Voca);
        Search_Voca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //webview 검색된 결과를 보여준다
                HiddenLayout_WebView.setVisibility(View.VISIBLE);
                //webView.getSettings().setLoadsImagesAutomatically(true);
                //webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                webView.loadUrl("https://en.dict.naver.com/#/search?query=" + cards.get(position).getVoca() );

            }
        });

        fab = Challenge_VocaCardActivity.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HiddenLayout_WebView.setVisibility(View.GONE);
                webView.loadUrl(null);
            }
        });

        container.addView(view, 0);

        return view;
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text, String text2) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                tts.setLanguage(Locale.ENGLISH);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
            } else {
                tts.setLanguage(Locale.KOREAN);
                tts.speak(text2, TextToSpeech.QUEUE_ADD, map);
            }
            tts.playSilence(750,TextToSpeech.QUEUE_ADD,map);
        }
        //tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    public void ttsGreater21(String text, String text2) {
        String utteranceId = this.hashCode() + "";

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                tts.setLanguage(Locale.ENGLISH);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else {
                tts.setLanguage(Locale.KOREAN);
                tts.speak(text2, TextToSpeech.QUEUE_ADD, null, utteranceId);
            }
            tts.playSilence(750, TextToSpeech.QUEUE_ADD, null);
        }
        //tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

}

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

        Voca.setText(cards.get(position).getVoca());
        Mean.setText(cards.get(position).getMean());

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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ttsGreater21(TTS_Text);
                else ttsUnder20(TTS_Text);
            }
        };

        cardView.setOnClickListener(onClickListener);
        btnTTS.setOnClickListener(onClickListener);

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
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    public void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

}

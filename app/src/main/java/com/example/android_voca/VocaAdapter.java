package com.example.android_voca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VocaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnVocaNoteItemClickListener {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Voca> items;
    private Context context;

    OnVocaNoteItemClickListener listener; //뷰 클릭시 여부

    static Boolean Edit_Activation;

    //단어 검색 webView -activity_voca.xml
    public static RelativeLayout HiddenLayout_WebView;
    public static WebView webView;

    public static FloatingActionButton fabClose;

    Button Search_Voca; //네이버 버튼

    public void addItem(Voca item) {
        items.add(item);
    }

    public void setItems(ArrayList<Voca> items) {
        this.items = items;
    }

    public Voca getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Voca item) {
        items.set(position, item);
    }

    public VocaAdapter(Context context, List<Voca> itemModels) {
        this.items = itemModels;
        this.context = context;
    }

    // cardView Voca 적용 //
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.cardview_voca, parent, false);
            Search_Voca = itemView.findViewById(R.id.Search_Voca);
            return new ViewHolder(itemView, this);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    // 클릭 리스너 //
    public void setOnItemClickListener(OnVocaNoteItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position) {
        //여기선 사용하지 않음
    }

    @Override
    public void onItemVocaClick(ViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemVocaClick(holder, view, position); //클릭 포지션 값
        }
    }

    @Override
    public void onItemChapterClick(ChapterAdapter.ItemViewHolder holder, View view, int position) {

    }

    @Override
    public void onItemBoardClick(BoardAdapter.ItemViewHolder holder, View view, int position) {

    }

    @Override
    public void onItemGroupClick(GroupAdapter.GroupItemViewHolder holder, View view, int position) {

    }

    //값 반복적으로 바인딩
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder) {

            Voca item = items.get(position);
            initializeView(item,(ViewHolder) holder,position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        //return items.size();
        return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void initializeView(final Voca item, final VocaAdapter.ViewHolder holder, int position) {
        //바인딩할 data
        holder.Word.setText(item.getVoca());
        holder.Mean.setText(item.getMean());
        holder.Sentence.setText(item.getSentence());
        holder.Interpretation.setText(item.getInterpretation());

        // 예문 해석이 없을 때 안보이게 설정
        if(item.getSentence() == null) {
            holder.Sentence.setVisibility(View.GONE);
        } else {
            holder.Sentence.setVisibility(View.VISIBLE);
        }

        //단어 검색
        HiddenLayout_WebView = VocaActivity.HiddenLayout_WebView;
        webView = VocaActivity.webView;
        fabClose = VocaActivity.fabClose;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient()); //새 창 띄우기 방지

        //네이버 검색

        Search_Voca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //webview 검색된 결과를 보여준다
                HiddenLayout_WebView.setVisibility(View.VISIBLE);
                //webView.getSettings().setLoadsImagesAutomatically(true);
                //webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                webView.loadUrl("https://en.dict.naver.com/#/search?query=" + holder.Word.getText());

            }
        });

        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HiddenLayout_WebView.setVisibility(View.GONE);
                webView.loadUrl(null);
            }
        });


    }

    //체크박스 관련
    public List<Voca> getSelectedItem() {
        List<Voca> itemModelList = new ArrayList<>();

        int i;
        for (i = 0; i < items.size(); i++) {
            Voca item = items.get(i);

            if(item.isComplete()) {
                itemModelList.add(item);
            }
        }
        return itemModelList;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Word;
        TextView Mean;
        TextView Sentence;
        TextView Interpretation;

        CardView Main_CardView;

        public ViewHolder(View itemView, final OnVocaNoteItemClickListener listener) {
            super(itemView);

            Word = itemView.findViewById(R.id.Word);
            Mean = itemView.findViewById(R.id.Mean);
            Sentence = itemView.findViewById(R.id.Sentence);
            Interpretation = itemView.findViewById(R.id.Interpretation);

            Main_CardView = itemView.findViewById(R.id.Main_CardView_Voca);

            final ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) Main_CardView.getLayoutParams();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemVocaClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Voca item) {
            Word.setText(item.getVoca());
            Mean.setText(item.getMean());
            Sentence.setText(item.getSentence());
            Interpretation.setText(item.getInterpretation());
        }
    }

    //로딩

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}

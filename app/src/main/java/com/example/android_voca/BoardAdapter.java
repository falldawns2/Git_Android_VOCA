package com.example.android_voca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnVocaNoteItemClickListener{

    private final int VIEW_TYPE_ITEM = 0; //게시판
    private final int VIEW_TYPE_LOADING = 1; //로딩

    private List<Board> items;
    private Fragment fragment;

    private Context context;

    OnVocaNoteItemClickListener listener; //뷰 클릭시 여부

    public void addItem(Board item) { items.add(item);}
    public void setItems(ArrayList<Board> items) {this.items = items;}
    public Board getItem(int position) {return items.get(position);}
    public void setItem(int position, Board item) {items.set(position, item);}

    public BoardAdapter(Fragment fragment, List<Board> itemModels) {
        this.items = itemModels;
        this.fragment = fragment;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //return null;
        //초기 뷰 홀더 지정
        //로딩 or 게시판 홀더
        if(viewType == VIEW_TYPE_ITEM) { //게시판 홀더
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.cardview_board, parent, false);

            return new ItemViewHolder(itemView, this);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        //반복할 홀더 지정 (DTO 클래스)

        if(holder instanceof ItemViewHolder) { //게시판 관련 뷰 홀더
            Board item = items.get(position);

            initializeViews(item, (ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        //return 0;
        return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) { //홀더 선택
        //return super.getItemViewType(position);
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM; //로딩, 게시판
    }

    public void setOnItemClickListener(OnVocaNoteItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position) {
        //단어장
    }

    @Override
    public void onItemChapterClick(ChapterAdapter.ItemViewHolder holder, View view, int position) {
        //챕터
    }

    @Override
    public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position) {
        //단어
    }

    @Override
    public void onItemBoardClick(BoardAdapter.ItemViewHolder holder, View view, int position) {
        //게시판
        if(listener != null) {
            listener.onItemBoardClick(holder, view, position);
        }
    }

    @Override
    public void onItemGroupClick(GroupAdapter.GroupItemViewHolder holder, View view, int position) {

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        //xml 객체 초기화 (카드 뷰)
        ImageView imageView_ProfileImage;
        TextView textView_Nickname, textView_Uploadtime, textView_Hits, textView_Titlt_Comments;


        public ItemViewHolder(View itemView, final OnVocaNoteItemClickListener listener) {
            super(itemView);

            //프로필 이미지
            imageView_ProfileImage = itemView.findViewById(R.id.imageView_ProfileImage);
            //닉네임
            textView_Nickname = itemView.findViewById(R.id.textView_Nickname);
            //글 업로드 날짜 (변환 필요 - ex: 5일 전)
            textView_Uploadtime = itemView.findViewById(R.id.textView_Uploadtime);
            //조회수
            textView_Hits = itemView.findViewById(R.id.textView_Hits);
            //글 제목 + 댓글 수
            textView_Titlt_Comments = itemView.findViewById(R.id.textView_Title_Comments);

            //아이템뷰 클릭
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null) { //클릭 위치 넘김
                        listener.onItemBoardClick(ItemViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Board item) {

            Glide.with(MainActivity.context_main)
                    .load("http://192.168.0.2/WCF_Android/ProfileImage/" + item.getProfileimage())
                    .override(150,150)
                    .into(imageView_ProfileImage);

            textView_Titlt_Comments.setText(item.getTitle() + " " + "[" + Integer.toString(item.getComments()) + "]");
            textView_Hits.setText(Integer.toString(item.getHits()));
            textView_Uploadtime.setText(item.getUploadtime()); //변환 필요
            textView_Nickname.setText(item.getNickname());

        }
    }

    //로딩 홀더
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void initializeViews(final Board item, final BoardAdapter.ItemViewHolder holder, int position) {
        //뷰 홀더 클래스에서 정의 및 초기화한 객체 설계

        Glide.with(MainActivity.context_main)
                .load("http://192.168.0.2/WCF_Android/ProfileImage/" + item.getProfileimage())
                .override(150,150)
                .into(holder.imageView_ProfileImage);

        holder.textView_Nickname.setText(item.getNickname());

        //시간은 오늘 날짜 기준으로 계산하여 몇일 전으로 표시하도록 한다.
        //일단 json 으로 가져온 시간은 2020-10-10 오후 8:27:25
        //substring 으로 2020-10-10을 담는다.

        //현재 날짜
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        //cal.setTime(today);

        //뒤의 시간을 잘라낸다.
        String a = item.getUploadtime();
        a = a.substring(0, a.indexOf(" "));

        //잘라낸 2020-10-10 값을 - 기준으로 잘라 배열에 담는다.
        String[] b = new String[3];
        b = a.split("-");

        //각각의 변수에 초기화 시켜준다.
        String year,month,day;
        year = b[0];
        month = b[1];
        day = b[2];

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Integer.parseInt(year),Integer.parseInt(month) - 1,Integer.parseInt(day)); //month  : 0 부터 시작

        long sec = (cal.getTimeInMillis() - cal2.getTimeInMillis()) / 1000; //밀리초 변환
        long GetDay = sec / (60 * 60 * 24); //몇일 전

        holder.textView_Uploadtime.setText(GetDay + "일 전"); //item.getUploadtime()
        holder.textView_Hits.setText("조회수[" + Integer.toString(item.getHits()) + "]");
        holder.textView_Titlt_Comments.setText(item.getTitle() + " " + "[" + Integer.toString(item.getComments()) + "]");
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //로딩 뷰 관련 설계
    }
}

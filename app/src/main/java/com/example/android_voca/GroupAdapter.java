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
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnVocaNoteItemClickListener {

    private final int VIEW_TYPE_ITEM = 0; //그룹
    private final int VIEW_TYPE_LOADING = 1; //로딩

    private List<Group> items;
    private Fragment fragment;

    private Context context;

    OnVocaNoteItemClickListener listener; //뷰 클릭시 여부

    public void addItem(Group item) { items.add(item);}
    public void setItems(ArrayList<Group> items) {this.items = items;}
    public Group getItem(int position) {return items.get(position);}
    public void setItem(int position, Group item) {items.set(position, item);}

    public GroupAdapter(Fragment fragment, List<Group> itemModels) {
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
        if(viewType == VIEW_TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_group,parent,false);

            return new GroupItemViewHolder(itemView, this);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        //반복할 홀더 지정 (DTO 클래스)

        if(holder instanceof GroupItemViewHolder) {
            Group item = items.get(position);

            initializeViews(item, (GroupItemViewHolder) holder, position);
        } else if (holder instanceof  LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        //return 0;
        return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnItemClickListener(OnVocaNoteItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position) {

    }

    @Override
    public void onItemChapterClick(ChapterAdapter.ItemViewHolder holder, View view, int position) {

    }

    @Override
    public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position) {

    }

    @Override
    public void onItemBoardClick(BoardAdapter.ItemViewHolder holder, View view, int position) {

    }

    @Override
    public void onItemGroupClick(GroupAdapter.GroupItemViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemGroupClick(holder, view, position);
        }
    }

    public class GroupItemViewHolder extends RecyclerView.ViewHolder {

        //xml 객체 초기화 (카드 뷰)
        ImageView imageView_GroupImage;
        TextView textView_GroupName;

        public GroupItemViewHolder(View itemView, final OnVocaNoteItemClickListener listener) {
            super(itemView);

            //그룹 이미지
            imageView_GroupImage = itemView.findViewById(R.id.imageView_GroupImage);
            //그룹 명
            textView_GroupName = itemView.findViewById(R.id.textView_GroupName);

            //아이템 뷰 클릭
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null) {
                        listener.onItemGroupClick(GroupItemViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Group item) {

            Glide.with(MainActivity.context_main)
                    .load("http://121.131.90.130/IIS_ASP_NET/groupImage/" + item.getGroupImage())

                    .into(imageView_GroupImage);

            textView_GroupName.setText(item.getGroupName());
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

    private void initializeViews(final Group item, final GroupAdapter.GroupItemViewHolder holder, int position) {
        //뷰 홀더 클래스에서 정의 및 초기화한 객체 설계

        Glide.with(MainActivity.context_main)
                .load("http://121.131.90.130/IIS_ASP_NET/groupImage/" + item.getGroupImage())

                .into(holder.imageView_GroupImage);

        holder.textView_GroupName.setText(item.getGroupName());
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //
    }
}

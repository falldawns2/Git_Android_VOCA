package com.example.android_voca;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnVocaNoteItemClickListener { //VocaNoteAdapter.ViewHolder

    private final int VIEW_TYPE_ITEM = 0; //단어장
    private final int VIEW_TYPE_LOADING = 1; //로딩

    //ArrayList<Note> items = new ArrayList<>();
    private List<Chapter> items;
    private Fragment fragment;

    private Context context;

    OnVocaNoteItemClickListener listener; //뷰 클릭시 여부
    //static int a = 0;
    static Boolean Edit_Activation;


    public void addItem(Chapter item) {
        items.add(item);
    }

    public void setItems(ArrayList<Chapter> items) {
        this.items = items;
    }

    public Chapter getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Chapter item) {
        items.set(position,item);
    }


    public ChapterAdapter(Context context, List<Chapter> itemModels) {
        this.context = context;
        this.items = itemModels;
    }
    
    /*public VocaNoteAdapter(Context context, List<VocaNote> itemModels) {
        this.items = itemModels;
        this.context = context;
    }*/

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) { //단어장 카드뷰
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.cardview_vocanote,parent,false);

            return new ItemViewHolder(itemView, this);
        } else { //로딩 카드뷰
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_loading,parent,false);
            return new LoadingViewHolder(view);
        }

        /*ViewHolder viewHolder = new ViewHolder(itemView,this);
        viewHolder.main_checkbox.setVisibility(View.VISIBLE);
        viewHolder.main_checkbox.setChecked(true);
        return  viewHolder;*/
    }

    public void setOnItemClickListener(OnVocaNoteItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position) {
        //단어장
    }

    @Override
    public void onItemChapterClick(ItemViewHolder holder, View view, int position) {

        ///챕터
        if(listener != null) {
            listener.onItemChapterClick(holder, view, position);
        }
    }

    @Override
    public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position) {
        //단어
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //ViewHolder

        if(holder instanceof ItemViewHolder) { //단어장 관련 뷰홀더
            Chapter item = items.get(position);
            /*if(MainActivity.tag == "single")
                holder.setItem(item);
                else if (MainActivity.tag == "multi")
                initializeViews(item,holder,position);*/
            initializeViews(item,(ItemViewHolder) holder,position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }



    }

    @Override
    public int getItemCount() {
        //return items.size();
        return items == null ? 0 : items.size();
    }

    //새로 추가한거


    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM; //로딩, 단어장
    }

    private void initializeViews(final Chapter item, final ChapterAdapter.ItemViewHolder holder, int position) {

        holder.textview_VocaNote.setText(item.getChapterName()); //챕터명 뿌림 //챕터 페이지

        //holder.textView_CreateDate.setText(item.getCreateDate());

        holder.textview_VocaCount.setText(String.valueOf(item.getVocaCount()));

        holder.main_checkbox.setChecked(item.isSelected());
        holder.main_checkbox.setTag(position);

        if(MainActivity.tag == "multi") {
            /*holder.main_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox) view;
                    int ClickedPosition = (Integer) checkBox.getTag();
                    items.get(ClickedPosition).setSelected(checkBox.isChecked());
                    //if(listener != null)
                        //listener.onItemClick(holder,checkBox, position);
                    notifyDataSetChanged();
                }
            });*/

            holder.main_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(holder.main_checkbox.isChecked()) {
                        int ClickedPosition = (Integer) holder.main_checkbox.getTag();
                        items.get(ClickedPosition).setSelected(holder.main_checkbox.isChecked());
                        //notifyDataSetChanged();
                    } else {
                        int ClickedPosition = (Integer) holder.main_checkbox.getTag();
                        items.get(ClickedPosition).setSelected(holder.main_checkbox.isChecked());

                        //notifyDataSetChanged();
                    }
                }
            });

        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //로딩 뷰 관련 디자인 구성
    }

    public List<Chapter> getSelectedItem() {
        List<Chapter> itemModelList = new ArrayList<>();
        int i;
        for (i = 0; i< items.size(); i++) {
            Chapter item = items.get(i);

            if(item.isSelected()) {
                itemModelList.add(item);
            }
        }
        return itemModelList;
    }

     public class ItemViewHolder extends RecyclerView.ViewHolder {//static , private 없음
        TextView textview_VocaNote;
        //TextView textView_CreateDate;
        TextView textview_VocaCount;

        CheckBox main_checkbox;
        CardView Main_CardView;

        public ItemViewHolder(View itemView, final OnVocaNoteItemClickListener listener) {
            super(itemView);

            textview_VocaNote = itemView.findViewById(R.id.textview_VocaNote);
            //textView_CreateDate = itemView.findViewById(R.id.CreateDate);
            textview_VocaCount = itemView.findViewById(R.id.textview_VocaCount);

            //frag_main_item.xml
            //checkbox checked, card view margins
            main_checkbox = itemView.findViewById(R.id.main_checkbox);

            Main_CardView = itemView.findViewById(R.id.Main_CardView);
            final ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) Main_CardView.getLayoutParams();



            if(MainActivity.tag == "single") {

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();

                        if(listener != null) {
                            listener.onItemChapterClick(ItemViewHolder.this, view, position);

                            if(main_checkbox.isChecked()) {
                                main_checkbox.setChecked(false);
                            }
                            else {
                                main_checkbox.setChecked(true);
                            }
                        }
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(main_checkbox.getVisibility() == View.INVISIBLE) {
                            MainActivity.tag = "multi";
                            main_checkbox.setChecked(true);
                            if (MainActivity.PageNum == 0) {
                                ((MainActivity)MainActivity.context_main).restart();
                            } else if (MainActivity.PageNum == 1) {
                                ((ChapterActivity)ChapterActivity.context_Chapter).restart();
                            }
                            //Edit_Activation = true;
                        }
                        return true;
                    }
                });
            } else { //MainActivity.tag == multi
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();

                        if(listener != null) {
                            //listener.onItemClick(ViewHolder.this, view, position);

                            if(main_checkbox.isChecked()) {
                                main_checkbox.setChecked(false);
                            }
                            else {
                                main_checkbox.setChecked(true);
                            }
                        }
                    }
                });
            }

            ValueAnimator valueAnimator = ValueAnimator.ofInt(25);
            valueAnimator.setDuration(400);
            Edit_Activation = ((MainActivity)MainActivity.context_main).Edit_Activation;
            //Edit_Activation = true;
            if(Edit_Activation) {
                if(!main_checkbox.isChecked()) {
                    main_checkbox.setVisibility(View.VISIBLE);
                    //main_checkbox.setChecked(true);

                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            layoutParams.setMargins(0,
                                    0,
                                    (Integer) valueAnimator.getAnimatedValue(),
                                    (Integer) valueAnimator.getAnimatedValue());
                            Main_CardView.requestLayout();
                        }
                    });
                    valueAnimator.start();
                }
            } else {
                main_checkbox.setVisibility(View.INVISIBLE);
            }

        }

        public void setItem(Chapter item) {
            textview_VocaNote.setText(item.getChapterName());
            //textView_CreateDate.setText(item.getCreateDate());
            textview_VocaCount.setText(item.getVocaCount());
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

}
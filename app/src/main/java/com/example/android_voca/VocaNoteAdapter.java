package com.example.android_voca;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VocaNoteAdapter extends RecyclerView.Adapter<VocaNoteAdapter.ViewHolder> implements OnVocaNoteItemClickListener {

    //ArrayList<Note> items = new ArrayList<>();
    private List<VocaNote> items;
    private Fragment fragment;

    OnVocaNoteItemClickListener listener; //뷰 클릭시 여부
    //static int a = 0;
    static Boolean Edit_Activation;


    public void addItem(VocaNote item) {
        items.add(item);
    }

    public void setItems(ArrayList<VocaNote> items) {
        this.items = items;
    }

    public VocaNote getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, VocaNote item) {
        items.set(position,item);
    }

    public VocaNoteAdapter(Fragment fragment, List<VocaNote> itemModels) {
        this.items = itemModels;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_vocanote,parent,false);
        /*ViewHolder viewHolder = new ViewHolder(itemView,this);
        viewHolder.main_checkbox.setVisibility(View.VISIBLE);
        viewHolder.main_checkbox.setChecked(true);
        return  viewHolder;*/

        return new ViewHolder(itemView, this);
    }

    public void setOnItemClickListener(OnVocaNoteItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VocaNote item = items.get(position);
        /*if(MainActivity.tag == "single")
            holder.setItem(item);
        else if (MainActivity.tag == "multi")
            initializeViews(item,holder,position);*/
        initializeViews(item,holder,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void initializeViews(final VocaNote item, final VocaNoteAdapter.ViewHolder holder, int position) {

        if(MainActivity.PageNum == 0) {
            holder.textview_VocaNote.setText(item.getVocaNoteName()); //단어장명 뿌림 //단어장 페이지
        } else if (MainActivity.PageNum == 1) {
            holder.textview_VocaNote.setText(item.getChapterName()); //챕터명 뿌림 //챕터 페이지
        }

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

    public List<VocaNote> getSelectedItem() {
        List<VocaNote> itemModelList = new ArrayList<>();
        int i;
        for (i = 0; i< items.size(); i++) {
            VocaNote item = items.get(i);

            if(item.isSelected()) {
                itemModelList.add(item);
            }
        }
        return itemModelList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview_VocaNote;
        //TextView textView_CreateDate;
        TextView textview_VocaCount;

        CheckBox main_checkbox;
        CardView Main_CardView;

        public ViewHolder(View itemView, final OnVocaNoteItemClickListener listener) {
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
                            listener.onItemClick(ViewHolder.this, view, position);

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

        public void setItem(VocaNote item) {
            textview_VocaNote.setText(item.getVocaNoteName());
            //textView_CreateDate.setText(item.getCreateDate());
            textview_VocaCount.setText(item.getVocaCount());
        }
    }
}

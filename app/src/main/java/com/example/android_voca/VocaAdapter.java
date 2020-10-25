package com.example.android_voca;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VocaAdapter extends RecyclerView.Adapter<VocaAdapter.ViewHolder> implements OnVocaNoteItemClickListener {

    private List<Voca> items;
    private Context context;

    OnVocaNoteItemClickListener listener; //뷰 클릭시 여부
    static boolean Edit_Activation;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_voca, parent, false);

        return new ViewHolder(itemView, this);
    }

    public void setOnItemClickListener(OnVocaNoteItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(VocaNoteAdapter.ViewHolder holder, View view, int position) {

    }

    @Override
    public void onItemVocaClick(ViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemVocaClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Voca item = items.get(position);

        initializeView(item,holder,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void initializeView(final Voca item, final VocaAdapter.ViewHolder holder, int position) {


    }

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
    }

    public void setItem(Voca item) {

    }
}

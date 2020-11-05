package com.example.android_voca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class VocaCardAdapter  extends PagerAdapter {

    private List<VocaCard> cards;
    private Context context;

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

        TextView Voca, Mean;

        Voca = view.findViewById(R.id.textView_Voca);
        Mean = view.findViewById(R.id.textView_Mean);

        Voca.setText(cards.get(position).getVoca());
        Mean.setText(cards.get(position).getMean());



        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }
}

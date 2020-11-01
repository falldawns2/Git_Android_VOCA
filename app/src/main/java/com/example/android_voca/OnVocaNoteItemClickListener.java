package com.example.android_voca;

import android.view.View;

public interface OnVocaNoteItemClickListener {
    //position 값 아이템 구분 인덱스
    public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position);

    public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position);
}

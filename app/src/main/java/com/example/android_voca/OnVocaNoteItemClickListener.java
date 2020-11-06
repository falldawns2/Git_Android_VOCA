package com.example.android_voca;

import android.view.View;

public interface OnVocaNoteItemClickListener {
    //position 값 아이템 구분 인덱스

    //단어장
    public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position);

    //챕터
    public void onItemChapterClick(ChapterAdapter.ItemViewHolder holder, View view, int position);

    //단어
    public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position);

    //게시판
    public void onItemBoardClick(BoardAdapter.ItemViewHolder holder, View view, int position);
}

package com.example.android_voca;

import android.view.View;

public interface OnVocaNoteItemClickListener {
    //position 값 아이템 구분 인덱스

    //단어장 (Fragment)
    public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position);

    //챕터 (Activity)
    public void onItemChapterClick(ChapterAdapter.ItemViewHolder holder, View view, int position);

    //단어 (Activity)
    public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position);

    //게시판 (Fragment)
    public void onItemBoardClick(BoardAdapter.ItemViewHolder holder, View view, int position);

    //그룹 (Fragment)
    public void onItemGroupClick(GroupAdapter.GroupItemViewHolder holder, View view, int position);
}

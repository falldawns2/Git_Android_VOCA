package com.example.android_voca;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_VocaNote extends Fragment {

    RecyclerView recyclerView;
    VocaNoteAdapter adapter;
    //Note_MultiSelectionAdapter multi_adapter;

    ///
    public static Fragment_VocaNote context_Frag_Main;
    public String Title;
    public String CreateDate;
    public String Memo;
    //

    static boolean checked= false;

    ///
    public static String[] Arr_ID = {};
    public static String[] Arr_TITLE = {};
    public static String[] Arr_CREATE_DATE = {};
    public static String[] Arr_CONTENTS_MEMO = {};
    public static String[] Arr_isFAVORITE = {};
    public static String[] Arr_WHICH_FOLDER = {};
    ///


    // 카드뷰 내용 : 단어장 명 , 총 단어 수
    public static String[] Arr_VocaNoteName = {};
    public static int[] Arr_VocaCount = {};

    // 카드 뷰 내용 //
    public String VocaNoteName;
    public String VocaCount;

    //public  String[] title;

    public  int i = 0;
    //어댑터 사이즈

    int adapter_size = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context_Frag_Main = Fragment_VocaNote.this;
        final View v = inflater.inflate(R.layout.fragment_vocanote, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        //QUERY_NOTE();
        // 샘플로 예시 단어장 넣어본다 //
        QUERY_VocaNote();

        List<VocaNote> list = getList();



        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.context_main);
        recyclerView.setLayoutManager(layoutManager);

        // 싱글 선택 어댑터 //
        adapter = new VocaNoteAdapter(this, list);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnVocaNoteItemClickListener() {
            @Override
            public void onItemClick(VocaNoteAdapter.ViewHolder holder, View view, int position) {

                MainActivity.PageNum = 1; //챕터 페이지로 이동
                VocaNote item = adapter.getItem(position);
                VocaNoteName = item.getVocaNoteName();
                VocaCount = "총 단어 수 :" + String.valueOf(item.getVocaCount());

                Toast.makeText(getContext(), "선택된 단어장 : " + item.getVocaNoteName() + ", " + item.getVocaCount(), Toast.LENGTH_SHORT).show();


                ///ChapterActivity
                Intent intent = new Intent(getContext(), ChapterActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position) {

            }
        });

        return v;
    }

    public void QUERY_NOTE() {
        int i = 0;

        try {
            String uriString = "content://com.example.banananote/Note";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[] {"id","title","createDate","contentsMemo","isFavorite","whichFolder"};
            Cursor cursor = getContext().getContentResolver().query(uri, columns, null, null, "id ASC");
            Log.e("Fragment_Main.java","QUERY 결과 : " + cursor.getCount());
            int columns_count = cursor.getCount();

            int index = 1;
            Arr_ID = new String[columns_count];
            Arr_TITLE = new String[columns_count];
            Arr_CREATE_DATE = new String[columns_count];
            Arr_CONTENTS_MEMO = new String[columns_count];
            Arr_isFAVORITE = new String[columns_count];
            Arr_WHICH_FOLDER = new String[columns_count];

            while (cursor.moveToNext()) { //출력
                String id = cursor.getString(cursor.getColumnIndex(columns[0]));
                String title = cursor.getString(cursor.getColumnIndex(columns[1]));
                String createDate = cursor.getString(cursor.getColumnIndex(columns[2]));
                String contentsMemo = cursor.getString(cursor.getColumnIndex(columns[3]));
                String isFavorite = cursor.getString(cursor.getColumnIndex(columns[4]));
                String which_folder = cursor.getString(cursor.getColumnIndex(columns[5]));

                Arr_ID[i] = id;
                Arr_TITLE[i] = title;

                createDate = createDate.substring(0,11);
                Arr_CREATE_DATE[i] = createDate;

                Arr_CONTENTS_MEMO[i] = contentsMemo;
                Arr_isFAVORITE[i] = isFavorite;
                Arr_WHICH_FOLDER[i] = which_folder;

                Log.e("Fragment_Main.java", "레코드 " + index + " : " + id + ", " + title + ", " + createDate + ", " +
                        contentsMemo + ", " + isFavorite + ", " + which_folder );
                index += 1;
                i+=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void QUERY_VocaNote() {
        // 일단 임시로 넣는다 나중에 DB에서 연동 //

        Arr_VocaNoteName = new String[20];
        Arr_VocaCount = new int[20];

        Arr_VocaNoteName[19] = "단어장 1";
        Arr_VocaCount[19] = 5;

        Arr_VocaNoteName[18] = "단어장 2";
        Arr_VocaCount[18] = 58;

        Arr_VocaNoteName[17] = "단어장 3";
        Arr_VocaCount[17] = 1;

        Arr_VocaNoteName[16] = "단어장 4";
        Arr_VocaCount[16] = 42;

        Arr_VocaNoteName[15] = "단어장 5";
        Arr_VocaCount[15] = 25;

        Arr_VocaNoteName[14] = "단어장 6";
        Arr_VocaCount[14] = 5;

        Arr_VocaNoteName[13] = "단어장 7";
        Arr_VocaCount[13] = 58;

        Arr_VocaNoteName[12] = "단어장 8";
        Arr_VocaCount[12] = 1;

        Arr_VocaNoteName[11] = "단어장 9";
        Arr_VocaCount[11] = 42;

        Arr_VocaNoteName[10] = "단어장 10";
        Arr_VocaCount[10] = 25;

        Arr_VocaNoteName[9] = "단어장 11";
        Arr_VocaCount[9] = 5;

        Arr_VocaNoteName[8] = "단어장 12";
        Arr_VocaCount[8] = 58;

        Arr_VocaNoteName[7] = "단어장 13";
        Arr_VocaCount[7] = 1;

        Arr_VocaNoteName[6] = "단어장 14";
        Arr_VocaCount[6] = 42;

        Arr_VocaNoteName[5] = "단어장 15";
        Arr_VocaCount[5] = 25;

        Arr_VocaNoteName[4] = "단어장 16";
        Arr_VocaCount[4] = 5;

        Arr_VocaNoteName[3] = "단어장 17";
        Arr_VocaCount[3] = 58;

        Arr_VocaNoteName[2] = "단어장 18";
        Arr_VocaCount[2] = 1;

        Arr_VocaNoteName[1] = "단어장 19";
        Arr_VocaCount[1] = 42;

        Arr_VocaNoteName[0] = "단어장 20";
        Arr_VocaCount[0] = 25;
    }

    private List<VocaNote> getList() {
        List<VocaNote> list = new ArrayList<>();

        for(int i = 0; i< Arr_VocaNoteName.length; i++) {
            VocaNote model = new VocaNote();
            model.setVocaNoteName(Arr_VocaNoteName[i]); // 단어장 명 //
            //model.setCrDateNote(Arr_CREATE_DATE[i]);
            model.setVocaCount(Arr_VocaCount[i]); // 총 단어 수 //
            list.add(model);
        }

        return list;
    }

    public void selectedClick() {
        List list = adapter.getSelectedItem();

        if(list.size() > 0 ) {
            StringBuilder sb = new StringBuilder();
            for(int index = 0; index < list.size(); index++) {
                VocaNote model = (VocaNote) list.get(index);
                sb.append(model.getVocaNoteName()).append("\n");
            }
            showToast(sb.toString());

        } else {
            showToast("체크 안됨");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}

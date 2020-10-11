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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class Fragment_VocaNote extends Fragment {

    RecyclerView recyclerView;
    //NoteAdapter adapter;
    //Note_MultiSelectionAdapter multi_adapter;

    ///
    public static Fragment_VocaNote context_Frag_Main;
    public String Title;
    public String CreateDate;
    public String Memo;

    static boolean checked= false;

    ///
    public static String[] Arr_ID = {};
    public static String[] Arr_TITLE = {};
    public static String[] Arr_CREATE_DATE = {};
    public static String[] Arr_CONTENTS_MEMO = {};
    public static String[] Arr_isFAVORITE = {};
    public static String[] Arr_WHICH_FOLDER = {};

    //public  String[] title;

    public  int i = 0;
    //어댑터 사이즈

    int adapter_size = 0;

    String Arr_settitle[] = {
            "test1","test2","test3",
            "test4","test5","test6",
            "test7","test8","test9",
            "test10","test11","test12",
            "test13","test14","test15",
            "test16","test17","test18"
    };
    String Arr_setCreateDate[] = {
            "2020-07-21","2020-07-22","2020-07-23",
            "2020-07-24","2020-07-25","2020-07-26",
            "2020-07-27","2020-07-28","2020-07-29",
            "2020-07-30","2020-07-31","2020-07-32",
            "2020-07-33","2020-07-34","2020-07-35",
            "2020-07-36","2020-07-37","2020-07-38"
    };
    String Arr_setMemo[] = {
            "테스트 입니다1","테스트입니다2","테스트입니다3",
            "테스트 입니다4","테스트입니다5","테스트입니다6",
            "테스트 입니다7","테스트입니다8","테스트입니다9",
            "테스트 입니다10","테스트입니다11","테스트입니다12",
            "테스트 입니다13","테스트입니다14","테스트입니다15",
            "테스트 입니다16","테스트입니다17","테스트입니다18"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context_Frag_Main = Fragment_VocaNote.this;
        View v = inflater.inflate(R.layout.fragment_vocanote, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        //QUERY_NOTE();

        //List<Note> list = getList();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(layoutManager);

        //MainActivity.tag = "multi";

        /*        switch (MainActivity.tag) {
            case "single":
                adapter = new NoteAdapter(this,list);

                adapter.addItem(new Note("가나다라마바","2020-07-20","테스트 메모 1"));
                adapter.addItem(new Note("가나다라마바사아자차카타파하","2020-07-20","테스트 메모 2, 수능완성 유니티 게임만들기"));
                adapter.addItem(new Note("가나다라마","2020-07-20","테스트 메모 3, 가낟라ㅏ"));
                adapter.addItem(new Note("test4","2020-07-20","테스트 메모 4,ㅓㄻㄴ아렁ㅁ니러아ㅣㅁ러ㅣ어ㅏ"));
                adapter.addItem(new Note("test5","2020-07-20","테스트 메모 5걎ㄷ갣ㅈㄱㄷ쟈ㅐㄱㅈ뎌갲댝ㅈ뎌갸ㅐㅈㄷ겨ㅑㅐㅈㄷ겾ㄷ겾갲ㄷ겨ㅑㄷㅈ겨ㅐㅈ뎌갲"));
                adapter.addItem(new Note("test6","2020-07-20","테스트 메모 6 jrtkweltj tkwejktlejqwl ejkwlq tjkleqwj tklewjkqt ljqweklt jkwqltj eklwtjkqwtjklweqtjwekltjkwelqtjqwekltjql"));

                adapter.addItem(new Note("test1","2020-07-20","테스트 메모 1"));
                adapter.addItem(new Note("test2","2020-07-20","테스트 메모 2, 수능완성 유니티 게임만들기"));
                adapter.addItem(new Note("test3","2020-07-20","테스트 메모 3, 가낟라ㅏ"));
                adapter.addItem(new Note("test4","2020-07-20","테스트 메모 4,ㅓㄻㄴ아렁ㅁ니러아ㅣㅁ러ㅣ어ㅏ"));
                adapter.addItem(new Note("test5","2020-07-20","테스트 메모 5걎ㄷ갣ㅈㄱㄷ쟈ㅐㄱㅈ뎌갲댝ㅈ뎌갸ㅐㅈㄷ겨ㅑㅐㅈㄷ겾ㄷ겾갲ㄷ겨ㅑㄷㅈ겨ㅐㅈ뎌갲"));
                adapter.addItem(new Note("test6","2020-07-20","테스트 메모 6 jrtkweltj tkwejktlejqwl ejkwlq tjkleqwj tklewjkqt ljqweklt jkwqltj eklwtjkqwtjklweqtjwekltjkwelqtjqwekltjql"));

                adapter.addItem(new Note("test1","2020-07-20","테스트 메모 1"));
                adapter.addItem(new Note("test2","2020-07-20","테스트 메모 2, 수능완성 유니티 게임만들기"));
                adapter.addItem(new Note("test3","2020-07-20","테스트 메모 3, 가낟라ㅏ"));
                adapter.addItem(new Note("test4","2020-07-20","테스트 메모 4,ㅓㄻㄴ아렁ㅁ니러아ㅣㅁ러ㅣ어ㅏ"));
                adapter.addItem(new Note("test5","2020-07-20","테스트 메모 5걎ㄷ갣ㅈㄱㄷ쟈ㅐㄱㅈ뎌갲댝ㅈ뎌갸ㅐㅈㄷ겨ㅑㅐㅈㄷ겾ㄷ겾갲ㄷ겨ㅑㄷㅈ겨ㅐㅈ뎌갲"));
                adapter.addItem(new Note("test6","2020-07-20","테스트 메모 6 jrtkweltj tkwejktlejqwl ejkwlq tjkleqwj tklewjkqt ljqweklt jkwqltj eklwtjkqwtjklweqtjwekltjkwelqtjqwekltjql"));

                adapter.addItem(new Note("test1","2020-07-20","테스트 메모 1"));
                adapter.addItem(new Note("test2","2020-07-20","테스트 메모 2, 수능완성 유니티 게임만들기"));
                adapter.addItem(new Note("test3","2020-07-20","테스트 메모 3, 가낟라ㅏ"));
                adapter.addItem(new Note("test4","2020-07-20","테스트 메모 4,ㅓㄻㄴ아렁ㅁ니러아ㅣㅁ러ㅣ어ㅏ"));
                adapter.addItem(new Note("test5","2020-07-20","테스트 메모 5걎ㄷ갣ㅈㄱㄷ쟈ㅐㄱㅈ뎌갲댝ㅈ뎌갸ㅐㅈㄷ겨ㅑㅐㅈㄷ겾ㄷ겾갲ㄷ겨ㅑㄷㅈ겨ㅐㅈ뎌갲"));
                adapter.addItem(new Note("test6","2020-07-20","테스트 메모 6 jrtkweltj tkwejktlejqwl ejkwlq tjkleqwj tklewjkqt ljqweklt jkwqltj eklwtjkqwtjklweqtjwekltjkwelqtjqwekltjql"));


                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new OnNoteItemClickListener() {
                    @Override
                    public void onItemClick(NoteAdapter.ViewHolder holder, View view, int position) {
                        Note item = adapter.getItem(position);
                        Toast.makeText(getContext(), "아이템 선택됨: " + item.getTitle(), Toast.LENGTH_LONG).show();
                        //CheckBox checkBox;
                        //checkBox = view.findViewById(R.id.main_checkbox);
                        //if(!checkBox.isChecked()) checkBox.setVisibility(View.VISIBLE);


                        //checked = true;
                    }
                });

                break;
            case "multi":
                multi_adapter = new Note_MultiSelectionAdapter(this,list);

                multi_adapter.addItem(new Note("가나다라마바","2020-07-20","테스트 메모 1"));
                multi_adapter.addItem(new Note("가나다라마바사아자차카타파하","2020-07-20","테스트 메모 2, 수능완성 유니티 게임만들기"));
                multi_adapter.addItem(new Note("가나다라마","2020-07-20","테스트 메모 3, 가낟라ㅏ"));
                multi_adapter.addItem(new Note("test4","2020-07-20","테스트 메모 4,ㅓㄻㄴ아렁ㅁ니러아ㅣㅁ러ㅣ어ㅏ"));
                multi_adapter.addItem(new Note("test5","2020-07-20","테스트 메모 5걎ㄷ갣ㅈㄱㄷ쟈ㅐㄱㅈ뎌갲댝ㅈ뎌갸ㅐㅈㄷ겨ㅑㅐㅈㄷ겾ㄷ겾갲ㄷ겨ㅑㄷㅈ겨ㅐㅈ뎌갲"));
                multi_adapter.addItem(new Note("test6","2020-07-20","테스트 메모 6 jrtkweltj tkwejktlejqwl ejkwlq tjkleqwj tklewjkqt ljqweklt jkwqltj eklwtjkqwtjklweqtjwekltjkwelqtjqwekltjql"));

                multi_adapter.addItem(new Note("test1","2020-07-20","테스트 메모 1"));
                multi_adapter.addItem(new Note("test2","2020-07-20","테스트 메모 2, 수능완성 유니티 게임만들기"));
                multi_adapter.addItem(new Note("test3","2020-07-20","테스트 메모 3, 가낟라ㅏ"));
                multi_adapter.addItem(new Note("test4","2020-07-20","테스트 메모 4,ㅓㄻㄴ아렁ㅁ니러아ㅣㅁ러ㅣ어ㅏ"));
                multi_adapter.addItem(new Note("test5","2020-07-20","테스트 메모 5걎ㄷ갣ㅈㄱㄷ쟈ㅐㄱㅈ뎌갲댝ㅈ뎌갸ㅐㅈㄷ겨ㅑㅐㅈㄷ겾ㄷ겾갲ㄷ겨ㅑㄷㅈ겨ㅐㅈ뎌갲"));
                multi_adapter.addItem(new Note("test6","2020-07-20","테스트 메모 6 jrtkweltj tkwejktlejqwl ejkwlq tjkleqwj tklewjkqt ljqweklt jkwqltj eklwtjkqwtjklweqtjwekltjkwelqtjqwekltjql"));

                multi_adapter.addItem(new Note("test1","2020-07-20","테스트 메모 1"));
                multi_adapter.addItem(new Note("test2","2020-07-20","테스트 메모 2, 수능완성 유니티 게임만들기"));
                multi_adapter.addItem(new Note("test3","2020-07-20","테스트 메모 3, 가낟라ㅏ"));
                multi_adapter.addItem(new Note("test4","2020-07-20","테스트 메모 4,ㅓㄻㄴ아렁ㅁ니러아ㅣㅁ러ㅣ어ㅏ"));
                multi_adapter.addItem(new Note("test5","2020-07-20","테스트 메모 5걎ㄷ갣ㅈㄱㄷ쟈ㅐㄱㅈ뎌갲댝ㅈ뎌갸ㅐㅈㄷ겨ㅑㅐㅈㄷ겾ㄷ겾갲ㄷ겨ㅑㄷㅈ겨ㅐㅈ뎌갲"));
                multi_adapter.addItem(new Note("test6","2020-07-20","테스트 메모 6 jrtkweltj tkwejktlejqwl ejkwlq tjkleqwj tklewjkqt ljqweklt jkwqltj eklwtjkqwtjklweqtjwekltjkwelqtjqwekltjql"));

                multi_adapter.addItem(new Note("test1","2020-07-20","테스트 메모 1"));
                multi_adapter.addItem(new Note("test2","2020-07-20","테스트 메모 2, 수능완성 유니티 게임만들기"));
                multi_adapter.addItem(new Note("test3","2020-07-20","테스트 메모 3, 가낟라ㅏ"));
                multi_adapter.addItem(new Note("test4","2020-07-20","테스트 메모 4,ㅓㄻㄴ아렁ㅁ니러아ㅣㅁ러ㅣ어ㅏ"));
                multi_adapter.addItem(new Note("test5","2020-07-20","테스트 메모 5걎ㄷ갣ㅈㄱㄷ쟈ㅐㄱㅈ뎌갲댝ㅈ뎌갸ㅐㅈㄷ겨ㅑㅐㅈㄷ겾ㄷ겾갲ㄷ겨ㅑㄷㅈ겨ㅐㅈ뎌갲"));
                multi_adapter.addItem(new Note("test6","2020-07-20","테스트 메모 6 jrtkweltj tkwejktlejqwl ejkwlq tjkleqwj tklewjkqt ljqweklt jkwqltj eklwtjkqwtjklweqtjwekltjkwelqtjqwekltjql"));

                recyclerView.setAdapter(multi_adapter);

                break;
        }*/

        /*if(MainActivity.tag == "multi") {
            recyclerView.removeAllViewsInLayout();
            recyclerView.removeAllViews();
        }*/




        /*//싱글 선택 어댑터
        adapter = new NoteAdapter(this,list);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnNoteItemClickListener() {
            @Override
            public void onItemClick(NoteAdapter.ViewHolder holder, View view, int position) {
                Note item = adapter.getItem(position);
                Title = item.getTitle();
                CreateDate = item.getCreateDate();
                Memo = item.getMemo();
                Toast.makeText(getContext(), "아이템 선택됨: " + item.getTitle(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(),NoteShowActivity.class);
                startActivity(intent);


                //CheckBox checkBox;
                //checkBox = view.findViewById(R.id.main_checkbox);
                //if(!checkBox.isChecked()) checkBox.setVisibility(View.VISIBLE);


                //checked = true;
            }
        });*/




        //return inflater.inflate(R.layout.fragment_main, container, false);

        //checkbox , LongClick

        
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

    /*private List<Note> getList() {
        List<Note> list = new ArrayList<>();

        for(int i = 0; i< Arr_TITLE.length; i++) {
            Note model = new Note();
            model.setTitle(Arr_TITLE[i]);
            model.setCreateDate(Arr_CREATE_DATE[i]);
            model.setMemo(Arr_CONTENTS_MEMO[i]);
            list.add(model);
        }

        return list;
    }

    public void selectedClick() {

        List list = adapter.getSelectedItem();

        if(list.size() > 0 ) {
            StringBuilder sb = new StringBuilder();
            for(int index = 0; index < list.size(); index++) {
                Note model = (Note) list.get(index);
                sb.append(model.getTitle()).append("\n");
            }
            showToast(sb.toString());

        } else {
            showToast("체크 안됨");
        }
    }*/

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}

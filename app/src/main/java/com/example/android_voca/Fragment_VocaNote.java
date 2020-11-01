package com.example.android_voca;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class Fragment_VocaNote extends Fragment {

    RecyclerView recyclerView;
    VocaNoteAdapter adapter;
    //Note_MultiSelectionAdapter multi_adapter;

    boolean isLoading = false; //핸들러

    Retrofit retrofit;
    POSTApi postApi;
    String svcName = "Service_VocaNote.svc/";
    //ArrayList<String> allList = new ArrayList<>(); //단어장 전체 가져오기 (서비스에서 가져옴)
    //ArrayList<String> list = new ArrayList<>(); //단어장 20개씩 가져옴 (배열에 20개씩 담을 예정)

    List<VocaNote> allList; //전체 담음.
    List<VocaNote> list; // 10개씩
    List<VocaNote> list_20;

    //postResponse 크기 구하기
    int POST_Response;

    //noSearch == 20보다 작다 이제 더이상 없다는 뜻
    boolean noSearch = false;

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

    //카드 뷰 내용 : 단어장 명, 총 단어 수 일부만 저장. //무한 스크롤
    public static String[] Arr_VocaNoteName_Part = {};
    public static int[] Arr_VocaCount_Part = {};

    // 카드 뷰 내용 //
    public String VocaNoteName;
    public String VocaCount;

    //public  String[] title;

    public  int i = 0;
    //어댑터 사이즈

    int adapter_size = 0;

    //핸들러 반복 횟수
    static int handler_count;


    int LastPosition; //dy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context_Frag_Main = Fragment_VocaNote.this;
        final View v = inflater.inflate(R.layout.fragment_vocanote, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context_main));

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);
        //QUERY_NOTE();
        // 샘플로 예시 단어장 넣어본다 //

        handler_count = 1;
        list = new ArrayList<>();
        list_20 = new ArrayList<>();

        //QUERY(handler_count,MainActivity.Session_ID,0, postApi);
        testQQQQ(handler_count,MainActivity.Session_ID,0, postApi);

        //QUERY_VocaNote(); //처음에 담아둘 배열 데이터
        //allList = getList(); //전체 단어장 가져옴.
        //list =  getList_Part(); //일부 단어장 가져옴.
        initAdapter(); //리싸이클러 뷰 어댑터 생성

        initScrollListener(); //리싸이클러뷰 이벤트 발생

        return v;
    }

    private void initAdapter() {

        // 싱글 선택 어댑터 //

        adapter = new VocaNoteAdapter(this, list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnVocaNoteItemClickListener() {
            @Override
            public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position) {

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
    }

    //부분 배열에 새로운 값 20개 담아온다.
    private void GetAddData() {
        Log.d("Fragment_VocaNote", "GetAddData :");
        list.add(null); //빈 공간 추가

        recyclerView.scrollToPosition(list.size() - 1); //그 위치로 자동으로 스크롤 내려줌.

        //list_20.clear();
        //list_20 = new ArrayList<>();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.e("handler_count", "" + handler_count );

                list.remove(list.size() - 1); //2초 뒤 프로그레스바 배열에서 지움.
                int scrollPosition = list.size(); //현재 위치 저장
                adapter.notifyItemRemoved(scrollPosition); //프로그레스바 그림 사라짐
                int currentSize = scrollPosition; //현재 위치 = 스크롤 위치 = 마지막 값 0 ~ 9면 9
                int nextLimit = currentSize + POST_Response; //9에서 가져올 값 더해 19

                //현재 값 9에서 ~ 19까지 반복해서 전체 배열에서 현재 배열에 10개 담는다
                /*for(int i = currentSize; i<nextLimit; i++) {
                    if (i == list.size()) { //마지막에 도달하면 리턴
                        return;
                    }
                    list.add(allList.get(i));
                }*/

                //testQuery(currentSize);
                //testQQQQ(handler_count, MainActivity.Session_ID, 0, postApi);
                //QUERY(handler_count,MainActivity.Session_ID,currentSize, postApi);

                TTTTT(currentSize ,nextLimit);
                adapter.notifyDataSetChanged(); //새로고침;

                isLoading = false; //쓰레드
            }
        }, 2000);
    }

    public void QUERY(int Page_NO, String userid, int currentSize, POSTApi postApi) {


        //List<VocaNote> vocaNote = new VocaNote(Page_NO,20,userid,"CrDateNote desc");
        Call<List<VocaNote2>> call = postApi.GetVocaNote(new VocaNote2(Page_NO, 20, userid, "CrDateNote desc"));
        call.enqueue(new Callback<List<VocaNote2>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<VocaNote2>> call, Response<List<VocaNote2>> response) {
                if(!response.isSuccessful()) {
                    Log.e("Fragment_VocaNote","not success");
                    return;
                }

                List<VocaNote2> postResponse = response.body();
                POST_Response = postResponse.size();

                if(POST_Response < 20) { //20 불러왔는데 더 적은 수가 왔다면 다음엔 없다 = 서버 요청 x
                    noSearch = true;
                }
                if (postResponse != null) {

                   /* int j = currentSize + 20;
                    for (int a = currentSize; a < j; a++) {

                        list_20.add(new VocaNote(
                                postResponse.get(a).getVocaNoteName(),
                                postResponse.get(a).getNickname(),
                                postResponse.get(a).getCrDateNote(),
                                postResponse.get(a).getTotalVocaCount()
                        ));
                    }

                    for (int s = currentSize; s < j; s++) {
                        list.add(list_20.get(s));
                    }*/

                    for (VocaNote2 vocaNote2 : postResponse) {

                        list_20.add(new VocaNote(
                                vocaNote2.getVocaNoteName(),
                                vocaNote2.getTotalVocaCount()));

                        //list.add(new VocaNote(
                                //vocaNote.getVocaNoteName(),
                                /*vocaNote.getNickname(),
                                vocaNote.getCrDateNote(),*/
                                //vocaNote.getTotalVocaCount()));

                        /*list_20.add(new VocaNote(
                                vocaNote.getVocaNoteName(),
                                vocaNote.getNickname(),
                                vocaNote.getCrDateNote(),
                                vocaNote.getTotalVocaCount()));*/
                    }

                    //list.add(list_20.get(postResponse.size() - 1));
                    //testQuery(currentSize, postResponse);
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<VocaNote2>>  call, Throwable t) {
                Log.e("Fragment_VocaNote" , t.getMessage() +"");
            }
        });
    }

    public void testQuery(int i) {
        int j = i + 20;
        for (int a = i; a<j; a++) {
            list.add(new VocaNote("VocaNoteName"));
            /*list.add(new VocaNote(
                    postResponse.get(a).getVocaNoteName(),
                    postResponse.get(a).getTotalVocaCount()
            ));*/
        }
    }

    public void TTTTT(int i, int j) {

        //for (int a = i; a<j; a++) {
        for (int a = i; a< j; a++) {
            if (a == list_20.size()) {
                return;
            }
            list.add(new VocaNote(
                    list_20.get(a).getVocaNoteName(),
                    list_20.get(a).getTotalVocaCount()
            ));
        }
    }

    public void testQQQQ(int Page_NO, String userid, int currentSize, POSTApi postApi) {
        Call<List<VocaNote>> call = postApi.GetVocaNote(new VocaNote(Page_NO, 20, userid, "CrDateNote desc"));
        call.enqueue(new Callback<List<VocaNote>>() {
            @Override
            public void onResponse(Call<List<VocaNote>> call, Response<List<VocaNote>> response) {
                if(!response.isSuccessful()) {
                    Log.e("frag", "not success");
                    return;
                }

                List<VocaNote> postResponse = response.body();

                if (postResponse != null) {
                    for (VocaNote vocaNote : postResponse) {

                        list.add(new VocaNote(
                                vocaNote.getVocaNoteName(),
                                /*vocaNote.getNickname(),
                                vocaNote.getCrDateNote(),*/
                                vocaNote.getTotalVocaCount()));

                        list_20.add(new VocaNote(
                                vocaNote.getVocaNoteName(),
                                /*vocaNote.getNickname(),
                                vocaNote.getCrDateNote(),*/
                                vocaNote.getTotalVocaCount()));

                    }
                }
            }

            @Override
            public void onFailure(Call<List<VocaNote>> call, Throwable t) {

            }
        });
    }

    //리싸이클러뷰 이벤트
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("Fragment_VocaNote","onScrollStateChanged: ");

                MainActivity.fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //QUERY(2,MainActivity.Session_ID,0, postApi);
                        //GetAddData();
                    }
                });

                if(recyclerView.getScrollY() > LastPosition)
                    MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                else
                    MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LastPosition = dy;

                Log.d("Fragment_VocaNote", "onScrolled :");

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(!isLoading) {
                    //리니어 레이아웃 매니저가 null이거나 마지막 아이템 포지션이 부분배열 사이즈 - 1 값과 동일하면
                    //GetAddData()을 통해 list(부분 배열) 에다가 새로운 값 20개를 받아온다.
                    //isLoading 은 쓰레드 관련

                    if(!noSearch) { //더 이상 20개 이상 값이 없다. = 서버에서 요청하지 말아라.
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                                == list.size() - 1) { //list.size() - 1

                            adapter.notifyItemInserted(list.size() - 1); //그 공간에 프로그레스바 넣는다.
                            ++handler_count;

                            QUERY(handler_count,MainActivity.Session_ID,0, postApi);
                            GetAddData(); // 새 데이터 받아온다. (전체 배열에서 20개씩)

                            isLoading = true;
                            Toast.makeText(LoginActivity.context_Login, "스크롤 감지", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
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

        Arr_VocaNoteName_Part = new String[20];
        Arr_VocaCount_Part = new int[20];

        //WCF 서비스에서 가져온 전체 단어장 배열에 담는다. (테스트 50개)
        for (int a = 0; a<Arr_VocaNoteName.length; a++) {
            Arr_VocaNoteName[a] = "단어장 " + a;
            Arr_VocaCount[a] = a;
        }

        //다 가져온 배열에서 일부만 보여준다 (테스트 10개)
        for(int i = 0; i<20; i++) {
            Arr_VocaNoteName_Part[i] = Arr_VocaNoteName[i];
            Arr_VocaCount_Part[i] = Arr_VocaCount[i];
        }
    }

    //전체 단어장 (WCF)
    private List<VocaNote> getList() {
        List<VocaNote> list = new ArrayList<>();

        for(int i = 0; i< Arr_VocaNoteName.length; i++) {
            VocaNote model = new VocaNote(1,2,"a","a");
            model.setVocaNoteName(Arr_VocaNoteName[i]); // 단어장 명 //
            //model.setCrDateNote(Arr_CREATE_DATE[i]);
            model.setVocaCount(Arr_VocaCount[i]); // 총 단어 수 //
            list.add(model);
        }

        return list;
    }

    //일부 단어장 (배열에서 담기)
    private List<VocaNote> getList_Part() {
        List<VocaNote> list = new ArrayList<>();

        for(int i = 0; i< Arr_VocaNoteName_Part.length; i++) {
            VocaNote model = new VocaNote(1,2,"a","a");
            model.setVocaNoteName(Arr_VocaNoteName_Part[i]); //단어장 명 (일부)
            model.setVocaCount(Arr_VocaCount_Part[i]); //총 단어 수 (일부)
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

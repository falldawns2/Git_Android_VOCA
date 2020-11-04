package com.example.android_voca;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Challenge extends Fragment {

    CardView CardView_VocaCard, CardView_Quiz, CardView_Spelling;
    AppCompatSpinner VocaNote_Spinner, Chapter_Spinner;

    Retrofit retrofit;
    POSTApi postApi;

    final String svcName = "Service_VocaNote.svc/";
    final String TAG = "Fragment_Challenge";

    int POST_Response;

    String VocaNoteName; //스피너 단어장 명 담기

    List<String> arrayVocaNoteSpinner;
    List<String> arrayChapterSpinner;

    class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.CardView_VocaCard:
                    //먼저 커스텀 다이얼로그 안에 있는 스피너에 값을 담는다.
                    CustomDialog_Select CustomDialog = new CustomDialog_Select(getContext(), new CustomDialogSelectClickListener() {
                        @Override
                        public void onPositiveClick() {
                            Log.e("test","OK");
                        }

                        @Override
                        public void onNegativeClick() {
                            Log.e("test","cancel");
                        }
                    });
                    CustomDialog.setCanceledOnTouchOutside(true);
                    CustomDialog.setCancelable(true);
                    CustomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    CustomDialog.show();

                    VocaNote_Spinner = (AppCompatSpinner) CustomDialog.findViewById(R.id.VocaNote_Spinner);
                    Chapter_Spinner = (AppCompatSpinner) CustomDialog.findViewById(R.id.Chapter_Spinner);

                    //Spinner Selected
                    OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener();
                    VocaNote_Spinner.setOnItemSelectedListener(onItemSelectedListener);

                    //Retrofit 비동기 호출 (단어장 목록 스피너)
                    VocaNote_Spinner_Insert(MainActivity.Session_ID, "CrDateNote desc");
                    break;
            }
        }
    }

    class OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            Chapter_Spinner.setAdapter(null); //들어있는 값 초기화

            //선택된 값을 포함 Retrofit 비동기 호출 (챕터 목록 스피너)
            Chapter_Spinner_Insert(MainActivity.Session_ID,adapterView.getItemAtPosition(i).toString(),"CrDateNote desc");
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public void VocaNote_Spinner_Insert(String userid, String OrderBy) {
        Call<List<VocaNote_Chapter_List>> call = postApi.GetVocaNoteList(new VocaNote_Chapter_List(userid, OrderBy));
        //비동기 호출
        call.enqueue(new Callback<List<VocaNote_Chapter_List>>() {
            @Override
            public void onResponse(Call<List<VocaNote_Chapter_List>> call, Response<List<VocaNote_Chapter_List>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: not success");
                    return;
                }

                List<VocaNote_Chapter_List> postResponse = response.body();
                POST_Response = postResponse.size();

                arrayVocaNoteSpinner = new ArrayList<String>();

                for(VocaNote_Chapter_List vocaNote_chapter_list : postResponse) {
                    arrayVocaNoteSpinner.add(vocaNote_chapter_list.getVocaNoteName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.context_main,
                        android.R.layout.simple_spinner_item,arrayVocaNoteSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                VocaNote_Spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<VocaNote_Chapter_List>> call, Throwable t) {
                Log.e(TAG, "onFailure: 네트워크 연결 실패" + t.getMessage());
            }
        });
    }

    public void Chapter_Spinner_Insert(String userid, String VocaNoteName, String OrderBy) {
        Call<List<VocaNote_Chapter_List>> call = postApi.GetChapterList(new VocaNote_Chapter_List(userid,VocaNoteName,OrderBy));
        call.enqueue(new Callback<List<VocaNote_Chapter_List>>() {
            @Override
            public void onResponse(Call<List<VocaNote_Chapter_List>> call, Response<List<VocaNote_Chapter_List>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: not success");
                    return;
                }

                List<VocaNote_Chapter_List> postResponse = response.body();
                POST_Response = postResponse.size();

                arrayChapterSpinner = new ArrayList<String>();

                for(VocaNote_Chapter_List vocaNote_chapter_list : postResponse) {
                    arrayChapterSpinner.add(vocaNote_chapter_list.getChapterName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.context_main,
                        android.R.layout.simple_spinner_item,arrayChapterSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                Chapter_Spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<VocaNote_Chapter_List>> call, Throwable t) {
                Log.e(TAG, "onFailure:network fail" + t.getMessage());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_challenge, container, false);

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);

        //CardView Click
        BtnOnClickListener onClickListener = new BtnOnClickListener();

        CardView_VocaCard = (CardView) v.findViewById(R.id.CardView_VocaCard);
        CardView_Quiz = (CardView) v.findViewById(R.id.CardView_Quiz);
        CardView_Spelling = (CardView) v.findViewById(R.id.CardView_Spelling);

        CardView_VocaCard.setOnClickListener(onClickListener);
        CardView_Quiz.setOnClickListener(onClickListener);
        CardView_Spelling.setOnClickListener(onClickListener);

        return v;
    }
}

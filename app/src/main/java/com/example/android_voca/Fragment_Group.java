package com.example.android_voca;

import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.material.bottomappbar.BottomAppBar;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Group extends Fragment {

    RecyclerView recyclerView;
    GroupAdapter adapter;

    boolean isLoading = false; //핸들러

    Retrofit retrofit;
    POSTApi postApi;

    final String svcName = "Service_Group.svc/";
    final String TAG = "Fragment_Group";

    List<Group> list;
    List<Group> list_20;

    int POST_Response;

    //20보다 작다 == 서버에서 불러오지 마라
    boolean noSearch = false;

    //
    public static Fragment_Group context_Frag_Group;

    //어댑터 사이즈
    int adapter_size = 0;

    //핸들러 반복 횟수
    static int handler_count;

    int LastPosition; //dy;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_group, container, false);

        context_Frag_Group = Fragment_Group.this;

        final View view = inflater.inflate(R.layout.fragment_group, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,1);
        recyclerView.setLayoutManager(layoutManager);

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);

        handler_count = 1;
        list = new ArrayList<>();
        list_20 = new ArrayList<>();

        QUERY_Group_ONE(handler_count, postApi);

        initAdapter(); //어댑터 생성

        initScrollListener(); //리싸이클러뷰 이벤트


        return view;
    }

    private void initAdapter() {
        adapter = new GroupAdapter(this, list);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnVocaNoteItemClickListener() {
            @Override
            public void onItemClick(VocaNoteAdapter.ItemViewHolder holder, View view, int position) {
                //단어장
            }

            @Override
            public void onItemChapterClick(ChapterAdapter.ItemViewHolder holder, View view, int position) {
                //챕터
            }

            @Override
            public void onItemVocaClick(VocaAdapter.ViewHolder holder, View view, int position) {
                //단어
            }

            @Override
            public void onItemBoardClick(BoardAdapter.ItemViewHolder holder, View view, int position) {
                //게시판
            }

            @Override
            public void onItemGroupClick(GroupAdapter.GroupItemViewHolder holder, View view, int position) {
                //그룹

                //그룹 내용 페이지 이동
                Toast.makeText(MainActivity.context_main, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void QUERY_Group_ONE(int Page_NO, POSTApi postApi) {
        //비동기 호출
        retrofit2.Call<List<Group>> call = postApi.GetGroup(new Group(Page_NO,20));
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: ");
                    return;
                }

                List<Group> postResponse = response.body();

                if(postResponse != null) {
                    for(Group group : postResponse) {
                        list.add(new Group(
                                group.getGroupName(),
                                group.getGroupImage()
                        ));

                        list_20.add(new Group(
                                group.getGroupName(),
                                group.getGroupImage()
                        ));
                    }
                }

                response.body().clear();
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    //리싸이클러 뷰 이벤트
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG, "onScrollStateChanged: " );

                // 이부분은 하지 않는다. fab 버튼 이동할 필요 없이 로딩이 오른쪽에 있다.
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                LastPosition = dy;

                Log.d(TAG, "onScrolled: ");

                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                int[] size = new int[3]; //3열
                int[] a = layoutManager.findLastCompletelyVisibleItemPositions(size);

                if(!isLoading) {
                    if(!noSearch) {
                        Log.e(TAG, "test1: " + a[0] + " " + a[1] + " " + a[2] );
                        if(layoutManager != null && a[1] == list.size() - 1) {
                            adapter.notifyItemInserted(list.size() - 1);
                            ++handler_count;

                            QUERY(handler_count, postApi);
                            GetAddData();

                            isLoading = true;
                            Toast.makeText(MainActivity.context_main, "스크롤 감지", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void QUERY(int Page_NO, POSTApi postApi) {
        Call<List<Group2>> call = postApi.GetGroup(new Group2(Page_NO, 20));

        call.enqueue(new Callback<List<Group2>>() {
            @Override
            public void onResponse(Call<List<Group2>> call, Response<List<Group2>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " );
                    return;
                }


                List<Group2> postResponse = response.body();
                POST_Response = postResponse.size();

                if(POST_Response < 20) {
                    noSearch = true;
                }

                if(postResponse != null) {
                    for(Group2 group2 : postResponse) {
                        list_20.add(new Group(
                                group2.getGroupName(),
                                group2.getGroupImage()
                        ));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Group2>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    //새로운 값
    private void GetAddData() {
        Log.d(TAG, "GetAddData: ");
        list.add(null); //빈 공간 추가

        recyclerView.scrollToPosition(list.size() - 1); // 그 위치 자동으로 스크롤 내려줌.

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: " + handler_count);

                list.remove(list.size() - 1); //2초 뒤 프로그레스 바 배열에서 지움
                int scrollPosition = list.size(); //현재 위치 저장
                adapter.notifyItemRemoved(scrollPosition); // 프로그래스바 사라짐
                int currentSize = scrollPosition; // 현재 위치 = 스크롤 위치 = 마지막 값 0 ~ 9면 9
                int nextLimit = currentSize + POST_Response; //9에서 가져올 값 더해 19

                //현재 값 9 ~ 19 까지 반복해서 전체 배열에서 현재 배열에 10개 담는다.
                Copy(currentSize, nextLimit);
                adapter.notifyDataSetChanged(); //새로 고침

                isLoading = false; //쓰레드
            }
        }, 2000);
    }

    public void Copy(int i, int j) {
        for (int a = i; a < list_20.size(); a++) {
            if (a == list_20.size()) {
                return;
            }

            list.add(new Group(
                    list_20.get(a).getGroupName(),
                    list_20.get(a).getGroupImage()
            ));
        }
    }
}

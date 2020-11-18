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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.android.material.bottomappbar.BottomAppBar;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Board extends Fragment {

    RecyclerView recyclerView;
    BoardAdapter adapter;

    boolean isLoading = false; //핸들러

    Retrofit retrofit;
    POSTApi postApi;
    final String svcName = "Service_Board.svc/";
    final String TAG = "Fragment_Board";

    List<Board> list;
    List<Board> list_20;

    int POST_Response;

    //20보다 작다 == 서버에서 불러오지 마라
    boolean noSearch = false;

    ///
    public static Fragment_Board context_Frag_Board;


    //어댑터 사이즈
    int adapter_size = 0;

    //핸들러 반복 횟수
    static int handler_count;

    int LastPosition; //dy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_board, container, false);

        context_Frag_Board = Fragment_Board.this;
        final View view = inflater.inflate(R.layout.fragment_board, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context_main));

        retrofit = new Retrofit(postApi);
        postApi = retrofit.setRetrofitInit(svcName);

        handler_count = 1;
        list = new ArrayList<>();
        list_20 = new ArrayList<>();

        QUERY_Board_ONE(handler_count, postApi);

        initAdapter(); //리싸이클러 뷰 어댑터 생성

        initScrollListener(); //리싸이클러 뷰 이벤트 발생

        return view;
    }

    private void initAdapter() {
        adapter = new BoardAdapter(this, list);

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

                //게시판 글 내용 페이지로 이동한다.
                Toast.makeText(MainActivity.context_main, "" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemGroupClick(GroupAdapter.GroupItemViewHolder holder, View view, int position) {
                //그룹
            }
        });
    }

    public void QUERY_Board_ONE(int Page_NO, POSTApi postApi) {
        //이곳은 처음 시작이라 비동기 호출 가능할 듯 하다.
        retrofit2.Call<List<Board>> call = postApi.GetBoard(new Board(Page_NO, 20));
        call.enqueue(new Callback<List<Board>>() {
            @Override
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: ");
                    return;
                }

                List<Board> postResponse = response.body();

                if (postResponse != null) {
                    for (Board board : postResponse) {
                        list.add(new Board(
                                board.getProfileimage(),
                                board.getTitle(),
                                board.getUploadtime(),
                                board.getHits(),
                                board.getComments(),
                                board.getNickname()
                        ));

                        list_20.add(new Board(
                                board.getProfileimage(),
                                board.getTitle(),
                                board.getUploadtime(),
                                board.getHits(),
                                board.getComments(),
                                board.getNickname()
                        ));
                    }
                }
                response.body().clear();
            }

            @Override
            public void onFailure(Call<List<Board>> call, Throwable t) {
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
                Log.e(TAG, "onScrollStateChanged:");

                //만약 스크롤을 내리고 있다면 fab 버튼을 오른쪽으로 이동시킴
                //아니라면 다시 가운데로 이동시킴

                if (recyclerView.getScrollY() > LastPosition)
                    MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                else
                    MainActivity.bottom_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LastPosition = dy;

                Log.d(TAG, "onScrolled: ");

                // TODO: 이 부분 참고
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //StaggeredGridLayoutManager linearLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                //recyclerView.setLayoutManager(linearLayoutManager);
                //int[] size = new int[3];
                //int[] a = linearLayoutManager.findLastCompletelyVisibleItemPositions(size);

                //비교 값 : a[3]
                if (!isLoading) {
                    //리니어 레이아웃 매니저가 null or 마지막 아이템 포지션이 부분배열 사이즈 - 1값과 동일
                    //getadddata 메서드 실행 - > 부분배열에다가 새로운 값 20개 추가
                    //isloading 쓰레드 관련
                    if (!noSearch) { // 더이상 20개 이상 값이 없다 =서버 요청 x
                        //Log.e(TAG, "test1: " + a[0] + " " + a[1] + " " + a[2] );
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                                == list.size() - 1) {
                            adapter.notifyItemInserted(list.size() - 1); //그 공간에 프로그레스 바 넣는다.
                            ++handler_count;

                            QUERY(handler_count, postApi);
                            GetAddData(); //새 데이터를 받아온다. (전체 배열에서 20개씩)

                            isLoading = true;
                            //Toast.makeText(MainActivity.context_main, "스크롤 감지", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void QUERY(int Page_NO, POSTApi postApi) {
        Call<List<Board2>> call = postApi.GetBoard(new Board2(Page_NO, 20));

        /*동기 호출
        * POST_Response = list_20.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Board2 board2 : call.execute().body()) {
                        list_20.add(new Board(
                                board2.getProfileimage(),
                                board2.getTitle(),
                                board2.getUploadtime(),
                                board2.getHits(),
                                board2.getComments(),
                                board2.getNickname()
                        ));
                    }
                } catch (IOException e) {

                }
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (POST_Response < 20) { //20개 불렀는데 더 적은 수가 옴 = 다음 페이지 없다. = > 서버 요청 x
            noSearch = true;
        }
        * */

        call.enqueue(new Callback<List<Board2>>() {
            @Override
            public void onResponse(Call<List<Board2>> call, Response<List<Board2>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " );
                    return;
                }

                List<Board2> postResponse = response.body();
                POST_Response = postResponse.size();

                if(POST_Response < 20) { //20 불러왔는데 더 적으면 서버 요청 x
                    noSearch = true;
                }

                if(postResponse != null) {
                    for (Board2 board2 : postResponse) {
                        list_20.add(new Board(
                                board2.getProfileimage(),
                                board2.getTitle(),
                                board2.getUploadtime(),
                                board2.getHits(),
                                board2.getComments(),
                                board2.getNickname()
                        ));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Board2>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    //부분 배열에 새로운 값 20개 담아온다.
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
            list.add(new Board(
                    list_20.get(a).getProfileimage(),
                    list_20.get(a).getTitle(),
                    list_20.get(a).getUploadtime(),
                    list_20.get(a).getHits(),
                    list_20.get(a).getComments(),
                    list_20.get(a).getNickname()
            ));
        }
    }
}

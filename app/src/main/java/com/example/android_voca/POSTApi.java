package com.example.android_voca;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface POSTApi {
    //@FormUrlEncoded
    @Headers({ "Content-Type: application/json; charset=UTF-8"}) //json 형태로 반환 헤더
    @POST("Authenticate") //주소 경로
    Call<LoginCheck> Authenticate(@Body LoginCheck loginCheck); //Body에 담을 값
    //Call<LoginCheck> Authenticate(@FieldMap Map<String, STring> fields);

    //프로필 이미지 가져오기
    @POST("GetProfileImage")
    Call<AccountInfo> ProfileImage(@Body AccountInfo accountInfo);

    //닉네임 가져오기
    @POST("GetNickName")
    Call<AccountInfo> NickName(@Body AccountInfo accountInfo);

    //단어장
    @POST("GetVocaNote")
    Call<List<VocaNote>> GetVocaNote(@Body VocaNote vocaNote);

    //단어장 20개씩 받아오는
    @POST("GetVocaNote")
    Call<List<VocaNote2>> GetVocaNote(@Body VocaNote2 vocaNote2);

    //동기 호출
    //챕터
    @POST("GetChapter")
    Call<List<Chapter>> GetChapter(@Body Chapter chapter);

    //챕터 20개씩 받아오는
    @POST("GetChapter")
    Call<List<Chapter2>> GetChapter(@Body Chapter2 chapter2);

    //단어장 리스트 (스피너 담기)
    @POST("GetVocaNoteList")
    Call<List<VocaNote_Chapter_List>> GetVocaNoteList(@Body VocaNote_Chapter_List vocaNote_chapter_list);

    //챕터 리스트 (스피너 담기)
    @POST("GetChapterList")
    Call<List<VocaNote_Chapter_List>> GetChapterList(@Body VocaNote_Chapter_List vocaNote_chapter_list);

    //동기 호출
    //단어,뜻 (단어 카드)
    @POST("GetVocaMean")
    Call<List<VocaCard>> GetVocaMean(@Body VocaCard vocaCard);

    //게시판
    @POST("GetBoard")
    Call<List<Board>> GetBoard(@Body Board board);

    //게시판 20개씩
    @POST("GetBoard")
    Call<List<Board2>> GetBoard(@Body Board2 board2);

    //그룹
    @POST("GetGroup")
    Call<List<Group>> GetGroup(@Body Group group);

    //그룹 20개씩
    @POST("GetGroup")
    Call<List<Group2>> GetGroup(@Body Group2 group2);

    //단어
    @POST("GetVoca")
    Call<List<Voca>> GetVoca(@Body Voca voca);

    //단어 20개씩
    @POST("GetVoca")
    Call<List<Voca2>> GetVoca(@Body Voca2 voca2);

    //단어장 추가
    @POST("InsertVocaNote")
    Call<ADD> InsertVocaNote(@Body ADD add);

    //챕터 추가
    @POST("InsertChapter")
    Call<ChapterADD> InsertChapter(@Body ChapterADD add);
}

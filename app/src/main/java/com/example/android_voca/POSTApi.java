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
}
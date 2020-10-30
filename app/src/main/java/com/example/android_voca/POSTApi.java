package com.example.android_voca;

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
}

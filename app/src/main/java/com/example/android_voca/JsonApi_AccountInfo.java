package com.example.android_voca;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonApi_AccountInfo {
    @GET("Getmembers")
    Call<List<AccountInfo>> getAccountInfo();
}

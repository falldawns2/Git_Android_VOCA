package com.example.android_voca;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    POSTApi postApi;

    //생성자 인터페이스 받음
    public Retrofit(POSTApi postApi) {
        this.postApi = postApi;
    }

    public POSTApi setRetrofitInit(String svcName) {

        //log, OkHttp 3
        OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ClientBuilder.addInterceptor(loggingInterceptor);

        //Retrofit 2 빌드
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.0.2/WCF_Android/" + svcName)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ClientBuilder.build()) //로그 기록
                .build();

        postApi = retrofit.create(POSTApi.class);

        return postApi; //인터페이스 반환
    }
}

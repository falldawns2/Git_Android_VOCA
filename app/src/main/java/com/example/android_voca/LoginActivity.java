package com.example.android_voca;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText Userid;
    private EditText Passwd;

    private POSTApi postApi;

    private final String svcName = "Service_Account.svc/";

    public static Context context_Login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context_Login = LoginActivity.this;

        Userid = (EditText)findViewById(R.id.Userid);
        Passwd = (EditText)findViewById(R.id.PassWd);

        // 로그인 버튼 Button //
        Button btnLogin = findViewById(R.id.btnLogin);
        // 로고 ImageView //
        ImageView image_Logo = findViewById(R.id.image_Logo);

        // Display 해상도, 밀도, 스케일링 정보 객체 //
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        //ImageView 크기 폰의 화면에 따라 조절 //
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) image_Logo.getLayoutParams();
        params.width = metrics.widthPixels ;
        params.height = metrics.heightPixels / 4;

        image_Logo.setLayoutParams(params);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Retrofit
                Retrofit retrofit = new Retrofit(postApi);
                postApi = retrofit.setRetrofitInit(svcName); //반환된 인터페이스 받음
                //로그인 체크 Authenticate
                LoginCheck loginCheck = new LoginCheck(Userid.getText().toString(), Passwd.getText().toString());
                Call<LoginCheck> call = postApi.Authenticate(loginCheck);
                call.enqueue(new Callback<LoginCheck>() {
                    @Override
                    public void onResponse(Call<LoginCheck> call, Response<LoginCheck> response) {
                        if(!response.isSuccessful()) {
                            Userid.setText("code : " + response.code());
                            return;
                        }

                        LoginCheck postResponse = response.body();

                        if (postResponse.getValue()) { //성공
                            //성공하면 MainActivity 로 이동한다.
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            //로그인하면 로그인 액티비티 스택삭제.
                            //메인 액티비티에서 뒤로가면 알림창 띄우고 앱 종료.
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("Session_ID",Userid.getText().toString()); //유저아이디 세션 저장
                            startActivity(intent);
                        } else { //실패
                            Userid.setHint("로그인 실패");
                            Userid.setText("");
                            Passwd.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginCheck> call, Throwable t) {

                    }
                });
                /*//메인 액티비티로 이동. (로그인 성공 시) 임시로 그냥 넘어감.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                //로그인하면 로그인 액티비티 스택삭제.
                //메인 액티비티에서 뒤로가면 알림창 띄우고 앱 종료.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                //로그인 성공 시 DB에서 ID를 받아 보냄.
                //메인 액티비티에서는 받은 세션id값으로 닉네임을 찾아내 뿌림.
                //AccountInfo accountInfo = new AccountInfo("z","김태훈","개발자");

                //intent.putExtra("accountInfo",accountInfo);
                //intent.putExtra("Session_ID","z");

                startActivity(intent);*/
            }
        });
    }
}

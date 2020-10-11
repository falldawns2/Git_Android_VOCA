package com.example.android_voca;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                //메인 액티비티로 이동. (로그인 성공 시) 임시로 그냥 넘어감.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                //로그인하면 로그인 액티비티 스택삭제.
                //메인 액티비티에서 뒤로가면 알림창 띄우고 앱 종료.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                //로그인 성공 시 DB에서 ID를 받아 보냄.
                //메인 액티비티에서는 받은 세션id값으로 닉네임을 찾아내 뿌림.
                AccountInfo accountInfo = new AccountInfo("z","김태훈","개발자");

                intent.putExtra("accountInfo",accountInfo);
                //intent.putExtra("Session_ID","z");

                startActivity(intent);
            }
        });
    }
}

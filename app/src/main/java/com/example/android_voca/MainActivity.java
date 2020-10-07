package com.example.android_voca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //String Session_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); //로그인 액티비티

        //Session_ID = intent.getExtras().getString("Session_ID");

        AccountInfo accountInfo = (AccountInfo)intent.getSerializableExtra("accountInfo");

        //받아온 값을 여기서 닉네임을 찾아서 다시 뿌려야 한다. (DB 연결 시)
        TextView test = findViewById(R.id.test);
        test.setText(accountInfo.Session_ID + accountInfo.Name + accountInfo.Nickcname);
    }
}
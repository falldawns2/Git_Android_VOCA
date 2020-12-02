package com.example.android_voca;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomDialog_VocaNote extends Dialog {

    private CustomDialogSelectClickListener customDialogSelectClickListener; //클릭 여부
    private TextView tvNegative, tvPositive;

    private TextView tvTitle;
    public static EditText InsertVocaNoteName;

    private Context context;

    public CustomDialog_VocaNote(@NonNull Context context, CustomDialogSelectClickListener customDialogSelectClickListener) {
        super(context);
        this.context = context;
        this.customDialogSelectClickListener = customDialogSelectClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_vocanote);

        tvTitle = findViewById(R.id.option_codetype_dialog_title_tv);
        tvPositive = findViewById(R.id.option_codetype_dialog_positive);
        tvNegative = findViewById(R.id.option_codetype_dialog_negative);

        Log.e("asdf", "onCreate: " + context.toString());

        InsertVocaNoteName = findViewById(R.id.InsertVocaNoteName);

        if (context.toString().contains("MainActivity")) {
            tvTitle.setText("단어장 추가");
            InsertVocaNoteName.setHint("단어장명을 입력하세요.");
        } else if (context.toString().contains("ChapterActivity")) {
            tvTitle.setText("챕터 추가");
            InsertVocaNoteName.setHint("챕터명을 입력하세요.");
        }

        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //저장
                customDialogSelectClickListener.onPositiveClick();
                dismiss();
            }
        });

        tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //취소
                customDialogSelectClickListener.onNegativeClick();;
                dismiss();
            }
        });
    }
}

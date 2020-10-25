package com.example.android_voca;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomDialog_VocaNote extends Dialog {

    private Context context;
    private CustomDialogSelectClickListener customDialogSelectClickListener; //클릭 여부
    private TextView tvTitle, tvNegative, tvPositive;

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

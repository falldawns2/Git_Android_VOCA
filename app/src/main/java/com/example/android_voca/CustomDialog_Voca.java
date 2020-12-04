package com.example.android_voca;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomDialog_Voca extends Dialog {

    private final String TAG = "CustomDialog_Voca";
    private CustomDialogVocaClickListener customDialogVocaClickListener; //클릭 여부
    private TextView tvNegative, tvPositive, tvADD;

    public static TextView tvTitle;
    public static EditText InsertVoca, InsertMean, InsertSentence, InsertInterpretation;

    private Context context;

    public CustomDialog_Voca(@NonNull Context context, CustomDialogVocaClickListener customDialogVocaClickListener) {
        super(context);
        this.context = context;
        this.customDialogVocaClickListener = customDialogVocaClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_voca);

        tvTitle = findViewById(R.id.option_codetype_dialog_title_tv);

        tvPositive = findViewById(R.id.option_codetype_dialog_positive);
        tvNegative = findViewById(R.id.option_codetype_dialog_negative);
        tvADD = findViewById(R.id.option_codetype_dialog_ADD);

        Log.e(TAG, "onCreate: " + context.toString());

        InsertVoca = findViewById(R.id.InsertVoca);
        InsertMean = findViewById(R.id.InsertMean);
        InsertSentence = findViewById(R.id.InsertSentence);
        InsertInterpretation = findViewById(R.id.InsertInterpretation);

        tvTitle.setText("단어 추가");

        InsertVoca.setHint("단어");
        InsertMean.setHint("뜻");
        InsertSentence.setHint("예문 (선택사항)");
        InsertInterpretation.setHint("해석 (선택사항)");

        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //저장
                customDialogVocaClickListener.onPositiveClick();
                //dismiss();
            }
        });

        tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //취소
                customDialogVocaClickListener.onNegativeClick();
                dismiss();
            }
        });

        tvADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //계속 추가
                customDialogVocaClickListener.onADDClick();
                //dismiss();
            }
        });
    }
}

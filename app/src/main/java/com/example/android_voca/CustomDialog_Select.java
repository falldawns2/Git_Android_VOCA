package com.example.android_voca;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomDialog_Select extends Dialog {

    private Context context;
    private CustomDialogSelectClickListener customDialogSelectClickListener;
    private TextView tvNegatvie, tvPositive;

    public TextView tvTitle;

    Spinner spinner, spinner2;
    ArrayList<String> arrayList, arrayList2;
    ArrayAdapter<String> arrayAdapter;

    View view;


    public CustomDialog_Select(View view,@NonNull Context context, CustomDialogSelectClickListener customDialogSelectClickListener) {
        super(context);
        this.view = view;
        this.context = context;
        this.customDialogSelectClickListener = customDialogSelectClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_select);

        tvTitle = findViewById(R.id.option_codetype_dialog_title_tv);
        tvPositive = findViewById(R.id.option_codetype_dialog_positive);
        tvNegatvie = findViewById(R.id.option_codetype_dialog_negative);

        //단어선택, 단어 뜻 맞추기, 스펠링 쓰기
        switch (view.getId()) {
            case R.id.CardView_VocaCard:
                tvTitle.setText("단어 카드");
                break;
            case R.id.CardView_Quiz:
                tvTitle.setText("단어/뜻 맞추기");
                break;
            case R.id.CardView_Spelling:
                tvTitle.setText("스펠링 맞추기");
                break;
        }

        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //저장
                customDialogSelectClickListener.onPositiveClick();
                dismiss();
            }
        });

        tvNegatvie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //취소
                customDialogSelectClickListener.onNegativeClick();
                dismiss();
            }
        });

        arrayList = new ArrayList<>();
        arrayList.add("단어장1");
        arrayList.add("단어장2");
        arrayList.add("단어장3");
        arrayList.add("단어장4");
        arrayList.add("단어장5");
        arrayList.add("단어장6");

        arrayAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList);

        spinner = (Spinner) findViewById(R.id.VocaNote_Spinner);
        spinner.setAdapter(arrayAdapter);


        arrayList2 = new ArrayList<>();
        arrayList2.add("챕터 1");
        arrayList2.add("챕터 2");
        arrayList2.add("챕터 3");
        arrayList2.add("챕터 4");
        arrayList2.add("챕터 5");
        arrayList2.add("챕터 6");

        arrayAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList2);

        spinner2 = (Spinner) findViewById(R.id.Chapter_Spinner);
        spinner2.setAdapter(arrayAdapter);
    }
}

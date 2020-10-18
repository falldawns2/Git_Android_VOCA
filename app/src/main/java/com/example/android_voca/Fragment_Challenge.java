package com.example.android_voca;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Fragment_Challenge extends Fragment {

    CardView cardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_challenge, container, false);
        cardView = (CardView) v.findViewById(R.id.CardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();

                CustomDialog_Select CustomDialog = new CustomDialog_Select(getContext(), new CustomDialogSelectClickListener() {
                    @Override
                    public void onPositiveClick() {
                        Log.e("test","OK");
                    }

                    @Override
                    public void onNegativeClick() {
                        Log.e("test","cancel");
                    }
                });
                CustomDialog.setCanceledOnTouchOutside(true);
                CustomDialog.setCancelable(true);
                CustomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                CustomDialog.show();
            }
        });

        return v;
    }
}

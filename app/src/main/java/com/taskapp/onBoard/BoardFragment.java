package com.taskapp.onBoard;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskapp.MainActivity;
import com.taskapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {
    View view;
    private int position;

    public BoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_board, container, false);
        TextView textView = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        ImageView imageView = view.findViewById(R.id.imageView);
        Button buttonStart = view.findViewById(R.id.buttonStart);


        textView.setText("Привет");


        position = getArguments().getInt("pos");

        switch (position) {
            case 0:
                textView.setText("Привет");
                textView2.setText("Описание");
                imageView.setImageResource(R.drawable.earth);
                buttonStart.setVisibility(View.INVISIBLE);
                view.setBackgroundResource(R.drawable.backgrround_gradient1);
                break;
            case 1:
                textView.setText("Как дела?");
                textView2.setText("Описание");
                imageView.setImageResource(R.drawable.moon);
                buttonStart.setVisibility(View.INVISIBLE);


                view.setBackgroundResource(R.drawable.backgrround_gradient2);
                break;
            case 2:
                textView.setText("Что делаешь?");
                textView2.setText("Описание");
                imageView.setImageResource(R.drawable.mars);

                view.setBackgroundResource(R.drawable.backgrround_gradient3);

                break;
        }
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
                SharedPreferences preferences = getActivity().
                        getSharedPreferences("settings", Context.MODE_PRIVATE);
                preferences.edit().putBoolean("isShown", true).apply();

            }
        });

        return view;
    }

}

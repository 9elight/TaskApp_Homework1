package com.taskapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;




public class Form_Activity extends AppCompatActivity {

    private EditText editTitle;
    private EditText description;
    private Task mTask;
    private ProgressBar progressBar;
    private Button saveButton;
    private LinearLayout linearLayout;
    private AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_);
        editTitle = findViewById(R.id.editTitle);
        description = findViewById(R.id.description);
        progressBar = findViewById(R.id.loading);
        saveButton = findViewById(R.id.saveButton);
        linearLayout = findViewById(R.id.linearLayout);
        animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);

        mTask = (Task) getIntent().getSerializableExtra("HomeFragmentTask");
        if (mTask != null) {
            editTitle.setText(mTask.getTitle(), TextView.BufferType.EDITABLE);
            description.setText(mTask.getDesc(), TextView.BufferType.EDITABLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

    public void onSave(View view) {

        String title = editTitle.getText().toString().trim();
        String desc = description.getText().toString().trim();
        if (title.isEmpty() || desc.isEmpty()){
            editTitle.setError("Поле пустое");
            editTitle.requestFocus();
            description.setError("Поле пустое");
            description.requestFocus();
            YoYo.with(Techniques.Shake).duration(1000).repeat(1).playOn(editTitle);
            YoYo.with(Techniques.Shake).duration(1000).repeat(1).playOn(description);
        }else {
            if (mTask != null) {

                mTask.setTitle(title);
                mTask.setDesc(desc);
                progressBar.setVisibility(ProgressBar.VISIBLE);

                updateTaskInFirestore();
                progressBar.setVisibility(ProgressBar.GONE);

            } else {

                mTask = new Task(title, desc);
                progressBar.setVisibility(ProgressBar.VISIBLE);
                saveToFirestore();
                progressBar.setVisibility(ProgressBar.GONE);


            }

            saveButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }

    }

    private void saveToFirestore() {

        FirebaseFirestore.getInstance().collection("tasks").add(mTask).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> task) {
                if (task.isSuccessful()) {

                    mTask.setId(task.getResult().getId());
                    App.getDataBase().taskDao().insert(mTask);
                    Toaster.show("Успешно");

                    finish();
                } else {
                    Toaster.show("Ошибка " + task.getException().getMessage());
                }
            }
        });

    }

    private void updateTaskInFirestore() {

        FirebaseFirestore.getInstance().collection("tasks").document(mTask.getId())
                .set(mTask).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if (task.isSuccessful()) {

                    App.getDataBase().taskDao().update(mTask);
                    Toaster.show("Успешно");

                    finish();
                } else {
                    Toaster.show("Ошибка " + task.getException().getMessage());
                }
            }
        });

    }
}

package com.taskapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Form_Activity extends AppCompatActivity {

    private EditText editTitle;
    private EditText description;
    private Task mTask;
    private ProgressBar progressBar;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_);
        editTitle = findViewById(R.id.editTitle);
        description = findViewById(R.id.description);
        progressBar = findViewById(R.id.loading);
        saveButton = findViewById(R.id.saveButton);
        mTask = (Task) getIntent().getSerializableExtra("HomeFragmentTask");
        if (mTask != null) {
            editTitle.setText(mTask.getTitle(), TextView.BufferType.EDITABLE);
            description.setText(mTask.getDesc(), TextView.BufferType.EDITABLE);
        }
    }


    public void onSave(View view) {

        String title = editTitle.getText().toString().trim();
        String desc = description.getText().toString().trim();
        if (mTask != null) {

            mTask.setTitle(title);
            mTask.setDesc(desc);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            updateTaskInFirestore();
            progressBar.setVisibility(ProgressBar.GONE);

//            tasks.put("tasks", mTask);


//            FirebaseFirestore.getInstance().collection("tasks").document(taskId)
//                    .set(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
//                    if(task.isSuccessful()){
//                        Toast.makeText(Form_Activity.this,"Успешно", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(Form_Activity.this,"Ошибка", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });

        } else {

            mTask = new Task(title, desc);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            saveToFirestore();
            progressBar.setVisibility(ProgressBar.GONE);

//            tasks.put("tasks",mTask);

//            FirebaseFirestore.getInstance().collection("tasks").add(tasks)
//                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                @Override
//                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> mTask) {
//                    if(mTask.isSuccessful()){
//                        Toast.makeText(Form_Activity.this,"Успешно", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(Form_Activity.this,"Ошибка", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });


        }

        saveButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

//        intent.putExtra("mTask", mTask);
//        setResult(RESULT_OK, intent);


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

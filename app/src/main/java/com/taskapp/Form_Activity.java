package com.taskapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_);

        editTitle = findViewById(R.id.editTitle);
        description = findViewById(R.id.description);
        task = (Task) getIntent().getSerializableExtra("HomeFragmentTask");
        if (task != null) {
            editTitle.setText(task.getTitle(), TextView.BufferType.EDITABLE);
            description.setText(task.getDesc(), TextView.BufferType.EDITABLE);
        }
    }


    public void onSave(View view) {
        Map<String, Object> tasks = new HashMap<>();

        String title = editTitle.getText().toString().trim();
        String desc = description.getText().toString().trim();
        String taskId = FirebaseAuth.getInstance().getUid();
        Intent intent = new Intent();
        if (task != null) {

            task.setTitle(title);
            task.setDesc(desc);
            App.getDataBase().taskDao().update(task);
            task=new Task(title,desc);

            tasks.put("tasks",task);
            FirebaseFirestore.getInstance().collection("tasks").document(taskId)
                    .set(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Form_Activity.this,"Успешно", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Form_Activity.this,"Ошибка", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } else {

            task = new Task(title, desc);
            App.getDataBase().taskDao().insert(task);
            tasks.put("tasks",task);
            FirebaseFirestore.getInstance().collection("tasks").add(tasks)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Form_Activity.this,"Успешно", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Form_Activity.this,"Ошибка", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
//        intent.putExtra("task", task);
//        setResult(RESULT_OK, intent);
            finish();

    }


}

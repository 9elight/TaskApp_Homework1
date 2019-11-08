package com.taskapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Form_Activity extends AppCompatActivity {

    private EditText editTitle;
    private EditText description;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_);

        editTitle = findViewById(R.id.editTitle);
        description = findViewById(R.id.description);
        task = (Task) getIntent().getSerializableExtra("HomeFragmentTask");
        if (task != null){

            editTitle.setText(task.getTitle(),TextView.BufferType.EDITABLE);
            description.setText(task.getDesc(),TextView.BufferType.EDITABLE);}
        }





    public void onSave(View view) {
        String title = editTitle.getText().toString().trim();
        String desc = description.getText().toString().trim();
        Intent intent = new Intent();
        Task task = new Task(title, desc);
        intent.putExtra("task", task);
        setResult(RESULT_OK, intent);
        finish();

    }


}

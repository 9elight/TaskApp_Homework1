package com.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Form_Activity extends AppCompatActivity {
    private Button saveButton;
    private EditText aTask;
    private  EditText description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_);
       saveButton = findViewById(R.id.saveButton);
       aTask = findViewById(R.id.task);
       description = findViewById(R.id.description);
       saveButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Form_Activity.this,MainActivity.class);
               intent.putExtra("task",aTask.getText().toString());
               intent.putExtra("description",description.getText().toString());
               startActivity(intent);
               finish();
           }
       });

    }
}

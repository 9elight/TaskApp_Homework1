package com.taskapp;

import android.widget.Toast;

public class Toaster {
    public static void show(String massage){
        Toast.makeText(App.instance.getBaseContext(),massage,Toast.LENGTH_SHORT).show();
    }
}

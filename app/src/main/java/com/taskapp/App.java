package com.taskapp;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.room.Room;

import com.taskapp.room.AppDataBase;

public class App extends Application {

    public static App instance;
    public static AppDataBase dataBase;
    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        dataBase = Room.databaseBuilder(this,AppDataBase.class, "database")
                .allowMainThreadQueries().build();

    }


    public static AppDataBase getDataBase() {
        return dataBase;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}



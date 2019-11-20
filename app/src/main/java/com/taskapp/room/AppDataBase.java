package com.taskapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.taskapp.Task;


@Database(entities = {Task.class},version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TaskDao taskDao();

}

package com.taskapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.taskapp.Task;

import java.util.List;

@Dao
public interface TaskDao {
//    @Query("SELECT * FROM task")
//    List<Task> getAll();

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAll();

    @Query("SELECT * FROM task ORDER BY title")
    LiveData<List<Task>> getSortedList();


    @Update
    void update(Task task);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);






}

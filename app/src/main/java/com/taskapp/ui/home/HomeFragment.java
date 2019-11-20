package com.taskapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taskapp.App;
import com.taskapp.Form_Activity;
import com.taskapp.R;
import com.taskapp.Task;
import com.taskapp.TaskAdapter;
import com.taskapp.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private static TaskAdapter adapter;
    private static List<Task> list;

    private static int pos;
    private static List<Task> sortedList;
    private static List<Task> notSortedList;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        initList();
        return root;
    }



    private void initList() {
        list = new ArrayList<>();
////        list.add(new Task("Rahat",""));


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new TaskAdapter();
        adapter.addList(list);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onClick(int position) {
                Task task = list.get(position);
                Intent intent = new Intent(getContext(), Form_Activity.class);
                intent.putExtra("HomeFragmentTask", task);
                startActivity(intent);
                Log.d("HomeFragment", "onClick: " + task.getDesc() + task.getTitle());
                //pos = position;


//                Toast.makeText(getContext(),"pos = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Внимание");
                builder.setMessage("Вы точно хотите удалить запись");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Task task = list.get(pos);

                        App.getDataBase().taskDao().delete(task);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
        App.getDataBase().taskDao().getAll().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                notSortedList = tasks;
                list.clear();
                list.addAll(tasks);
                adapter.notifyDataSetChanged();
            }

        });
        App.getDataBase().taskDao().getSortedList().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                sortedList = tasks;
            }
        });


    }

    public static void setNotSortedList(){
        list.clear();
        list.addAll(notSortedList);
        adapter.notifyDataSetChanged();

    }

    public static void setSortedList() {

            list.clear();
            list.addAll(sortedList);
            adapter.notifyDataSetChanged();





        //        Collections.sort(list, new Comparator<Task>() {
//            @Override
//            public int compare(Task o1, Task o2) {
//
//                return o1.getTitle().compareTo(o2.getTitle());
//            }
//        });

    }



}


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == 100) {
//            task = (Task) data.getSerializableExtra("task");
//            Log.wtf("tag", "title: " + task.getTitle());
//            list.add(0,task);
//            adapter.notifyDataSetChanged();
//
//        }else if (resultCode == Activity.RESULT_OK && requestCode == 200){
//            Task task = (Task) data.getSerializableExtra("task");
//            list.set(pos,task);
//            adapter.notifyDataSetChanged();
//        }
//    }

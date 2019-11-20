package com.taskapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.taskapp.onBoard.OnBoardActivity;
import com.taskapp.ui.home.HomeFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.List;

import static com.taskapp.ui.home.HomeFragment.setNotSortedList;
import static com.taskapp.ui.home.HomeFragment.setSortedList;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean isShown = preferences.getBoolean("isShown", false);


        if (!isShown) {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(this,PhoneActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, Form_Activity.class), 100);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        String task = getIntent().getStringExtra("task");
        String description = getIntent().getStringExtra("description");
        if (task != null || description != null) {
            Log.d("tag", task + " " + description);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.exit:
                SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
                preferences.edit().putBoolean("isShown", false).apply();
                finish();
            case R.id.sort:
                if (flag == false){
                    setSortedList();
                    flag = true;
                    Log.e("tag", "onOptionsItemSelected: +" );
                }else {
                    setNotSortedList();
                    flag = false;
                    Log.e("Tag", "onOptionsItemSelected: " );
                }
                break;
            case R.id.action_sign_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Внимание!!");
                builder.setMessage("Вы точно зотите выйти с аккаунта?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
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
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//            fragment.getChildFragmentManager().getFragments().get(0).onActivityResult(requestCode,resultCode,data);
//
//    }
}

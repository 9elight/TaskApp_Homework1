package com.taskapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.taskapp.onBoard.OnBoardActivity;
import com.taskapp.ui.home.HomeFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.taskapp.ui.home.HomeFragment.setNotSortedList;
import static com.taskapp.ui.home.HomeFragment.setSortedList;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView editName;
    private TextView editEmail;
    private ImageView headerImg;
    private String name;
    private String email;
    private Uri uril;



    boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean isShown = preferences.getBoolean("isShown", false);
        name = preferences.getString("name", "No name defined");
        email = preferences.getString("email","No email defined");

        Log.e("nameEmail", "onCreate: " + name + email );





        if (!isShown) {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, PhoneActivity.class));
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

        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ProfileActivity.class), 100);
            }
        });
        String imgId = FirebaseAuth.getInstance().getUid();
        TextView editName = header.findViewById(R.id.editHeaderName);
        TextView editEmail = header.findViewById(R.id.editHeaderEmail);
        headerImg = header.findViewById(R.id.headerImage);

        setImages();
        editName.setText(name);
        editEmail.setText(email);


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
                if (flag == false) {
                    setSortedList();
                    flag = true;
                    Log.e("tag", "onOptionsItemSelected: +");
                } else {
                    setNotSortedList();
                    flag = false;
                    Log.e("Tag", "onOptionsItemSelected: ");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            editName = findViewById(R.id.editHeaderName);
            editEmail = findViewById(R.id.editHeaderEmail);

            editName.setText(data.getStringExtra("name"));
            editEmail.setText(data.getStringExtra("email"));
            setImages();


        }

    }
    private void setImages(){

        StorageReference storage = FirebaseStorage.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getUid();
        storage.child("images/" + userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this).load(uri).into(headerImg);
            }
        });
    }


//        @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//            fragment.getChildFragmentManager().getFragments().get(0).onActivityResult(requestCode,resultCode,data);
//
//    }
}

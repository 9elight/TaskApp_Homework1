package com.taskapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private final int GALLERY_REQUEST = 10;
    private EditText editName;
    private EditText editEmail;
    private ImageView headerImg;
    private Uri selectedImage;
    private ProgressBar loading;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editMail);
        headerImg = findViewById(R.id.headerImg);
        loading = findViewById(R.id.progressBar);
        loadData2();
    }
    private void loadData(){
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    String name = task.getResult().getString("name");
                    String email = task.getResult().getString("email");
                    editName.setText(name);
                    editEmail.setText(email);

                }
            }
        });
    }
    private  void loadData2() {
        final String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users").document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            editName.setText(name);
                            editEmail.setText(email);
//
                        }

                    }
                });
        StorageReference storage = FirebaseStorage.getInstance().getReference();

        storage.child("images/" + userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(ProfileActivity.this).load(uri).into(headerImg);
                    }
                });
    }

    public void onClick(View view) {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email",email);

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users").document(userId).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this,"Успешно", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ProfileActivity.this,"Ошибка", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.apply();
//        Bitmap image = imageView.getDrawingCache();
//        Bundle extras = new Bundle();
//        extras.putParcelable("imageBitmap",image);
//        imageView.buildDrawingCache();
//        intent.putExtras(extras);

        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("email",email);

        setResult(RESULT_OK,intent);
        finish();



    }

    public void onImgClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            imageView = findViewById(R.id.headerImg);
            selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
            upload(data.getData());
        }
//        Bitmap bitmap = null;
//        ImageView imageView = findViewById(R.id.headerImg);
//        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST){
//            Uri selectedImage = data.getData();
//
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            imageView.setImageBitmap(bitmap);
//        }

    }

    private void upload(Uri uri) {

        loading.setVisibility(View.VISIBLE);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference reference = storage.getReference().child("images/" + userId);
        UploadTask uploadTask = reference.putFile(uri);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Toaster.show("Успешно");
                    loading.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}

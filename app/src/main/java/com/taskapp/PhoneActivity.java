package com.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {
    private EditText editPhone;
    private EditText editSmsCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private Button confirmCodeButton;
    private Button continueButton;
    private String verificationId;
    private boolean isCodeSent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        editPhone = findViewById(R.id.editPhone);
        editSmsCode = findViewById(R.id.editSmsCode);
        confirmCodeButton = findViewById(R.id.confirmButton);
        continueButton = findViewById(R.id.continueButton);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull final PhoneAuthCredential phoneAuthCredential) {

//                Log.e("tag", "onVerificationCompleted: " + phoneAuthCredential.getSmsCode() );
//                if (editSmsCode.equals(phoneAuthCredential.getSmsCode())){
//                    Toast.makeText(PhoneActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
//                }
                    if (!isCodeSent){
                        signIn(phoneAuthCredential);
                    }


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("tag", "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationId = s;
                isCodeSent = true;


            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("tag", "onComplete: " + task.getResult());
                            Toast.makeText(PhoneActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.e("tag", "Error: " + task.getException().getMessage());
                            Toast.makeText(PhoneActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onClickPhone(View view) {

            editPhone.setVisibility(View.GONE);
            editSmsCode.setVisibility(View.VISIBLE);
            String phone = editPhone.getText().toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60,
                    TimeUnit.SECONDS, this, callbacks);
            continueButton.setVisibility(View.GONE);
            confirmCodeButton.setVisibility(View.VISIBLE);










    }


    public void onConfirmClick(View view) {
        String code = editSmsCode.getText().toString();
        PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(verificationId,code);
        signIn(phoneCredential);
    }
}

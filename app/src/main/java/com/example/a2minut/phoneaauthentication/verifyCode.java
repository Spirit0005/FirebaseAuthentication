package com.example.a2minut.phoneaauthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyCode extends AppCompatActivity {


    private String phoneNum, verificationID;
    private Button verify_btn;
    private FirebaseAuth mAuth;
    private EditText code1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);


        mAuth = FirebaseAuth.getInstance();
        phoneNum = getIntent().getStringExtra("Phonenumber");
        code1 = findViewById(R.id.code1);
        verify_btn = findViewById(R.id.verify_btn);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = code1.getText().toString().trim();

                if(code != null){
                    verifyCode(code);
                }else {
                    Toast.makeText(verifyCode.this,"Error Wrong code",Toast.LENGTH_LONG).show();

                }

            }
        });

        sendverificationCode(phoneNum);

    }

    private void verifyCode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
       // signInWithCredential(credential);

        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                          //  updateUI(user);
                        } else {
                           // Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(verifyCode.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isComplete()){

                    Intent intent = new Intent(new Intent(verifyCode.this,Home.class));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    Toast.makeText(verifyCode.this,"Approved",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(verifyCode.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendverificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(verifyCode.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
}

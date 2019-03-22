package com.example.a2minut.phoneaauthentication;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText mPhone,email,pass;
    private String phoneNum = "0";
    public static  FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mAuth = FirebaseAuth.getInstance();

        mPhone = findViewById(R.id.phone2);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button2);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneNum = mPhone.getText().toString();
                if(phoneNum.isEmpty()){
                    Toast.makeText(login.this,"Input Number",Toast.LENGTH_LONG).show();
                    return;
                }

                createUser();

            }
        });

    }

    private void createUser(){

        String emailText =email.getText().toString();
        String passText = pass.getText().toString();



        mAuth.createUserWithEmailAndPassword(emailText,passText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Intent intent = new Intent(login.this,verifyCode.class);
                    intent.putExtra("Phonenumber", phoneNum);
                    startActivity(intent);
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success");
                    //FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                   // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            Intent intent = new Intent(new Intent(this,Home.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}

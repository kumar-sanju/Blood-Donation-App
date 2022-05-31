package com.smart.blooddonation.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.smart.blooddonation.R;
import com.smart.blooddonation.Register.RegisterIActivity;

public class LoginScreenActivity extends AppCompatActivity {

    
    com.google.android.material.textfield.TextInputEditText Email,Pass;
    com.google.android.material.button.MaterialButton loginBtn;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        
        Email = findViewById(R.id.emailLogin);
        Pass = findViewById(R.id.passLogin);
        loginBtn = findViewById(R.id.loginBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateTheEmail();
            }
        });
    }

    private void validateTheEmail() {
        String email = Email.getText().toString();
        String password = Pass.getText().toString();

        if (Email.getText().toString().isEmpty()) {
            Email.setError("Field Required...");
        }
        else if (Pass.getText().toString().isEmpty()) {
            Pass.setError("Field Required...");
        }
        else {
            LoginNow(email, password);
        }
    }

    public void OpenRegisterActivity(View view) {
        startActivity(new Intent(LoginScreenActivity.this, RegisterIActivity.class));
    }


    public void LoginNow(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.hide();
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginScreenActivity.this, HomeActivity.class));
                    }else {
                        Toast.makeText(LoginScreenActivity.this,"Please verify your email id.",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginScreenActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


//        if(!Email.getText().toString().isEmpty() && !Pass.getText().toString().isEmpty()){
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email.getText().toString(),Pass.getText().toString())
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()){
//                                startActivity(new Intent(LoginScreenActivity.this, SplashScreen.class));
//                            }else {
//                                Toast.makeText(LoginScreenActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
    }
}
package com.smart.blooddonation.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.smart.blooddonation.R;

import java.util.HashMap;
import java.util.UUID;

public class RegisterIActivity extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText fName,lName,email,pass;
    com.google.android.material.button.MaterialButton nextToI;
    com.google.android.material.progressindicator.LinearProgressIndicator progressIndicator;
    FirebaseAuth fAuth;
    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_i);
        initializeComponents();
    }

    private void initializeComponents() {
        fAuth = FirebaseAuth.getInstance();

        fName = findViewById(R.id.fNameInput);
        lName = findViewById(R.id.lNameInput);
        email = findViewById(R.id.emailInput);
        pass = findViewById(R.id.passInput);
        nextToI = findViewById(R.id.nextButtonI);
        progressIndicator = findViewById(R.id.iProgressbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
    }

    public void nextRegisterPage(View view) {
        String f_name,l_name,emailText,passText;
        f_name = fName.getText().toString();
        l_name = lName.getText().toString();
        emailText = email.getText().toString().toLowerCase();
        passText = pass.getText().toString();

        if(f_name.isEmpty()){
            fName.setError("Field Required...");
        }
        if(emailText.isEmpty()){
            email.setError("Field Required...");
        }
        if (l_name.isEmpty()){
            lName.setError("Field Required...");
        }
        if(passText.isEmpty()){
            pass.setError("Field Required...");
        }

        if(! f_name.isEmpty() && ! l_name.isEmpty() && ! emailText.isEmpty() && ! passText.isEmpty()){
            RegisterUser(f_name,l_name,emailText,passText);
        }

    }

    private void RegisterUser(String f_name, String l_name, String emailText, String passText) {

        firebaseAuth.createUserWithEmailAndPassword(emailText,passText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.hide();
                if(task.isSuccessful()) {
                    Task<AuthResult> newTask = task;
//                    String uniqueId = UUID.randomUUID().toString();

                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterIActivity.this, "User registered successfully.Please verify your email...", Toast.LENGTH_SHORT).show();
                                addToDatabase(newTask.getResult().getUser().getUid(),f_name,l_name,emailText,passText);
                            }else {
                                Toast.makeText(RegisterIActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterIActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

//        fAuth.createUserWithEmailAndPassword(emailText,passText)
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        // send verification link
//                        FirebaseUser fuser = fAuth.getCurrentUser();
//                        assert fuser != null;
//                        fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(RegisterIActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
//                                addToDatabase(task.getResult().getUser().getUid(),f_name,l_name,emailText);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(RegisterIActivity.this, "Email not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }else {
//                        Toast.makeText(RegisterIActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
    }

    private void addToDatabase(String uid, String f_name, String l_name, String emailText, String passText) {

        HashMap<String,String>values = new HashMap<>();
        values.put("FName",f_name);
        values.put("LName",l_name);
        values.put("Email",emailText);
        values.put("UID",uid);
        values.put("Password",passText);
        values.put("Step","1");
        values.put("Visible","False");
        values.put("RequestBlood","False");
        values.put("State","None");
        values.put("District","None");
        values.put("Tehsil","None");
        values.put("Village","None");
        values.put("Mobile","None");
        values.put("BloodGroup","None");

        FirebaseDatabase.getInstance().getReference("Donors")
                .child(uid).setValue(values).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        startActivity(new Intent(RegisterIActivity.this,RegisterIIActivity.class));
                    }
                });


    }
}
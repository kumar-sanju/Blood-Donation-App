package com.smart.blooddonation.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.smart.blooddonation.Activity.LoginScreenActivity;
import com.smart.blooddonation.R;

import java.util.HashMap;

public class Register4Activity extends AppCompatActivity {

    AutoCompleteTextView bloodgrp;

    com.google.android.material.textfield.TextInputEditText mobile,textVerification;
    com.google.android.material.button.MaterialButton submit;

    boolean isVerified = false, isSubmit = false;
    private CountryCodePicker country;
    String id, countryCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        initializeComponents();

        String[] bloodGroups = getResources().getStringArray(R.array.blood_groups);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,bloodGroups);

        bloodgrp.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToDatabase();
            }
        });
    }

    private void initializeComponents() {
        country = findViewById(R.id.country);
        bloodgrp = findViewById(R.id.bloodGrpDropDown);
        mobile = findViewById(R.id.mobileEditText);
        textVerification = findViewById(R.id.verificationText);
        submit = findViewById(R.id.btnVerifySubmit);
    }

    private void addToDatabase() {
        countryCode = country.getSelectedCountryCode();

        if (mobile.getText().toString().isEmpty()) {
            mobile.setError("Enter Mobile Number!");
        }
        else if (bloodgrp.getText().toString().equals("Blood Groups")) {
            Toast.makeText(Register4Activity.this, "Please Select Blood Groups !!!", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String,Object> values = new HashMap<>();
            values.put("Step","Done");
            values.put("Mobile",countryCode + " " +mobile.getText().toString());
            values.put("BloodGroup",bloodgrp.getText().toString());
            values.put("Visible","True");
            FirebaseDatabase.getInstance().getReference("Donors/"+ FirebaseAuth.getInstance().getUid())
                    .updateChildren(values)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Register4Activity.this, LoginScreenActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(new Intent(Register4Activity.this, LoginScreenActivity.class));
                                Register4Activity.this.finish();
                            }else {
                                Toast.makeText(Register4Activity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    String selected_country_code;
    public void onCountryPickerClick(View view) {
        // Country code select on click listener
        country.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                selected_country_code = country.getSelectedCountryCodeWithPlus();
            }
        });
    }

    //    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential credential) {
//            textVerification.setHint("Verified ! ✔");
//            addToDatabase();
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//
//            if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                textVerification.setHint("Failed!");
//            } else if (e instanceof FirebaseTooManyRequestsException) {
//                textVerification.setHint("Message Quota Exceeded!\nTry Again After few Hours!");
//            }
//
//            mobile.setEnabled(true);
//
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String verificationId,
//                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
//
//            textVerification.setHint("Enter OTP!");
//            submit.setText("Submit");
//            id = verificationId;
//            isSubmit = true;
//
//        }
//
//    };


//    public void verifyAndSubmit(View view) {
//
//        mobile.setEnabled(false);
//        if(!isSubmit) {
//            if (!isVerified && !mobile.getText().toString().isEmpty() && !bloodgrp.getText().toString().isEmpty()) {
//                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
//                        .setPhoneNumber("+91" + mobile.getText().toString())
//                        .setTimeout(60L, TimeUnit.SECONDS)
//                        .setActivity(Register4Activity.this)
//                        .setCallbacks(mCallbacks)
//                        .build();
//
//                PhoneAuthProvider.verifyPhoneNumber(options);
//                textVerification.setHint("Verifying...");
//            }
//            if (mobile.getText().toString().isEmpty()) {
//                mobile.setError("Enter Mobile Number!");
//            }
//        }else {
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,textVerification.getText().toString());
//            FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()){
//                                addToDatabase();
//                            }else {
//                                Toast.makeText(Register4Activity.this, "Error!\n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                submit.setText("Verify");
//                                mobile.setEnabled(true);
//                                textVerification.setHint("Not Verified!");
//                                isVerified = false;
//                                isSubmit = false;
//                            }
//                        }
//                    });
//        }
//
//
//    }
}
package com.example.remotelightoperator.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.firebase.UserConfigurationStoreUtils;
import com.example.remotelightoperator.model.AuthenticationData;
import com.example.remotelightoperator.welcome.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RegistrationActivity extends Activity implements View.OnClickListener {

    private FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_view);

        auth = FirebaseAuth.getInstance();
        auth.signOut();
        handleAuthenticatedUser();

        ((Button) findViewById(R.id.editRegisterButton)).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View arg0) {

        EditText email = (EditText) findViewById(R.id.signUpEmailEditText);
        EditText password = (EditText) findViewById(R.id.signUpPasswordEditText);

        if (arg0 == findViewById(R.id.editRegisterButton)) {
            AuthenticationData authenticationData = new AuthenticationData(email.getText().toString(),
                    password.getText().toString());
           signUp(authenticationData);
        }
    }

    private void signUp(AuthenticationData authData) {
        auth.createUserWithEmailAndPassword(authData.getEmail(), authData.getPassword())
                .addOnCompleteListener(this, new SignUpOnCompleteListener());
    }

    private void handleAuthenticatedUser() {
        if(auth.getCurrentUser() != null && !auth.getCurrentUser().isAnonymous()) {
            redirectToWelcomeActivity();
        }
    }

    private void handleRegisterSuccess() {
        Log.wtf("TestRegister","Registration success");
        Log.wtf("TEST",auth.getUid());
        redirectToWelcomeActivity();
    }

    private void handleRegisterFailure() {
        Log.wtf("TestRegistration","Registration failure");
        Toast.makeText(this, "Registration failed.",
                Toast.LENGTH_SHORT).show();
    }

    private void redirectToWelcomeActivity() {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    private class SignUpOnCompleteListener implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid();
                UserConfigurationStoreUtils.addUserConfigurationQueryTask(uid)
                        .addOnCompleteListener(new addUserConfigurationOnCompleteListener())
                        .addOnFailureListener(new addUserConfigurationOnFailureListener());
            } else  {
                handleRegisterFailure();
            }
        }
    }
    private class addUserConfigurationOnCompleteListener implements OnCompleteListener<Void> {
        @Override
        public void onComplete(@NonNull Task task) {
            Log.wtf("TestRegistration", "Succesfully Added configuration");
            handleRegisterSuccess();
        }
    }

    private class addUserConfigurationOnFailureListener implements OnFailureListener {

        @Override
        public void onFailure(@NonNull Exception e) {
            Log.wtf("TestRegistration", "Failture while adding Added configuration");
            handleRegisterFailure();
        }
    }
}

package com.example.remotelightoperator.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.welcome.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

public class RegistrationActivity extends Activity implements View.OnClickListener {

    private FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_view);

        //TODO Tutaj se dajesz kurwa wszystko co tam kurwa potrzebujesz do rejestracji. Looknij looginActivity
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onClick(View arg0) {

        EditText email = (EditText) findViewById(R.id.emailEditText);
        EditText password = (EditText) findViewById(R.id.passwordEditText);

        //TODO AuthenticationDATA like password etc. No co tam kurwa mordo bedziesz potrzebował do rejestracji to tutaj.

        if (arg0 == findViewById(R.id.editLoginButton)) {
           //TODO registerMechanismOnFirebase
        }

    }
    private void handleAuthenticatedUser() {
        if(auth.getCurrentUser() != null && !auth.getCurrentUser().isAnonymous()) {
            redirectToWelcomeActivity();
        }
    }

    //TODO metoda do wysylajaca z obiektu auth do tego chuja firebase'a dane do rejestracji.

    private void handleRegisterSuccess() {
        Log.wtf("TestRegister","Registration success");
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

    private class LoginOnCompleteListener implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                handleRegisterSuccess();
            } else  {
                handleRegisterFailure();
            }
        }
    }
}

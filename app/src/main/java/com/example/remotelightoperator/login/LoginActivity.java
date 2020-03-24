package com.example.remotelightoperator.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.remotelightoperator.MainActivity;
import com.example.remotelightoperator.R;
import com.example.remotelightoperator.model.AuthenticationData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

public class LoginActivity extends Activity implements View.OnClickListener {

    private FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        auth = FirebaseAuth.getInstance();
        handleAuthenticatedUser();

        ((Button) findViewById(R.id.editLoginButton)).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onClick(View arg0) {

        EditText email = (EditText) findViewById(R.id.emailEditText);
        EditText password = (EditText) findViewById(R.id.passwordEditText);

        AuthenticationData authenticationData = new AuthenticationData
                (email.getText().toString(), password.getText().toString());

        if (arg0 == findViewById(R.id.editLoginButton)) {
            signIn(authenticationData);
        }

    }
    private void handleAuthenticatedUser() {
        if(auth.getCurrentUser() != null && !auth.getCurrentUser().isAnonymous()) {
            redirectToMainActivity();
        }
    }

    private void signIn(AuthenticationData authenticationData) {
        auth.signInWithEmailAndPassword(
                authenticationData.getEmail(),
                authenticationData.getPassword()
        ).addOnCompleteListener(this, new LoginOnCompleteListener());
    }

    private void handleSingInSuccess() {
        Log.wtf("TestLogin","Login success");
        redirectToMainActivity();
    }

    private void  handleSingInFailure() {
        Log.wtf("TestLogin","Login failure");
        Toast.makeText(this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    private void redirectToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private class LoginOnCompleteListener implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                handleSingInSuccess();
            } else  {
                handleSingInFailure();
            }
        }
    }
}

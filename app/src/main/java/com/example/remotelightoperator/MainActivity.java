package com.example.remotelightoperator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.remotelightoperator.model.AuthenticationData;
import com.example.remotelightoperator.plantchooser.PlantListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        handleAuthenticatedUser();

        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.plantListButton)).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onClick(View arg0) {
        // TODO: get it from login inputs
        AuthenticationData authenticationData = new AuthenticationData
                ("mail@mai.mail", "Type pass in here");

        if (arg0 == findViewById(R.id.plantListButton)) {
            signIn(authenticationData);
        }

    }

    private void handleAuthenticatedUser() {
        if(auth.getCurrentUser() != null && !auth.getCurrentUser().isAnonymous()) {
            redirectToPlantList();
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
        redirectToPlantList();
    }

    private void  handleSingInFailure() {
        Log.wtf("TestLogin","Login failure");
        Toast.makeText(this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    private void redirectToPlantList () {
        startActivity(new Intent(this, PlantListActivity.class));
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

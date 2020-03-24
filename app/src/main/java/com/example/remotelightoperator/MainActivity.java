package com.example.remotelightoperator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.remotelightoperator.plantchooser.PlantListActivity;
import com.example.remotelightoperator.welcome.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity implements View.OnClickListener {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.plantListButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.logoutButton)).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.plantListButton)) {
            startActivity(new Intent(this, PlantListActivity.class));
        }
        if (v == findViewById(R.id.logoutButton)) {
            auth.signOut();
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }
}

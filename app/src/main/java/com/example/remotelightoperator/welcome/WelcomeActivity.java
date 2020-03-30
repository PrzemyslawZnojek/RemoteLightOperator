package com.example.remotelightoperator.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.login.LoginActivity;
import com.example.remotelightoperator.registration.RegistrationActivity;

public class WelcomeActivity extends Activity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.hello_view);

        ((Button) findViewById(R.id.buttonLogin)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonRegister)).setOnClickListener(this);
    }

    public void onClick(View arg0) {

        if (arg0 == findViewById(R.id.buttonLogin)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (arg0 == findViewById(R.id.buttonRegister)) {
            startActivity(new Intent(this, RegistrationActivity.class));
        }
    }
}

package com.example.remotelightoperator.plantdescription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.model.UserConfiguration;
import com.example.remotelightoperator.plantconfigurator.PlantConfiguratorActivity;

public class MyPlantsDescriptionActivity extends Activity implements View.OnClickListener {
    UserConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_plant_description);

        Intent intent = this.getIntent();
        configuration = (UserConfiguration) intent.getSerializableExtra("UserConfiguration");

        TextView name = (TextView) findViewById(R.id.name);
        TextView description = (TextView) findViewById(R.id.description);
        TextView irradiationTime = (TextView) findViewById(R.id.irradiationTime);

        name.setText(configuration.getPlantName());
        description.setText(configuration.getDescription());
        irradiationTime.setText(String.valueOf(configuration.getIrradiationTime()).concat(" [s]"));

        ((Button) findViewById(R.id.configureLight)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.configureLight)) {

            Intent intent = new Intent(this, PlantConfiguratorActivity.class);
            intent.putExtra("UserConfiguration", configuration);
            startActivity(intent);

        }
    }
}

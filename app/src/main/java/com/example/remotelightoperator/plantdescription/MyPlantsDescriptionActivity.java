package com.example.remotelightoperator.plantdescription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.model.PlantTemplate;

public class MyPlantsDescriptionActivity  extends Activity implements View.OnClickListener  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_plant_description);

        Intent intent = this.getIntent();
        PlantTemplate plantTemplate = (PlantTemplate) intent.getSerializableExtra("PlantTemplate");

        TextView name = (TextView) findViewById(R.id.name);
        TextView description = (TextView) findViewById(R.id.description);
        TextView irradiationTime = (TextView) findViewById(R.id.irradiationTime);

        name.setText(plantTemplate.getName());
        description.setText(plantTemplate.getDescription());
        irradiationTime.setText(String.valueOf(plantTemplate.getIrradiationTime()));

        ((Button) findViewById(R.id.configureLight)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.configureLight)) {
            Toast.makeText(this, "Not ready Yet.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

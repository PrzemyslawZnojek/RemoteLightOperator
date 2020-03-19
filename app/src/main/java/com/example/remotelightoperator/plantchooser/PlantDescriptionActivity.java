package com.example.remotelightoperator.plantchooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.model.PlantTemplate;

public class PlantDescriptionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_description);

        Intent intent = this.getIntent();
        PlantTemplate plantTemplate = (PlantTemplate) intent.getSerializableExtra("PlantTemplate");

        TextView name = (TextView) findViewById(R.id.name);
        TextView description = (TextView) findViewById(R.id.description);
        TextView irradiationTime = (TextView) findViewById(R.id.irradiationTime);
        TextView rate = (TextView) findViewById(R.id.rate);
        TextView rateCount = (TextView) findViewById(R.id.rateCount);

        name.setText(plantTemplate.getName());
        description.setText(plantTemplate.getDescription());
        irradiationTime.setText(String.valueOf(plantTemplate.getIrradiationTime()));
        rate.setText(String.valueOf(plantTemplate.getRate()));
        rateCount.setText(String.valueOf(plantTemplate.getRateCount()));
    }
}

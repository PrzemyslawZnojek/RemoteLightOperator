package com.example.remotelightoperator.plantdescription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.model.PlantTemplate;
import com.example.remotelightoperator.myplants.MyPlantsActivity;

public class PlantFullDescriptionActivity extends Activity implements View.OnClickListener {

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

        ((Button) findViewById(R.id.addToMyPlants)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.addToMyPlants)) {
            //TODO Dej kurwie miodu i dej tu funkcje do dodawania
            startActivity(new Intent(this, MyPlantsActivity.class));
        }
    }
}

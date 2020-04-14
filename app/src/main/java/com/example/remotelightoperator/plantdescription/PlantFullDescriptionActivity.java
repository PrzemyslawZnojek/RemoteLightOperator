package com.example.remotelightoperator.plantdescription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.firebase.PlantTemplateStoreUtils;
import com.example.remotelightoperator.firebase.UserConfigurationStoreUtils;
import com.example.remotelightoperator.model.PlantTemplate;
import com.example.remotelightoperator.myplants.MyPlantsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class PlantFullDescriptionActivity extends Activity implements View.OnClickListener {

    private PlantTemplate plantTemplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_description);

        Intent intent = this.getIntent();
        plantTemplate = (PlantTemplate) intent.getSerializableExtra("PlantTemplate");

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


        // ---BACKEND DEMO---
        //TODO: REMOVE
        PlantTemplateStoreUtils.rateTemplate(5, plantTemplate)
                .addOnSuccessListener(new RatePlantSuccessListener())
                .addOnFailureListener(new RatePlantFailureListener());

        PlantTemplate templateToAdd = new PlantTemplate();
        templateToAdd.setName("Dodany");
        templateToAdd.setDescription("Test opis");
        templateToAdd.setIrradiationTime(11111);
        templateToAdd.setPlantID(9);
        PlantTemplateStoreUtils.addPlantTemplate(templateToAdd)
            .addOnSuccessListener(new AddPlantSuccessListener());

        // ---DEMO END---
        ((Button) findViewById(R.id.addToMyPlants)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.addToMyPlants)) {
            UserConfigurationStoreUtils.updateUserConfigurationPlantParamsQueryTask(plantTemplate)
                    .addOnSuccessListener(new UpdateConfigurationSuccessListener())
                    .addOnFailureListener(new UpdateConfigurationOnFailureListener());
        }
    }

    private class UpdateConfigurationSuccessListener implements OnSuccessListener<Void> {

        @Override
        public void onSuccess(Void aVoid) {
            Log.wtf("TEST UPDATE CONFIGURATION", "Update finished");
            Toast.makeText(PlantFullDescriptionActivity.this, "Update finished.",
                    Toast.LENGTH_SHORT).show();
            //TODO: FIX KRURWA z MY PLANTS
            startActivity(new Intent(PlantFullDescriptionActivity.this, MyPlantsActivity.class));
        }
    }

    private class UpdateConfigurationOnFailureListener implements OnFailureListener {

        @Override
        public void onFailure(@NonNull Exception e) {
            Log.wtf("TEST UPDATE CONFIGURATION", "Update finished");
            Toast.makeText(PlantFullDescriptionActivity.this, "Update failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class RatePlantSuccessListener implements OnSuccessListener<Void> {
        @Override
        public void onSuccess(Void v) {
            Log.wtf("TEST Rate PLANT", "Rate finished");
            Toast.makeText(PlantFullDescriptionActivity.this, "Rating finished for plant finished.",
                    Toast.LENGTH_SHORT).show();
           }
    }

    private class RatePlantFailureListener implements OnFailureListener {

        @Override
        public void onFailure(@NonNull Exception e) {
            Log.wtf("TEST RATE", "Rate finished");
            Log.wtf("TEST RATE", e.getMessage());
            Toast.makeText(PlantFullDescriptionActivity.this, "Rate failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class AddPlantSuccessListener implements OnSuccessListener<DocumentReference> {
        @Override
        public void onSuccess(DocumentReference v) {
            Log.wtf("TEST ADD PLANT", "Adding finished");
            Toast.makeText(PlantFullDescriptionActivity.this, "Adding finished for plant finished.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

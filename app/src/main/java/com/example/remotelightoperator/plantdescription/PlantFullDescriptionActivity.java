package com.example.remotelightoperator.plantdescription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.firebase.PlantTemplateStoreUtils;
import com.example.remotelightoperator.firebase.UserConfigurationStoreUtils;
import com.example.remotelightoperator.model.PlantTemplate;
import com.example.remotelightoperator.myplants.MyPlantsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.NonNull;

public class PlantFullDescriptionActivity extends Activity {

    private PlantTemplate plantTemplate;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_description);

        auth = FirebaseAuth.getInstance();

        Intent intent = this.getIntent();
        plantTemplate = (PlantTemplate) intent.getSerializableExtra("PlantTemplate");

        TextView name = (TextView) findViewById(R.id.name);
        TextView description = (TextView) findViewById(R.id.description);
        TextView irradiationTime = (TextView) findViewById(R.id.irradiationTime);
        TextView rate = (TextView) findViewById(R.id.rate);
        TextView rateCount = (TextView) findViewById(R.id.rateCount);

        name.setText(plantTemplate.getName());
        description.setText(plantTemplate.getDescription());
        irradiationTime.setText(String.valueOf(plantTemplate.getIrradiationTime()).concat(" [s]") );
        rate.setText(String.valueOf(plantTemplate.getRate()));
        rateCount.setText(String.valueOf((int)plantTemplate.getRateCount()));

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        TextView ratingText = (TextView) findViewById(R.id.rateText);

        boolean couldBeRated = PlantTemplateStoreUtils.couldBeRated(plantTemplate);
        if(!couldBeRated){
            ratingBar.setRating((float) PlantTemplateStoreUtils.getUserRate(auth.getUid(), plantTemplate));
            ratingBar.setEnabled(false);
            ratingText.setEnabled(false);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                PlantTemplateStoreUtils.rateTemplate((int)rating, plantTemplate)
                        .addOnSuccessListener(new RatePlantSuccessListener())
                        .addOnFailureListener(new RatePlantFailureListener());

                finish();
                startActivity(getIntent());
            }
        });

        Button button = findViewById(R.id.addToMyPlants);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserConfigurationStoreUtils.updateUserConfigurationPlantParamsQueryTask(plantTemplate)
                        .addOnSuccessListener(new UpdateConfigurationSuccessListener())
                        .addOnFailureListener(new UpdateConfigurationOnFailureListener());
            }
        });


    }

    private class UpdateConfigurationSuccessListener implements OnSuccessListener<Void> {

        @Override
        public void onSuccess(Void aVoid) {
            Log.wtf("TEST UPDATE CONFIGURATION", "Update finished");
            Toast.makeText(PlantFullDescriptionActivity.this, "Update finished.",
                    Toast.LENGTH_SHORT).show();
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

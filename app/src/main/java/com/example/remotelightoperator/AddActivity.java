package com.example.remotelightoperator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.remotelightoperator.firebase.PlantTemplateStoreUtils;
import com.example.remotelightoperator.model.PlantTemplate;
import com.example.remotelightoperator.plantdescription.PlantFullDescriptionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name;
    EditText irradiation_time;
    EditText decription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = (EditText) findViewById(R.id.add_activity_name);
        irradiation_time = (EditText) findViewById(R.id.add_activity_irradiation_time);
        decription = (EditText) findViewById(R.id.add_activity_description);
        ((Button) findViewById(R.id.add_activity_save_button)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.add_activity_save_button)) {
            if(name.length() == 0 || decription.length() == 0 || irradiation_time.length() == 0) {
                return;
            }
                    PlantTemplate templateToAdd = new PlantTemplate();
            templateToAdd.setName(name.getText().toString());
            templateToAdd.setDescription(decription.getText().toString());
            templateToAdd.setIrradiationTime(Integer.parseInt(irradiation_time.getText().toString()));
            templateToAdd.setPlantID(1);
            PlantTemplateStoreUtils.addPlantTemplate(templateToAdd)
                    .addOnSuccessListener(new OnAddedListener())
                    .addOnFailureListener(new OnAddFailureListener());
        }
    }

    private class OnAddedListener implements OnSuccessListener<DocumentReference> {

        @Override
        public void onSuccess(DocumentReference documentReference) {
            Log.wtf("ADD PLANT", "Adding finished");
            Toast.makeText(AddActivity.this, "Adding finished for plant finished.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class OnAddFailureListener implements OnFailureListener {

        @Override
        public void onFailure(@NonNull Exception e) {
            Log.wtf("ADD PLANT", "Adding finished " + e.getMessage());
            Toast.makeText(AddActivity.this, "Adding failure.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

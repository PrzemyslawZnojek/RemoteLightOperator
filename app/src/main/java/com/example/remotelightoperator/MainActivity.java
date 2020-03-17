package com.example.remotelightoperator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.remotelightoperator.firestore.PlantTemplateStoreUtils;
import com.example.remotelightoperator.model.PlantTemplate;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exampleGetPlantTemplate();
    }
    // TODO: Remove this method after creating a documentation.
    private void exampleGetPlantTemplate() {
        Task<QuerySnapshot> queryTask = PlantTemplateStoreUtils.getAllPlantTemplatesQueryTask();
        queryTask.addOnSuccessListener(genetateOnSuccess());
        queryTask.addOnCanceledListener(generateOnCanceled());
        queryTask.addOnFailureListener(genetateOnFailture());

    }

    // TODO: Remove this method after creating a documentation
    private OnSuccessListener<QuerySnapshot> genetateOnSuccess() {
        return new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<PlantTemplate> plants = PlantTemplateStoreUtils
                        .mapSnapshotsToPlantTemplates(queryDocumentSnapshots.getDocuments());
                for (PlantTemplate plant : plants) {
                    Log.wtf("FirestoreTests: ", plant.toString());
                }
            }
        };
    }

    // TODO: Remove this method after creating a documentation
    private OnFailureListener genetateOnFailture() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.wtf("FirestoreTest", String.format("Exception occured %s", e.toString()));
            }
        };
    }

    // TODO: Remove this method after creating a documentation
    private OnCanceledListener generateOnCanceled() {
        return new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.wtf("FirestoreTest", "Operation canceled");
            }
        };
    }
}

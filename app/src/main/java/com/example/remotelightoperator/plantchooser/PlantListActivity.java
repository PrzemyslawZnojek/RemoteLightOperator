package com.example.remotelightoperator.plantchooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.firestore.PlantTemplateStoreUtils;
import com.example.remotelightoperator.model.PlantTemplate;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;

public class PlantListActivity extends Activity{

    List<PlantTemplate> plants;
    PlantChooserAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_chooser);
        getPlantTemplate();

    }

    // TODO: Remove this method after creating a documentation.
    private void getPlantTemplate() {
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
                ListView listView = (ListView) findViewById(R.id.listView);
                plants = PlantTemplateStoreUtils
                        .mapSnapshotsToPlantTemplates(queryDocumentSnapshots.getDocuments());
                adapter = new PlantChooserAdapter(plants, PlantListActivity.this);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent newIntent = new Intent(PlantListActivity.this, PlantDescriptionActivity.class);
                        newIntent.putExtra("PlantTemplate", plants.get(position));
                        startActivity(newIntent);
                    }
                });

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

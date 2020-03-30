package com.example.remotelightoperator.myplants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.firebase.PlantTemplateStoreUtils;
import com.example.remotelightoperator.model.PlantTemplate;
import com.example.remotelightoperator.plantdescription.MyPlantsDescriptionActivity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;

public class MyPlantsActivity  extends Activity {
    List<PlantTemplate> plants;
    MyPlantsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_plants_view);
        getPlantTemplate();

    }

    private void getPlantTemplate() {
        Task<QuerySnapshot> queryTask = PlantTemplateStoreUtils.getMyPlantsQueryTask();
        queryTask.addOnSuccessListener(generateOnSuccess());
        queryTask.addOnCanceledListener(generateOnCanceled());
        queryTask.addOnFailureListener(genetateOnFailture());

    }

    // TODO: Remove this method after creating a documentation
    private OnSuccessListener<QuerySnapshot> generateOnSuccess() {
        return new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ListView listView = (ListView) findViewById(R.id.listView);
                plants = PlantTemplateStoreUtils
                        .mapSnapshotsToPlantTemplates(queryDocumentSnapshots.getDocuments());
                adapter = new MyPlantsAdapter(plants, MyPlantsActivity.this);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent newIntent = new Intent(MyPlantsActivity.this, MyPlantsDescriptionActivity.class);
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

package com.example.remotelightoperator.firebase;

import com.example.remotelightoperator.model.PlantTemplate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO: Consider replacing this class with DAO, synchronized singelton(?)
public class PlantTemplateStoreUtils {
    private static final String COLLECTION_NAME = "PlantTemplate";

    public static Task<QuerySnapshot>  getAllPlantTemplatesQueryTask() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        return  firestore
                .collection(COLLECTION_NAME)
                .get();
    }

    public static List<PlantTemplate> mapSnapshotsToPlantTemplates(Collection<DocumentSnapshot> snapshots) {
        List<PlantTemplate> plantTemplates  = new ArrayList<>();
        for (DocumentSnapshot snap : snapshots) {
            plantTemplates.add(snap.toObject(PlantTemplate.class));
        }
        return plantTemplates;
    }

}

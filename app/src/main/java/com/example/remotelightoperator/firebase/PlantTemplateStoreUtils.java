package com.example.remotelightoperator.firebase;

import com.example.remotelightoperator.model.PlantTemplate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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

    public static Task<DocumentReference> addPlantTemplate(PlantTemplate template) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        template.setRate(0);
        template.setRateCount(0);
        return  firestore
                .collection(COLLECTION_NAME)
                .add(template);
    }

    public static Task<Void> rateTemplate(int rate, PlantTemplate templateToRate) {
        if(rate < 1 || rate > 5) {
            throw  new IllegalArgumentException("Rate must be in range of 1 - 5");
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        templateToRate.addNewRate(rate, auth.getUid());
        return firestore
                .collection(COLLECTION_NAME)
                .document(templateToRate.getFirebaseID())
                .set(templateToRate);
    }

    public static List<PlantTemplate> mapSnapshotsToPlantTemplates(Collection<DocumentSnapshot> snapshots) {
        List<PlantTemplate> plantTemplates  = new ArrayList<>();
        int index = 0;
        for (DocumentSnapshot snap : snapshots) {
            PlantTemplate template = snap.toObject(PlantTemplate.class);
            template.setFirebaseID(snap.getId());
            template.setPlantID(index++);
            plantTemplates.add(template);
        }
        return plantTemplates;
    }

    public static boolean couldBeRated(PlantTemplate template) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getUid() != null
                && !template.getRatedBy().contains(auth.getUid());
    }

}

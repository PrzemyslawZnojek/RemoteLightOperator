package com.example.remotelightoperator.firebase;

import com.example.remotelightoperator.model.PlantTemplate;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Optional;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if(template.getRatedBy() == null){
            return true;
        }
        return auth.getUid() != null
                && !template.getRatedBy().contains(auth.getUid());
    }

    public static int getUserRate(String uid, PlantTemplate template) throws RuntimeException {
        String ratedBy = Optional.fromNullable(template.getRatedBy()).or("");
        Matcher matcher = Pattern
                .compile(String.format("%s:.", uid))
                .matcher(ratedBy.trim());
        if(matcher.find()) {
            String rate = matcher.group(0).split(":")[1];
            return Integer.parseInt(rate);
        } else {
            throw new IllegalArgumentException(String.format("Could not find id for user with ID: %s", uid));
        }

    }

}

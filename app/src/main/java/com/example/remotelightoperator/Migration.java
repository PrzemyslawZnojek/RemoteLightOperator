package com.example.remotelightoperator;

import com.example.remotelightoperator.model.ForcedState;
import com.example.remotelightoperator.model.LightOptions;
import com.example.remotelightoperator.model.PlantTemplate;
import com.example.remotelightoperator.model.UserConfiguration;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Migration {
    private static final String PLANT_TEMPLATE = "PlantTemplate";
    public static void execute() {
//        addLightOptionsToPlantTemplates();
//        addLightOptionsToUserConfiguration();
        removeAllRatings();

        // Add new scripts when needed, after execution comment out it
    }

    private static void addLightOptionsToPlantTemplates() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection(PLANT_TEMPLATE)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Collection<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snapshots) {
                            final String PLANT_TEMPLATE = "PlantTemplate";
                            PlantTemplate template = snap.toObject(PlantTemplate.class);

                            Random random = new Random();
                            template.setLightOptions(new LightOptions(random.nextInt(100)+156,
                                    random.nextInt(256),
                                    random.nextInt(265),
                                    random.nextInt(256)));

                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection(PLANT_TEMPLATE)
                                    .document(snap.getId())
                                    .set(template);
                        }
                    }
                });
    }

    private static void addLightOptionsToUserConfiguration() {
        final String USER_CONFIGURATION = "UserConfiguration";
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection(USER_CONFIGURATION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Collection<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snapshots) {
                            final String PLANT_TEMPLATE = "UserConfiguration";
                            UserConfiguration configuration = snap.toObject(UserConfiguration.class);
                            configuration.setLightOptions(new LightOptions());
                            configuration.setPlantName("no plant chosen");
                            configuration.setDescription("no plant chosen");
                            configuration.setForcedState(ForcedState.NONE);
                            configuration.setIrradiationTime(0);
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection(PLANT_TEMPLATE)
                                    .document(snap.getId())
                                    .set(configuration);
                        }
                    }
                });
    }

    private static void removeAllRatings() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection(PLANT_TEMPLATE)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Collection<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snapshots) {
                            final String PLANT_TEMPLATE = "PlantTemplate";
                            PlantTemplate template = snap.toObject(PlantTemplate.class);


                            template.setRateCount(0);
                            template.setRate(0);
                            template.setRatedBy("");

                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection(PLANT_TEMPLATE)
                                    .document(snap.getId())
                                    .set(template);
                        }
                    }
                });
    }
}

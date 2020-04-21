package com.example.remotelightoperator.firebase;

import com.example.remotelightoperator.model.ForcedState;
import com.example.remotelightoperator.model.LightOptions;
import com.example.remotelightoperator.model.PlantTemplate;
import com.example.remotelightoperator.model.UserConfiguration;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public  class UserConfigurationStoreUtils {
    private static final String COLLECTION_NAME = "UserConfiguration";
    private static final String UID_FIELD = "uid";

    public static Task<DocumentSnapshot> getUserConfigurationQueryTask() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        return  firestore
                .collection(COLLECTION_NAME)
                .document(auth.getUid())
                .get();
    }

    public static Task<Void> addUserConfigurationQueryTask(String uid) {
        LightOptions lightOptions = new LightOptions();

        UserConfiguration configurationCreator = new UserConfiguration();
        configurationCreator.setDescription("No plant chosen");
        configurationCreator.setForcedState(ForcedState.NONE);
        configurationCreator.setPlantName("No plant chosen");
        configurationCreator.setIrradiationTime(0);
        configurationCreator.setLightOptions(lightOptions);
        configurationCreator.setUid(uid);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        return  firestore
                .collection(COLLECTION_NAME)
                .document(uid)
                .set(configurationCreator);
    }

    public static Task<Void> updateUserConfigurationPlantParamsQueryTask(PlantTemplate plantTemplate) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        UserConfiguration configuration = new UserConfiguration();

        configuration.setUid(auth.getUid());
        configuration.setForcedState(ForcedState.NONE);
        configuration.setIrradiationTime(plantTemplate.getIrradiationTime());
        configuration.setPlantName(plantTemplate.getName());
        configuration.setDescription(plantTemplate.getDescription());

        return  firestore
                .collection(COLLECTION_NAME)
                .document(auth.getUid())
                .set(configuration);
    }

    public static UserConfiguration mapSnapshotsToUserConfiguration(DocumentSnapshot snapshot) {
        return snapshot.toObject(UserConfiguration.class);
    }

    public static Task<Void> changeLampState(UserConfiguration configuration, ForcedState state) {
        configuration.setForcedState(state);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        return  firestore
                .collection(COLLECTION_NAME)
                .document(auth.getUid())
                .set(configuration);
    }
}

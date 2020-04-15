package com.example.remotelightoperator.plantconfigurer;

import android.app.Activity;
import android.os.Bundle;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.firebase.UserConfigurationStoreUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.Nullable;

public class PlantConfigurerActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_configure_view);
        Task<DocumentSnapshot> queryTask = UserConfigurationStoreUtils.getUserConfigurationQueryTask();
    }
}

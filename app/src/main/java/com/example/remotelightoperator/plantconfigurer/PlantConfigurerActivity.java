package com.example.remotelightoperator.plantconfigurer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.firebase.UserConfigurationStoreUtils;
import com.example.remotelightoperator.model.ForcedState;
import com.example.remotelightoperator.model.UserConfiguration;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import androidx.annotation.Nullable;

public class PlantConfigurerActivity extends Activity{

    UserConfiguration configuration;
    ForcedState lightCurrentState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_configure_view);

        Intent intent = this.getIntent();
        configuration = (UserConfiguration) intent.getSerializableExtra("UserConfiguration");
        lightCurrentState = configuration.getForcedState();

        final ChipGroup group =  findViewById(R.id.lightModeGroup);

        switch(lightCurrentState){
            case ON:
                group.check(findViewById(R.id.lightOn).getId());
                break;
            case OFF:
                group.check(findViewById(R.id.lightOff).getId());
                break;
            case NONE:
                group.check(findViewById(R.id.lightAuto).getId());
                break;
        }

        group.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null){
                    CharSequence text = chip.getText();
                    if ("On".equals(text)) {
                        lightCurrentState = ForcedState.ON;
                    } else if ("Off".equals(text)) {
                        lightCurrentState = ForcedState.OFF;
                    } else if ("Auto".equals(text)) {
                        lightCurrentState = ForcedState.NONE;
                    }
                    UserConfigurationStoreUtils.changeLampState(configuration, lightCurrentState);
                }
            }
        });
    }
}

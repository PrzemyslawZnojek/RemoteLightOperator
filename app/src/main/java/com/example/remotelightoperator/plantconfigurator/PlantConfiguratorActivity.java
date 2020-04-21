package com.example.remotelightoperator.plantconfigurator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.firebase.UserConfigurationStoreUtils;
import com.example.remotelightoperator.model.ForcedState;
import com.example.remotelightoperator.model.LightOptions;
import com.example.remotelightoperator.model.UserConfiguration;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlantConfiguratorActivity extends AppCompatActivity {

    UserConfiguration configuration;
    ForcedState lightCurrentState;
    LightOptions lightOptions;
    SeekBar seekBar;
    ImageView image;
    RelativeLayout backgroundToShowLightColor;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_configure_view);

        Intent intent = this.getIntent();
        configuration = (UserConfiguration) intent.getSerializableExtra("UserConfiguration");
        lightCurrentState = configuration.getForcedState();
        lightOptions = configuration.getLightOptions();
        backgroundToShowLightColor = findViewById(R.id.show_color_background);
        int currentColor = Color.rgb(lightOptions.getRed(), lightOptions.getGreen(), lightOptions.getBlue());
        backgroundToShowLightColor.setBackgroundColor(currentColor);

        lampStateController();
        colorPickerController();
        seekBarController();

    }

    private void lampStateController() {
        final ChipGroup group = findViewById(R.id.lightModeGroup);
        setCurrentLampState(group);
        setChipsListener(group);
    }

    private void setCurrentLampState(ChipGroup group) {
        switch (lightCurrentState) {
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
    }

    private void setChipsListener(ChipGroup group) {
        group.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    setLightCurrentState(chip);
                    UserConfigurationStoreUtils.changeLampState(configuration, lightCurrentState);
                }
            }
        });
    }

    private void setLightCurrentState(Chip chip) {
        CharSequence text = chip.getText();
        if ("On".equals(text)) {
            lightCurrentState = ForcedState.ON;
        } else if ("Off".equals(text)) {
            lightCurrentState = ForcedState.OFF;
        } else if ("Auto".equals(text)) {
            lightCurrentState = ForcedState.NONE;
        }
    }

    private void colorPickerController() {
        image = findViewById(R.id.color_picker_image);
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache(true);
        setColorPickerListener();
    }

    private void setColorPickerListener() {
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap = image.getDrawingCache();
                    int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());
                    setLightOptionsColorFromImage(pixel);
                    updateColor(pixel);
                }
                return true;
            }
        });
    }

    private void setLightOptionsColorFromImage(int pixel) {
        lightOptions.setBlue(Color.blue(pixel));
        lightOptions.setRed(Color.red(pixel));
        lightOptions.setGreen(Color.green(pixel));
    }

    private void updateColor(int pixel) {
        UserConfigurationStoreUtils.changeLampOptions(configuration, lightOptions);
        backgroundToShowLightColor.setBackgroundColor(pixel);
    }

    private void seekBarController() {
        seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(lightOptions.getBrightness());
        setBrightnessListener();
    }

    private void setBrightnessListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lightOptions.setBrightness(progress);
                UserConfigurationStoreUtils.changeLampOptions(configuration,lightOptions);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

}

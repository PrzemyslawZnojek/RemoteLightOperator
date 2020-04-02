package com.example.remotelightoperator.myplants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.model.PlantTemplate;
import com.example.remotelightoperator.model.UserConfiguration;

import java.util.List;

public class MyPlantsAdapter  extends BaseAdapter {
    public List<UserConfiguration> userConfigurations;
    Context context;

    public MyPlantsAdapter(List<UserConfiguration> userConfigurations, Context context) {
        this.userConfigurations = userConfigurations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userConfigurations.size();
    }

    @Override
    public Object getItem(int position) {
        return userConfigurations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.plant_text, parent, false);
        }
        System.out.print("Plants " + userConfigurations.get(0).getPlantName());
        PlantTemplate plant = (PlantTemplate) getItem(position);
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView tvSurname = (TextView) convertView.findViewById(R.id.description);
        tvName.setText(plant.getName());
        tvSurname.setText(plant.getDescription());
        return convertView;

    }

}



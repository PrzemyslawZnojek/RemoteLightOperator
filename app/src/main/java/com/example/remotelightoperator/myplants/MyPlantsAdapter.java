package com.example.remotelightoperator.myplants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.remotelightoperator.R;
import com.example.remotelightoperator.model.PlantTemplate;

import java.util.List;

public class MyPlantsAdapter  extends BaseAdapter {
    public List<PlantTemplate> plants;
    Context context;

    public MyPlantsAdapter(List<PlantTemplate> plants, Context context) {
        this.plants = plants;
        this.context = context;
    }

    @Override
    public int getCount() {
        return plants.size();
    }

    @Override
    public Object getItem(int position) {
        return plants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return plants.get(position).getPlantID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.plant_text, parent, false);
        }
        System.out.print("Plants " + plants.get(0).getName());
        PlantTemplate plant = (PlantTemplate) getItem(position);
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView tvSurname = (TextView) convertView.findViewById(R.id.description);
        tvName.setText(plant.getName());
        tvSurname.setText(plant.getDescription());
        return convertView;

    }

}



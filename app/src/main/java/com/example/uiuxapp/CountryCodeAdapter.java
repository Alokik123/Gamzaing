package com.example.uiuxapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CountryCodeAdapter extends BaseAdapter {

    private Context context;
    private int[] flags;
    private LayoutInflater inflater;

    public CountryCodeAdapter(Context context, int[] flags) {
        this.context = context;
        this.flags = flags;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int position) {
        return flags[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        ImageView flagImageView = convertView.findViewById(R.id.country_flag);
        flagImageView.setImageResource(flags[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        ImageView flagImageView = convertView.findViewById(R.id.country_flag);
        flagImageView.setImageResource(flags[position]);

        return convertView;
    }
}

package com.tamz.soko2023;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Level> {

    private List<Level> data;

    public MyAdapter(Context context, int textViewResourceId, List<Level> data) {
        super(context, textViewResourceId, data);
        this.data = data;
    }

    public void addItem(Level l) {
        this.data.add(l);
    }
    @Override
    public int getCount() {
        return (int) this.data.stream().count();
    }

    @Override
    public Level getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }



        Level l = this.data.get(i);
        if (l != null) {
            SokoView s = (SokoView) v.findViewById(R.id.listSoko);
            TextView titleText = (TextView) v.findViewById(R.id.titleText);
            TextView scoreText = (TextView) v.findViewById(R.id.scoreText);
            s.setLevel(l, i);
            titleText.setText(l.getTitle());
            String scr = l.getScore() == 0 ? "NaN" : String.valueOf(l.getScore());
            scoreText.setText("Best score: " + scr);
        }

        return v;
    }
}

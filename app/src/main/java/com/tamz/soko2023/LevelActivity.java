package com.tamz.soko2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LevelActivity extends AppCompatActivity {

    ListView levelListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        this.levelListView = (ListView) findViewById(R.id.levelListView);

        ArrayAdapter<Level> adapter = new ArrayAdapter<Level>
                (this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, MainActivity.levelList);
        this.levelListView.setAdapter(adapter);

        this.levelListView.setOnItemClickListener((adapterView, view, i, l) -> {
            this.levelListView.setAdapter(null);
            Log.d("Item i", String.valueOf(i));
            Log.d("Item l", String.valueOf(l));
            Intent returnIntent = new Intent();
            returnIntent.putExtra("index", i);
            setResult(RESULT_OK, returnIntent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
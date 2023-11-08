package com.tamz.soko2023;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private SokoView sokoView;
    private GestureDetectorCompat mDetector;
    public static List<Level> levelList = new ArrayList<Level>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sokoView = (SokoView) findViewById(R.id.sokoView);
        mDetector = new GestureDetectorCompat(this,new MyGestureListener());
        this.loadAssets();
        this.sokoView.setLevel(levelList.get(1), 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.levelItem) {
            Intent intent = new Intent(this, LevelActivity.class);
            startActivityForResult(intent, 0);
        }
        if(id == R.id.reset) {
            Log.d("Reset", "reset");
            this.sokoView.reset();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK) {
            int index = data.getIntExtra("index", 0);
            this.sokoView.setLevel(levelList.get(index), index);
        }

    }

    public void loadAssets() {
        AssetManager assetManager = getAssets();
        InputStream input;
        try {
            input = assetManager.open("levels.txt");

            int size = input.available();
            byte[] buffer = new byte[size*2];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            String text = new String(buffer);
            this.assetToLevel(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void assetToLevel(String asset) {
        List<String> levels = new ArrayList<String>();
        String level = "";
        Log.d("Scanner", "Start of scanner");
        Scanner scanner = new Scanner(asset);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Log.d("Line", line);
            if (line.toLowerCase().contains("level")) {
                if(level != "") {
                    levels.add(level);
                    level = "";
                }
            }

            if (line.length() != 0)
                level += line + '\n';
        }
        scanner.close();
        for(String l : levels)
            levelList.add(new Level(l));
    }



    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            float deltaX = event2.getX() - event1.getX();
            float deltaY = event2.getY() - event1.getY();

            if (Math.abs(deltaX) > Math.abs(deltaY) ) {
                if (deltaX > 0) {
                    Log.d(DEBUG_TAG, "Move right");
                    sokoView.moveHero(Move.MOVE_RIGHT);
                } else {
                    Log.d(DEBUG_TAG, "Move left");
                    sokoView.moveHero(Move.MOVE_LEFT);
                }
                return true;
            } else {
                if (deltaY > 0) {
                    Log.d(DEBUG_TAG, "Move down");
                    sokoView.moveHero(Move.MOVE_DOWN);
                } else {
                    Log.d(DEBUG_TAG, "Move up");
                    sokoView.moveHero(Move.MOVE_UP);
                }
                return true;
            }
        }
    }

}
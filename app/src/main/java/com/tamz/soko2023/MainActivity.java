package com.tamz.soko2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    private SokoView sokoView;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sokoView = (SokoView) findViewById(R.id.sokoView);
        mDetector = new GestureDetectorCompat(this,new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
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
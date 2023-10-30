package com.tamz.soko2023;

public enum Move {
    MOVE_LEFT(-1),
    MOVE_RIGHT(1),
    MOVE_UP(-10),
    MOVE_DOWN(10);

    private int value;
    Move(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

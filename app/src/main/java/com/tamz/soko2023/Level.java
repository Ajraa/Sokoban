package com.tamz.soko2023;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Scanner;

public class Level {
    private int map[];
    private String title;
    private int height;
    private int width;

    public Level(String input) {
        int height = 0;
        int max_width = 0;
        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            height += 1;

            String line = scanner.nextLine();
            if(line.length() > max_width)
                max_width = line.length();
        }

        this.height = height-1;
        this.width = max_width;

        this.map = new int[height*max_width];
        scanner.close();

        scanner = new Scanner(input);
        int index = 0;
        this.title = scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char lineChars[] = line.toCharArray();
            for (char c : lineChars) {
                this.map[index++] = this.evaluateChar(c);
            }

            for (int i = 0; i < (this.width - line.length()); i++)
                this.map[index++] = 0;
        }
        scanner.close();
    }

    private int evaluateChar(char c) {
       switch (c) {
           case ' ':
               return 0;
           case '#':
               return 1;
           case '.':
               return 3;
           case '$':
               return 2;
           case '*':
               return 5;
           case '@':
               return 4;
           case '+':
               return 4;
           default:
               return 0;
       }
    }

    public int[] getMap() {
        return map;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
}

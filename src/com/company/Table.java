package com.company;

import java.awt.*;

public class Table {
    int minX,maxX,minY,maxY;

    public Table(int x,int y,int width,int height) {
        minX = x;
        minY = y;
        maxX = x + width - 1;
        maxY = y + height - 1;
    }

    public void set(int x, int y, int width, int height) {
        minX = x;
        minY = y;
        maxX = x + width - 1;
        maxY = y + height - 1;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(minX,minY,maxX-minX-1,maxY-minY-1);
        g.setColor(Color.ORANGE);
        g.drawRect(minX, minY, maxX - minX - 1, maxY - minY - 1);
    }
}

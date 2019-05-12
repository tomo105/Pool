package com.company;

import java.awt.*;

public class Table {
    int minX, maxX, minY, maxY;
    public static final int POCKET_NUMBER = 6;
    public static final int WINDOW_SIDE = 18;
    public static Pocket[] topPockets = new Pocket[POCKET_NUMBER / 2];
    public static Pocket[] bottomPockets = new Pocket[POCKET_NUMBER / 2];

    public Table(int x, int y, int width, int height) {
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
        g.setColor(Color.BLACK);
        g.fillRect(minX, minY, maxX - minX - 1, maxY - minY - 1);
        g.setColor(Color.ORANGE);
        g.drawRect(minX, minY, maxX - minX - 1, maxY - minY - 1);
        drawPockets(g);

    }

    private void drawPockets(Graphics g) {
        for (int i = 0; i < POCKET_NUMBER / 2; i++) {
            topPockets[i] = new Pocket(i * ((maxX / 2) - WINDOW_SIDE), minY);
            topPockets[i].draw(g);

            bottomPockets[i] = new Pocket(i * ((maxX / 2) - WINDOW_SIDE), maxY - 2 * WINDOW_SIDE);
            bottomPockets[i].draw(g);
        }
    }
}


package com.company;

import java.awt.*;

import static java.awt.Color.RED;

public class Ball {

    double x;
    double y;
    double speedX;
    double speedY;
    double radius;
    private Color color;

    public Ball(double x,double y,double radius,double speed,double angle) {
        this.x=x;
        this.y=y;
        this.speedX = speed * Math.cos(Math.toRadians(angle));
        this.speedY = -speed * Math.sin(Math.toRadians(angle));
        this.radius=radius;
        this.color=RED;
    }

    public void setSpeedAndAngle(double speed,double angle) {
        this.speedX = speed * Math.cos(Math.toRadians(angle));
        this.speedY = -speed * Math.sin(Math.toRadians(angle));
    }

    public void moveOneStep(Table table) {

        double ballMinX = table.minX + radius;
        double ballMinY = table.minY + radius;
        double ballMaxX = table.maxX - radius;
        double ballMaxY = table.maxY - radius;

        x += speedX;
        y += speedY;

        if(x < ballMinX) {
            speedX = -speedX;
            x=ballMinX;
        }  else if (x > ballMaxX) {
        speedX = -speedX;
        x = ballMaxX;
        }
        if (y < ballMinY) {
        speedY = -speedY;
        y = ballMinY;
        } else if (y > ballMaxY) {
        speedY = -speedY;
        y = ballMaxY;
    }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)(x-radius),(int)(y-radius),(int)(2*radius),(int)(2*radius) );
    }
}

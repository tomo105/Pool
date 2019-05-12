package com.company;

import java.awt.*;

import static java.awt.Color.RED;

public class Ball {

    double x;               // Ball's center x and y
    double y;
    double speedX;
    double speedY;
    double radius;
    private Color color;
    double slow;
    private double angle;
    private double speed;


    public Ball(double x, double y, double radius, double speed, double angle, Color color) {
        this.x = x;
        this.y = y;
        this.speedX = speed * Math.cos(Math.toRadians(angle));
        this.speedY = -speed * Math.sin(Math.toRadians(angle));
        this.radius = radius;
        this.color = color;
        this.slow = 0.99;
        this.angle = angle;
        this.speed = speed;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void setSpeedAndAngle(double speed, double angle) {
        this.speed = speed;
        this.angle = angle;
        this.speedX = speed * Math.cos(Math.toRadians(angle));
        this.speedY = -speed * Math.sin(Math.toRadians(angle));
    }

    public void addSuppression() {

        this.x += this.speedX;
        this.y += this.speedY;
        this.speedX *= slow;
        this.speedY *= slow;
        if(this.speedY < 0.2 && this.speedX < 0.2 && this.speedY > -0.2 && this.speedX > -0.2){
            this.speedX = 0.0f;
            this.speedY = 0.0f;
        }
    }

    public void updateWallCollision(Table table) {

        double ballMinX = table.minX + radius;
        double ballMinY = table.minY + radius;
        double ballMaxX = table.maxX - radius;
        double ballMaxY = table.maxY - radius;

        x += speedX;
        y += speedY;

        if (x < ballMinX) {
            speedX = -speedX;
            x = ballMinX;
        } else if (x > ballMaxX) {
            speedX = -speedX;
            x = ballMaxX;
        } else if (y < ballMinY) {
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

    public void setSlow(double slow) {
        this.slow = slow;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSpeedFromPosition(){
        if(this.speedX > 0)
            return this.speedX  / Math.cos(Math.toRadians(angle));
        else
            return -this.speedX  / Math.cos(Math.toRadians(angle));
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }
}

package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements MouseListener, MouseMotionListener {

    private Ball ball;
    private Ball ball2;
    private Table table;

    private DrawCanvas canvas;
    private int canvasWidth;
    private int canvasHeight;
    private double mouseX;
    private double mouseY;
    private boolean mouseClickedBall;
    private double speed;
    private double angle;

    public Game(int width, int height) {

        addMouseListener(this);
        addMouseMotionListener(this);

        canvasWidth = width;
        canvasHeight = height;

        int radius = 50;
        int x = canvasHeight / 2;
        int y = canvasWidth / 2;
        mouseX = x;
        mouseY = y;

        int speed = 0;
        int angleInDegree = 0;
        ball = new Ball(x, y - 300, radius, speed, angleInDegree);
        ball2 = new Ball(x, y, radius, speed, angleInDegree);

        table = new Table(0, 0, canvasWidth, canvasHeight);

        canvas = new DrawCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component c = (Component) e.getSource();
                Dimension dim = c.getSize();
                canvasWidth = dim.width;
                canvasHeight = dim.height;
                table.set(0, 0, canvasWidth, canvasHeight);
            }
        });

        gameStart();
    }


    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseIsInBall(e, ball)) {
            mouseClickedBall = true;

        }

        if (mouseClickedBall) {
            speed = ((ball.getX() - e.getX()) * (ball.getX() - e.getX()) + (ball.getY() - e.getY()) * (ball.getY() - e.getY())) * 0.0001;
            double temp = ((ball.getX() - e.getX()) * (ball.getX() - e.getX()) + (ball.getY() - e.getY()) * (ball.getY() - e.getY())) * 0.0001;
            angle = Math.toDegrees(Math.atan2(ball.getX() - e.getX(), ball.getY() - e.getY())) - 90;
            System.out.println(speed);
            System.out.println(angle);
            mouseX = e.getX();  //polozenie myszki
            mouseY = e.getY();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (mouseClickedBall) {
            ball.setSpeedAndAngle(speed, angle);
        }

        mouseX = ball.getX();
        mouseY = ball.getY();
        mouseClickedBall = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean detectCollision(Ball ball, Ball ball2) {
        double dist = Math.sqrt(Math.pow(ball.getX() - ball2.getX(), 2) + Math.pow(ball.getY() - ball2.getY(), 2));
        if (ball.radius + ball2.radius - dist > 0.0001) {
            System.out.println(ball.speedX);
            System.out.println(ball2.speedX);
            System.out.println("Collision");
            return true;
        }
        return false;
    }

    public void collision(Ball ball, Ball ball2) {
        ball.speedX = -ball.speedX;
        ball.speedY = -ball.speedY;
        System.out.println(ball.getSpeed() + ball.getAngle());
        ball2.setSpeedAndAngle(ball.getSpeedFromPosition(), -ball.getAngle());
        //ball2.setSpeedX(-ball.speedX);
        //ball2.setSpeedY(-ball.speedY);
    }

    public void gameStart() {

        Thread gameThread = new Thread() {
            public void run() {
                while (true) {
                    gameUpdate();
                    repaint();
                    try {
                        Thread.sleep(1000 / 30);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        gameThread.start();
    }

    public void gameUpdate() {

        ball.updateWallCollision(table);
        ball.addSuppression();
        ball2.updateWallCollision(table);
        ball2.addSuppression();
    }

    public boolean mouseIsInBall(MouseEvent e, Ball ball) {
        return ((e.getX() >= ball.getX() - ball.radius && e.getX() <= ball.getX() + ball.radius) && (e.getY() >= ball.getY() - ball.radius && e.getY() <= ball.getY() + ball.radius));
    }

    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            table.draw(g);
            ball.draw(g);
            ball2.draw(g);
            if (mouseClickedBall) g.drawLine((int) ball.getX(), (int) ball.getY(), (int) mouseX, (int) mouseY);

            if (detectCollision(ball, ball2)) {
                collision(ball, ball2);
                System.out.println("Byla kolizja");

            }
        }

        @Override
        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }
}

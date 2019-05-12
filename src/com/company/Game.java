package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Game extends JPanel implements MouseListener, MouseMotionListener {

    private static final int MAX_BALLS = 3;
    private Ball[] balls = new Ball[MAX_BALLS];
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
        balls[0] = new Ball(x, y - 300, radius, speed, angleInDegree, Color.WHITE);
        balls[1] = new Ball(x, y, radius, speed, angleInDegree, Color.RED);
        balls[2] = new Ball(x, y - 150, radius, speed, angleInDegree, Color.RED);

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
        if (mouseIsInBall(e, balls[0])) {
            mouseClickedBall = true;

        }

        if (mouseClickedBall) {
            speed = ((balls[0].getX() - e.getX()) * (balls[0].getX() - e.getX()) + (balls[0].getY() - e.getY()) * (balls[0].getY() - e.getY())) * 0.0001;
            double temp = ((balls[0].getX() - e.getX()) * (balls[0].getX() - e.getX()) + (balls[0].getY() - e.getY()) * (balls[0].getY() - e.getY())) * 0.0001;
            angle = Math.toDegrees(Math.atan2(balls[0].getX() - e.getX(), balls[0].getY() - e.getY())) - 90;
            System.out.println(speed);
            System.out.println(angle);
            mouseX = e.getX();  //polozenie myszki
            mouseY = e.getY();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (mouseClickedBall) {
            balls[0].setSpeedAndAngle(speed, angle);
        }

        mouseX = balls[0].getX();
        mouseY = balls[0].getY();
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

        ball.setSpeedAndAngle(ball2.getSpeedFromPosition(), -ball2.getAngle());
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

        for(int i = 0; i < MAX_BALLS; i ++) {
            balls[i].updateWallCollision(table);
            balls[i].addSuppression();
        }
    }

    public boolean mouseIsInBall(MouseEvent e, Ball ball) {
        return ((e.getX() >= ball.getX() - ball.radius && e.getX() <= ball.getX() + ball.radius) && (e.getY() >= ball.getY() - ball.radius && e.getY() <= ball.getY() + ball.radius));
    }

    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            table.draw(g);
            for(int i = 0; i < MAX_BALLS; i ++) {
                balls[i].draw(g);
            }
            if (mouseClickedBall) g.drawLine((int) balls[0].getX(), (int) balls[0].getY(), (int) mouseX, (int) mouseY);

            for(int i = 0; i < MAX_BALLS; i++) {
                for (int j = 0; j < MAX_BALLS; j++ ) {
                    if(i>j) {
                        if (detectCollision(balls[i], balls[j])) {
                            collision(balls[i], balls[j]);
                            System.out.println("Byla kolizja");
                        }
                    }
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }
}

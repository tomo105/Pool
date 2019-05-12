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
    private int radius;

    public Game(int width, int height) {

        addMouseListener(this);
        addMouseMotionListener(this);

        canvasWidth = width;
        canvasHeight = height;

        radius = 50;
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

        //Get positions
        double x = ball.getX();
        double y = ball.getY();
        double x2 = ball2.getX();
        double y2 = ball2.getY();

        //Get speed
        double dx = ball.getSpeedX();
        double dy = ball.getSpeedY();
        double dx2 = ball2.getSpeedX();
        double dy2 = ball2.getSpeedY();

        double diamSqr = (ball.getRadius() + ball2.getRadius()) * (ball.getRadius() + ball2.getRadius());
        double difDx = x - x2;
        double difDy = y - y2;

        //Move backwards (forwards if dt < 0) in time until balls are just touching
        double CoefA = (dx - dx2) * (dx - dx2) + (dy - dy2) * (dy - dy2);
        double CoefB = 2 * ((dx - dx2) * (x - x2) + (dy - dy2) * (y - y2));
        double CoefC = (x - x2) * (x - x2) + (y - y2) * (y - y2) - diamSqr;
        double t;
        if (CoefA == 0) {
            t = -CoefC / CoefB;
        } else {
            t = (-CoefB - Math.sqrt(CoefB * CoefB - 4 * CoefA * CoefC)) / (2 * CoefA);
        }

        //Center of momentum coordinates
        double mx = (dx + dx2) / 2;
        double my = (dy + dy2) / 2;
        dx = dx - mx;
        dy = dy - my;
        dx2 = dx2 - mx;
        dy2 = dy2 - my;

        //New center to center line
        double dist = Math.sqrt(difDx * difDx + difDy * difDy);
        difDx = difDx / dist;
        difDy = difDy / dist;

        //Reflect balls velocity vectors in center to center line
        double OB = -(difDx * dx + difDy * dy);
        dx = dx + 2 * OB * difDx;
        dy = dy + 2 * OB * difDy;
        OB = -(difDx * dx2 + difDy * dy2);
        dx2 = dx2 + 2 * OB * difDx;
        dy2 = dy2 + 2 * OB * difDy;

        //Back to moving coordinates with elastic velocity change
        double e = Math.sqrt(1.1);
        dx2 = e * (dx2 + mx);
        dy2 = e * (dy2 + my);

        //First ball velocities and position
        ball.setX(x - t * dx);
        ball.setY(y - t * dy);
        ball.setSpeedX( e * (dx + mx));
        ball.setSpeedY( e * (dy + my));

        x2 = x2 - t * dx2;
        y2 = y2 - t * dy2;

        //Set velocities and speed
        ball2.setSpeedX(dx2);
        ball2.setSpeedY(dy2);
        ball2.setX(x2);
        ball2.setY(y2);


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

        for (int i = 0; i < MAX_BALLS; i++) {
            balls[i].updateWallCollision(table);
            balls[i].addSuppression();
        }
    }

    public void setBallsSlow(double slow){
        for(int i = 0; i < MAX_BALLS; i++){
            balls[i].setSlow(slow);
        }
    }

    public void setBallsRadius(double radius){
        for(int i = 0; i < MAX_BALLS; i++){
            balls[i].setRadius(radius);
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
            for (int i = 0; i < MAX_BALLS; i++) {
                balls[i].draw(g);
            }
            if (mouseClickedBall) g.drawLine((int) balls[0].getX(), (int) balls[0].getY(), (int) mouseX, (int) mouseY);

            for (int i = 0; i < MAX_BALLS; i++) {
                for (int j = 0; j < MAX_BALLS; j++) {
                    if (i > j) {
                        if (detectCollision(balls[i], balls[j])) {
                            collision(balls[j], balls[i]);
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

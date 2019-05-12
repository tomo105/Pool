package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements MouseListener,MouseMotionListener {

    private Ball ball;
    private Table table;

    private DrawCanvas canvas;
    private int canvasWidth;
    private int canvasHeight;
    private double mouseX;
    private double mouseY;
    private boolean mouseClicked;
    private double speed;
    private double angle;

    public Game(int width, int height) {

        addMouseListener(this);
        addMouseMotionListener(this);

        canvasWidth = width;
        canvasHeight = height;

        int radius = 100;
        int x = canvasHeight/2;
        int y = canvasWidth/2;
        mouseX=x;
        mouseY=y;

        int speed = 0;
        int angleInDegree = 0;
        ball = new Ball(x, y, radius, speed, angleInDegree);

        table = new Table(0, 0, canvasWidth, canvasHeight);

        canvas = new DrawCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component c = (Component)e.getSource();
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
        if(mouseIsInBall(e))
            mouseClicked=true;
        if(mouseClicked) {
            speed=((ball.getX()-e.getX())*(ball.getX()-e.getX())+(ball.getY()-e.getY())*(ball.getY()-e.getY()))*0.0001;
            double temp=((ball.getX()-e.getX())*(ball.getX()-e.getX())+(ball.getY()-e.getY())*(ball.getY()-e.getY()))*0.0001;
            angle = Math.toDegrees(Math.atan2(ball.getX() - e.getX(), ball.getY() - e.getY()))-90;
            System.out.println(speed);
            System.out.println(angle);
            mouseX = e.getX();
            mouseY = e.getY();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if(mouseClicked)
        {
            ball.setSpeedAndAngle(speed,angle);
        }
        mouseX = ball.getX();
        mouseY = ball.getY();
        mouseClicked=false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public void gameStart() {

        Thread gameThread = new Thread() {
            public void run() {
                while (true) {
                    gameUpdate();
                    repaint();
                    try {
                        Thread.sleep(1000 / 30);
                    } catch (InterruptedException ex) {}
                }
            }
        };
        gameThread.start();
    }

    public void gameUpdate() {

        ball.moveOneStep(table);
    }

    public boolean mouseIsInBall(MouseEvent e){
        return ((e.getX()>=ball.getX()-ball.radius && e.getX()<=ball.getX()+ball.radius)&&(e.getY()>=ball.getY()-ball.radius && e.getY()<=ball.getY()+ball.radius));
    }

    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            table.draw(g);
            ball.draw(g);
            if(mouseClicked) g.drawLine((int)ball.getX(), (int)ball.getY(), (int)mouseX, (int)mouseY);
        }

        @Override
        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }
}

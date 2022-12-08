package com.pong.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.FlowLayout;
import java.awt.Font;

public class Window extends JFrame implements Runnable, KeyListener {
    private Paddle paddle1 = new Paddle(25, 300);
    private Paddle2 paddle2 = new Paddle2(750, 300);
    public Ball ball = new Ball(400, 400);
    Score score1 = new Score(250, 150);
    Score score2 = new Score(500, 150);
    JLabel label = new JLabel();
    JLabel scoreLabel = new JLabel();
    JButton button = new JButton("Play");
    JButton aiButton = new JButton("Play Against Ai");
    JLabel gameText = new JLabel("Ponggg");
    double velocity = 0;
    public boolean Pressed;
    boolean playPressed;
    BufferStrategy buffer;
    public boolean running;
    boolean gameStarted;
    boolean ai;
    int keycode;
    //
    public Window() {
        gameStarted = false;
        ai = false;
        // parameters for making the window (this) refers to the Window class/JFrame b\c it extends it
        this.setBounds(340, 30, 800, 700);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("PONGGG                                    it has three g's okay... it doesnt infringe on copy right i swear"); // Title
        this.setFocusable(true);
        this.addKeyListener(this);
        // creates the Menu
        Menu();
        //paints the screen but only once so that the button is visible
        this.setVisible(true); // also i cant figure out why the screen is white untill you hover over the button, like WHY??????????????????
        this.Buffer(getGraphics());
        run();
    }
    // run method (Its a seperate thread)
    @Override
    public void run() { // smooths framerate across devices by fixing framerate/game-updates/per second to time passed since last second and its only slightly borrowed... nice(!click)
        running = true; // used to tell game tick's while loop to run/ starts the game tick
        long lastLoopTime = System.nanoTime();
        final int FPS = 60;
        final long optimalTime = 1000000000 / FPS;
        long lastFpsTime = 0;
        //
        while(running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)optimalTime); // creates delta
            //
            lastFpsTime += updateLength;
            if(lastFpsTime >= 1000000000) {
                lastFpsTime = 0;
            }
            // ensures updates are only called if delta is greater than one/(updateLength is greater than optimalTime)
            if(delta >= 0 && gameStarted) {
                //
                this.updateGame();
                this.Buffer(getGraphics());
                // check if the player pressed on play ai button
                if(ai) {
                    this.ai();
                }
                // player slidey movement system
                if(!Pressed) {
                    wallcollision();
                    if(velocity > 0 && this.velocity != 0) {
                        if(velocity <= 1) {
                            this.paddle1.y -= velocity;
                            velocity = velocity / 2;
                        } else {
                            this.paddle1.y -= velocity;
                            velocity -= 0.2;
                        }
                    } else if(velocity < 0 && velocity != 0) {
                        if(velocity <= -1.2) {
                            this.paddle1.y -= velocity;
                            velocity += 0.2;
                        } else {
                            this.paddle1.y -= velocity;
                            velocity = velocity / 2;
                        }
                    }
                }
            }
            try {
                var l = (lastLoopTime - System.nanoTime() + optimalTime) / 1000000;
                Thread.sleep(l);
            }catch(IllegalArgumentException | InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    // paddle2's Ai/CPU mode
    public void ai() {
        // ai stuff here el bozo
        if(paddle2.y > ball.ball.y) {
            if(paddle2.y <= 10) {
                paddle2.y += 0.5;
            } else {
                paddle2.y -= 1;
                paddle2.paddle.y = paddle2.y;
            }
        } else if(paddle2.y < ball.ball.y) {
            if(paddle2.y >= 550) {
                paddle2.y -= 0.5;
            } else {
                paddle2.y += 1;
                paddle2.paddle.y = paddle2.y;
            }
        }
    }
    //
    public void playerGameOver() {
        // gameover state
        gameStarted = false;
        Dimension d = new Dimension();
        d.setSize(200, 135);
        label.setBackground(Color.WHITE);
        label.setText("GameOver");
        label.setSize(d);
        this.add(label);
    }
    //
    public void Menu() { // the menu
        Dimension d = new Dimension();
        d.width = 185;
        d.height = 45;
        // button stuff
        this.button.addActionListener(new ActionListener() { // Button stuff
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setVisible(false);
                aiButton.setVisible(false);
                gameStarted = true;
            }
        });
        this.aiButton.addActionListener(new ActionListener() { // toggles ai mode
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setVisible(false);
                aiButton.setVisible(false);
                gameStarted = true;
                ai = true;
            }
        });
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 350));
        aiButton.setPreferredSize(d);
        button.setPreferredSize(d);
        aiButton.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setFont(new Font("Arial", Font.PLAIN, 25));
        aiButton.setFocusPainted(false);
        button.setFocusPainted(false);
        this.add(aiButton);
        this.add(button);
    }
    //
    public void Buffer(Graphics g) {
        Graphics offg;
        Image offscreen = null;
        Dimension d = getSize();
        // creates the offscreen buffer
        offscreen = createImage(d.width, d.height);
        offg = offscreen.getGraphics();
        offg.setColor(getBackground());
        offg.fillRect(0, 0, d.width, d.height);
        offg.setColor(getForeground());
        paint(offg);
        // transfer offscreen to window
        g.drawImage(offscreen, 0, 0, this);
    }
    //
    public void collision() {
        // for paddle 1 collision
        if (ball.ball.intersects(this.paddle1.paddle)) {
            ball.setXDirection(+2);
        }
        // for paddle 2 collision  
        if (ball.ball.intersects(paddle2.paddle)) {
            ball.setXDirection(-2);
        }
    }
    //
    public void wallcollision() {
        if(this.paddle1.y >= 558) {
            this.paddle1.y -= 10;
            velocity = -velocity / 2;
        } else if(paddle1.y <= 40) {
            this.paddle1.y += 10;
            velocity = -velocity / 2;
        }
    }
    //
    protected void updateGame() {
        //game update methods such as calls to stuff goes here
        paddle1.paddle.y = paddle1.y;
        paddle2.paddle.y = paddle2.y;
        ball.move(this);
    }
    public void menuLabel() {
        gameText.setForeground(Color.WHITE);
        this.getContentPane().setBackground(Color.BLACK);
        gameText.setBounds(300, 150, 190, 100);
        gameText.setFont(new Font("Basic", Font.BOLD, 50));
        this.getContentPane().add(gameText);
    }
    //
    public void paint(Graphics g) {
        if(!gameStarted) {
            this.button.grabFocus();
            this.aiButton.grabFocus();
            menuLabel();
        } else if(gameStarted) {
            // paints ball using ball draw method
            this.ball.draw(g);
            // draws player 1's paddle
            this.paddle1.draw(g);
            // draws the player2/AI paddle
            this.paddle2.draw(g);
            // Player1's Score draw
            this.score1.draw1(g);
            // Player2's score
            this.score2.draw2(g);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        keycode = e.getKeyCode();
        Pressed = true;
        wallcollision();
        if(keycode == KeyEvent.VK_W || keycode == KeyEvent.VK_UP) {
            this.paddle1.y -= velocity;
            velocity += 0.2;
        } else if(keycode == KeyEvent.VK_S || keycode == KeyEvent.VK_DOWN ) {
            this.paddle1.y -= velocity;
            velocity -= 0.2;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        Pressed = false;
    }
}
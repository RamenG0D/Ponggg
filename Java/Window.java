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
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.FlowLayout;

public class Window extends JFrame implements Runnable, KeyListener {
    private Paddle paddle1 = new Paddle(25, 300);
    private Paddle2 paddle2 = new Paddle2(750, 300);
    public Ball ball = new Ball(400, 400);
    JLabel label = new JLabel();
    JButton button = new JButton("Play");
    JButton aiButton = new JButton("Play Against Ai");
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
        Dimension d = new Dimension();
        d.width = 220;
        d.height = 100;
        this.ball.fail = false;
        gameStarted = false;
        ai = false;
        // parameters for making the window (this) refers to the Window class/JFrame b\c it extends it
        this.getContentPane().setBackground(Color.BLACK);
        this.setBounds(340, 30, 800, 700);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("PONGGG                                 it has three g's okay... it doesnt infringe on copy right i swear");
        this.setFocusable(true);
        this.addKeyListener(this);
        // button stuff
        this.button.setBackground(Color.WHITE);
        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setVisible(false);
                gameStarted = true;
                ai = true;
            }
        });
        this.setLayout(new FlowLayout(FlowLayout.CENTER, this.getSize().width / 2, this.getSize().height / 2));
        button.setPreferredSize(d);
        //button.
        this.add(button);
        this.setVisible(true);
        this.paintComponents(getGraphics()); //paints the screen but only once so that the button is visible
        run();
    }
    // run method (Its a seperate thread)
    @Override
    public void run() { // smooths framerate across devices by fixing framerate/game-updates/per second to time passed since last second
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
            if(delta >= 0) {
                //
                if(gameStarted) {
                    this.updateGame();
                    this.Buffer(getGraphics());
                    ai();
                }
                //
                if(ball.playerGameOver() == true) {
                    this.playerGameOver();
                }
                //
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
            //
            try{
                var l = (lastLoopTime - System.nanoTime() + optimalTime) / 1000000;
                Thread.sleep(l);
            }catch(Exception e){}
            //
        }
    }
    //
    // paddle2's Ai/CPU mode
    public void ai() {
        // ai stuff here el bozo
        if(paddle2.y > ball.ball.y && paddle2.y > 558) {
            paddle2.y -= 1;
        } else if(paddle2.y < ball.ball.y && paddle2.y > 40) {
            paddle2.y += 1;
        }
    }
    //
    public void playerGameOver() {
        // gameover state
        gameStarted = false;
        label.setText("GameOver");
        label.setBackground(Color.WHITE);
        label.setSize(5000, 5000);
        this.add(label);
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
            ball.setXDirection(+1);
        }  
        // for paddle 2 collision  
        if (ball.ball.intersects(paddle2.paddle)) {
            ball.setXDirection(-1);
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
        paddle1.paddle.x = paddle1.x;
        paddle1.paddle.y = paddle1.y;
        this.ball.move(this);
    }
    //
    public void paint(Graphics g) {
        if(gameStarted) {
            // paints ball using ball draw method
            this.ball.draw(g);
            // draws player 1's paddle
            this.paddle1.draw(g);
            // draws the player2/AI paddle
            this.paddle2.draw(g);
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
            velocity += 0.1;
        } else if(keycode == KeyEvent.VK_S || keycode == KeyEvent.VK_DOWN ) {
            this.paddle1.y -= velocity;
            velocity -= 0.1;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        Pressed = false;
    }
}
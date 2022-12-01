import java.awt.Graphics;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.awt.Image;

public class Ball {
    File ballFile = new File("C:/Users/kali/Downloads/Java/Game/src/Ball.png");
    Image ballImage;
    Rectangle ball;
    int xDirection;
    int yDirection;
    boolean fail;
    int x;
    int y;
    //
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        Random r = new Random(); //creats Random numbers  
        int rDir = r.nextInt(0 ,2);  
        if (rDir == 0)  
        {
            rDir--;  
        }
        setXDirection(rDir);  
        int yrDir = r.nextInt(0, 2);  
        if (yrDir == 0)  
        {  
            yrDir--;  
        }
        setYDirection(yrDir);  
        ball = new Rectangle(this.x, this.y, 40, 40);
    }
    public void setXDirection(int xDir) {
        xDirection = xDir;
    }
    public void setYDirection(int yDir) {
        yDirection = yDir;
    }
    //
    public void move(Window window) {
        window.collision();
        ball.x += xDirection;  
        ball.y += yDirection;  
        //Bounce the ball when edge is detected  
        if(ball.x <= 14) {  
            setXDirection(+1);
            playerGameOver();
            fail = true;
        } else if (ball.x >= 745) { 
            setXDirection(-1);
            Score.playerScore(+1);
        } else if (ball.y <= 30) {  
            setYDirection(+1);  
        } else if (ball.y >= 640) {  
            setYDirection(-1);  
        }  
    }
    public boolean playerGameOver() {
        if(fail == true) {
            System.out.println("bruh");
            return true;
        } else {
            return false;
        }
    }
    public Ball ball() {
        return new Ball(x, y);
    }
    public void draw(Graphics g) {
        // draw parameters
        try {
            ballImage = ImageIO.read(ballFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.drawImage(ballImage, this.ball.x, this.ball.y, 40, 40, null);
    }
}
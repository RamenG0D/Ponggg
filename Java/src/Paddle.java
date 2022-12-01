import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

public class Paddle {
    public File Paddle = new File("assets\\Paddle.png");
    public Image playerImage;
    public Rectangle paddle;
    public int y;
    public int x;
    //
    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
        paddle = new Rectangle(x, y, 25, 140);
    }
    // draw method for createing the players paddle
    public void draw(Graphics g) {
        // Loads Paddle asset
        try {
            playerImage = ImageIO.read(Paddle);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // graphics for the players paddle, this gets used by paint method later to paint it to the screen
        g.drawImage(playerImage, x, y, 25, 140, null);
    }
}
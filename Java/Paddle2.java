import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;

public class Paddle2 {
    public Rectangle paddle;
    public File p2File = new File("C:/Users/kali/Downloads/Java/Game/src/Paddle.png");
    public int x;
    public int y;
    Image Sprite;
    //
    public Paddle2(int x, int y) {
        this.x = x;
        this.y = y;
        paddle = new Rectangle(x, y, 25, 145);
    }
    public Paddle2 paddle() {
        return new Paddle2(x, y);
    }
    //
    public void draw(Graphics g) {
        // gets Paddle sprite and assigns it to Image Sprite;
        try {
            Sprite = ImageIO.read(p2File);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // draws paddle Sprite
        g.drawImage(Sprite, x, y, 25, 140, null);
    }
}

package Player;

import static Util.Commons.*;
import javax.swing.ImageIcon;
import Util.Sprite;
import java.awt.Image;


public class Shot extends Sprite {

    private String shot = "/Recursos/shot.png";
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;

    public Shot() {
    }

    public Shot(int x, int y) {

        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        Image image = ii.getImage();
        Image newimg = image.getScaledInstance(10, 10,  java.awt.Image.SCALE_SMOOTH);
        ii = new ImageIcon(newimg);
        setImage(ii.getImage());
        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }
}
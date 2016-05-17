package Enemies;

import static Util.Commons.*;
import javax.swing.ImageIcon;
import Util.Sprite;
import java.awt.Image;


public class Alien extends Sprite {

    private Bomb bomb;
    private final String shot = "/Recursos/alien.png";

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));        
        Image image = ii.getImage();
        Image newimg = image.getScaledInstance(ALIEN_HEIGHT, ALIEN_WIDTH,  java.awt.Image.SCALE_SMOOTH);
        ii = new ImageIcon(newimg);
        setImage(ii.getImage());

    }

    public void act(int direction) {
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Sprite {

        private final String bomb = "/Recursos/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(this.getClass().getResource(bomb));
            Image image = ii.getImage();
            Image newimg = image.getScaledInstance(BOMB_HEIGHT, BOMB_WIDTH,  java.awt.Image.SCALE_SMOOTH);
            ii = new ImageIcon(newimg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}

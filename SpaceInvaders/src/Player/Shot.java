package Player;

import Enemies.Alien;
import static Util.Commons.*;
import javax.swing.ImageIcon;
import Util.Sprite;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;


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
    
    public int update(ArrayList<Alien> aliens,String expl){
        int deaths = 0;
        if (isVisible()) {
            Iterator it = aliens.iterator();
            int shotX = getX();
            int shotY = getY();

            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + ALIEN_HEIGHT)) {
                        ImageIcon ii
                                = new ImageIcon(getClass().getResource(expl));
                        Image image = ii.getImage();
                        Image newimg = image.getScaledInstance(EXPLOSION_HEIGHT, EXPLOSION_WIDTH, java.awt.Image.SCALE_SMOOTH);
                        ii = new ImageIcon(newimg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        die();
                    }
                }
            }

            int y = getY();
            y -= 4;
            if (y < 0) {
                die();
            } else {
                setY(y);
            }
        }
        return deaths;
    }
    
}
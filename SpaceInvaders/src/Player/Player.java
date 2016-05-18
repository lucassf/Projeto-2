package Player;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import Util.Commons;
import Util.Sprite;
import java.awt.Image;
import java.io.IOException;

public class Player extends Sprite implements Commons {

    private final int START_Y = 280;
    private final int START_X = 270;

    private String player;
    private int width;
    private String socketmessage = "";
    private int left;
    private int right;
    private int shoot;
    private final boolean controlled;

    public Player(String p, boolean controlled) {
        player = p;
        this.controlled = controlled;
        ImageIcon ii = new ImageIcon(this.getClass().getResource(player));
        Image image = ii.getImage();
        Image newimg = image.getScaledInstance(PLAYER_HEIGHT, PLAYER_WIDTH, java.awt.Image.SCALE_SMOOTH);
        ii = new ImageIcon(newimg);
        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }

    public void update() {
        if (!controlled) {
            if (left > 0) {
                dx = -2;
            } else if (right > 0) {
                dx = 2;
            } else {
                dx = 0;
            }
            socketmessage = "MOVE " + dx + " " + shoot;
            
        }
        x += dx;
        if (x <= 2) {
            x = 2;
        }
        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }

    }

    public int getDirection() {
        return dx;
    }

    public void setDirection(int direction) {
        this.dx = direction;
    }

    public int getShoot() {
        return shoot;
    }

    public void setShoot(int shoot) {
        this.shoot = shoot;
    }

    public String getSocketmessage() {
        return socketmessage;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }
}

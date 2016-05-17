package spaceinvaders;

import Util.Commons;
import javax.swing.*;

public class SpaceInvaders extends JFrame implements Commons {

    public SpaceInvaders(int i) {
        Board board = new Board(i);
        if (!board.isWorked()) {
            dispose();
        } else {
            add(board);
            setTitle("Space Invaders");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(BOARD_WIDTH, BOARD_HEIGTH);
            setLocationRelativeTo(null);
            setVisible(true);
            setResizable(false);
        }
    }

    public static void main(String[] args) {
        Menu calc = new Menu();
        calc.setVisible(true);
        //     new SpaceInvaders();
    }
}

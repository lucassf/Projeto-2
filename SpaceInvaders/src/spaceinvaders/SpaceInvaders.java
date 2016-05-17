package spaceinvaders;
import Util.Commons;
import static Util.Commons.BOMB_HEIGHT;
import static Util.Commons.BOMB_WIDTH;
import java.awt.Image;
import static java.lang.System.*;
import javax.swing.*;
import javax.swing.ImageIcon;

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
class LoadingIcon{    
        private final String gif = "/Recursos/loading.gif";
        public LoadingIcon(){
            JFrame load = new JFrame("Loading");
            ImageIcon loading = new ImageIcon(this.getClass().getResource(gif));  
          //  Image image = loading.getImage();
        //    Image newimg = image.getScaledInstance(500, 500,  java.awt.Image.SCALE_SMOOTH);
        //    loading = new ImageIcon(newimg);
            load.add(new JLabel("Conectando... ", loading, JLabel.CENTER));
            load.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            load.setSize(400, 300);
            load.setVisible(true);
        }
}

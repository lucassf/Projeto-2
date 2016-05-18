package spaceinvaders;

import Util.Commons;
import Player.Player;
import Player.Shot;
import Enemies.Alien;
import static Util.Commons.ALIEN_HEIGHT;
import static Util.Commons.ALIEN_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Board extends JPanel implements Runnable, Commons {

    private Dimension d;
    private ArrayList aliens;
    private Player player1, player2;
    private Shot shot;
    private Shot shot2;
    private int nplayers;
    private int alienX = 150;
    private int alienY = 5;
    private int direction = -1;
    private int deaths = 0;

    private boolean ingame1 = true, ingame2 = false; //player1 e player2
    private final String expl = "/Recursos/explosion.png";
    private final String alienpix = "/Recursos/alien.png";
    private String message = "Game Over";

    private String HOST = "localhost";
    private int PORT = 9090;
    private boolean worked;

    private Thread animator;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public Board(int n) {
        nplayers = n;
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);
        worked = true;
        if (!gameInit()) {
            worked = false;
        }
        setDoubleBuffered(true);
    }

    public boolean gameInit() {

        aliens = new ArrayList();

        ImageIcon ii = new ImageIcon(this.getClass().getResource(alienpix));
        Image image = ii.getImage();
        Image newimg = image.getScaledInstance(ALIEN_HEIGHT, ALIEN_WIDTH, java.awt.Image.SCALE_SMOOTH);
        ii = new ImageIcon(newimg);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = new Alien(alienX + 18 * j, alienY + 18 * i);
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }

        player1 = new Player("/Recursos/player.png", false);
        player2 = null;
        if (nplayers == 2) {
            player2 = new Player("/Recursos/player2.png", true);
            ingame2 = true;
            try {
                socket = new Socket(HOST, PORT);
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                input.readLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar no servidor",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                this.setVisible(false);
                return false;
            }
        }
        shot = new Shot();
        shot2 = new Shot();

        if (animator == null || (!ingame1 && !ingame2)) {
            animator = new Thread(this);
            animator.start();
        }
        return true;
    }

    public void drawAliens(Graphics g) {
        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayers(Graphics g) {

        if (player1.isVisible()) {
            g.drawImage(player1.getImage(), player1.getX(), player1.getY(), this);
        }
        if (nplayers == 2 && player2.isVisible()) {
            g.drawImage(player2.getImage(), player2.getX(), player2.getY(), this);
        }
        if (player1.isDying()) {
            player1.die();
            ingame1 = false;
        }
        if (nplayers == 2 && player2.isDying()) {
            player2.die();
            ingame2 = false;
        }
    }

    public void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
        if (shot2.isVisible()) {
            g.drawImage(shot2.getImage(), shot2.getX(), shot2.getY(), this);
        }
    }

    public void drawBombing(Graphics g) {

        Iterator i3 = aliens.iterator();

        while (i3.hasNext()) {
            Alien a = (Alien) i3.next();

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame1 || ingame2) {

            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            drawAliens(g);
            drawPlayers(g);
            drawShot(g);
            drawBombing(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {

        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

    public void animationCycle() throws IOException {

        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame1 = false;
            ingame2 = false;
            message = "Game won!";
        }

        // player
        player1.update();
        if (!shot.isVisible() && player1.getShoot() > 0) {
            shot = new Shot(player1.getX(), player1.getY());
        }
        if (nplayers > 1) {
            player2.update();
            if (!shot2.isVisible() && player2.getShoot() > 0) {
                shot2 = new Shot(player2.getX(), player2.getY());
            }
        }

        // shot
        deaths += shot.update(aliens, expl);
        deaths += shot2.update(aliens, expl);

        // aliens
        Iterator it1 = aliens.iterator();

        while (it1.hasNext()) {
            Alien a1 = (Alien) it1.next();
            int x = a1.getX();

            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                direction = -1;
                Iterator i1 = aliens.iterator();
                while (i1.hasNext()) {
                    Alien a2 = (Alien) i1.next();
                    a2.setY(a2.getY() + GO_DOWN);
                }
            }

            if (x <= BORDER_LEFT && direction != 1) {
                direction = 1;

                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien) i2.next();
                    a.setY(a.getY() + GO_DOWN);
                }
            }
        }

        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > GROUND - ALIEN_HEIGHT) {
                    ingame1 = false;
                    ingame2 = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

        // bombs
        Iterator i3 = aliens.iterator();
        int[] vars = null;
        if (nplayers > 1) {
            output.println("ALIENS " + (NUMBER_OF_ALIENS_TO_DESTROY - deaths));
            output.flush();
            String[] separator = input.readLine().split(" ");
            vars = new int[separator.length];
            for (int i = 0; i < vars.length; i++) {
                vars[i] = Integer.parseInt(separator[i]);
            }
        }
        Random generator = new Random();

        int j = 0;

        while (i3.hasNext()) {
            int shot = nplayers > 1 ? vars[j] : generator.nextInt(15);
            if (nplayers>1)
                j = (j+1)%vars.length;
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());
            }

            b.checkColision(player1, expl);
            if (nplayers > 1) {
                b.checkColision(player2, expl);
            }

            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    public void run() {

        long beforeTime, timeDiff, sleep;
        String action = "BEGIN";
        try {
            beforeTime = System.currentTimeMillis();
            if (nplayers > 1) {
                output.println(action);
                System.out.println(input.readLine());
            }
            while (ingame1 || ingame2) {

                if (nplayers > 1) {
                    output.println(player1.getSocketmessage());
                    output.flush();
                    action = input.readLine();
                    if (action.equals("EXIT")) {
                        break;
                    }
                    Player2command(action);
                }
                animationCycle();
                repaint();

                timeDiff = System.currentTimeMillis() - beforeTime;
                sleep = DELAY - timeDiff;

                if (sleep < 0) {
                    sleep = 2;
                }
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    System.out.println("interrupted");
                }
                beforeTime = System.currentTimeMillis();
            }
        } catch (IOException ex) {
            System.out.println("Player 2 closed connection");
        }
        if (nplayers > 1) {
            output.println("EXIT");
            output.flush();
        }
        gameOver();
    }

    private void Player2command(String action) {
        String[] mes = action.split(" ");
        if (mes[0].equalsIgnoreCase("MOVE")) {
            int direction = Integer.parseInt(mes[1]);
            int shoot = Integer.parseInt(mes[2]);

            player2.setDirection(direction);
            player2.setShoot(shoot);
        }
    }

    public boolean isWorked() {
        return worked;
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                player1.setRight(0);
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                player1.setLeft(0);
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                player1.setShoot(0);
            }
        }

        public void keyPressed(KeyEvent e) {

            if (ingame1) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player1.setRight(1);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player1.setLeft(1);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    player1.setShoot(1);
                }
            }
        }
    }
}

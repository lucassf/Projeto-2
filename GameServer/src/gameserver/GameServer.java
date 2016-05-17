package gameserver;

import java.net.*;
import java.io.*;


public class GameServer {
    
    static int PORT = 9090;
    
    public static void main(String[] args) {
        
        try (ServerSocket serversocket = new ServerSocket(PORT)){
            while (true) {
                Player player1 = new Player(serversocket.accept());
                Player player2 = new Player(serversocket.accept());
                player1.run();
                player2.run();
            }
        } catch (IOException ex) {
            System.out.println("Exit");
        }
    }
    
}

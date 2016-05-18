package gameserver;

import java.net.*;
import java.io.*;


public class GameServer {
    
    static int PORT = 9090;
    
    public static void main(String[] args) {
        
        try (ServerSocket serversocket = new ServerSocket(PORT)){
            while (true) {
                Game game = new Game(serversocket.accept(),serversocket.accept());
                new Thread(game).start();
                
            }
        } catch (IOException ex) {
            System.out.println("Exit");
        }
    }
    
}

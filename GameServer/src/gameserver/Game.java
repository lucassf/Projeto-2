package gameserver;

import java.net.*;
import java.io.*;

/*Protocolo
Client->Server

MOVE x y// x = 0, 2, -2; y = 0, 1
EXIT // jogador saiu do jogo

Server->Client

START
MOVE x y
EXIT
*/

public class Game implements Runnable{
    
    Socket socket1;
    Socket socket2;
    
    public Game(Socket socket1,Socket socket2){
        this.socket1 = socket1;
        this.socket2 = socket2;
    }

    @Override
    public void run() {
        try (
                BufferedReader socketinput1 = new BufferedReader(
                        new InputStreamReader(socket1.getInputStream()));
                PrintWriter socketoutput1 = new PrintWriter(socket1.getOutputStream());
                BufferedReader socketinput2 = new BufferedReader(
                        new InputStreamReader(socket2.getInputStream()));
                PrintWriter socketoutput2 = new PrintWriter(socket2.getOutputStream());) {
            socketoutput1.println("START");
            socketoutput2.println("START");
            socketoutput1.flush();
            socketoutput2.flush();
            while (true){
                String message1 = socketinput1.readLine();
                String message2 = socketinput2.readLine();
                socketoutput1.println(message2);
                socketoutput2.println(message1);
                socketoutput1.flush();
                socketoutput2.flush();
                if (message1.split(" ")[0]=="EXIT"||message2.split(" ")[0]=="EXIT"){
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Jogador morreu");
        }
    }
}

package gameserver;

import java.net.*;
import java.io.*;

/*Protocolo
Client->Server

SHOT (0,1) // 0 = não atira, 1 = atira
MOVE (0,1,2) // 0 = não move, 1 = esquerda, 2 = direita
EXIT // jogador saiu do jogo

Server->Client

START
SHOT (0,1)
MOVE (0,1,2)
EXIT
*/

public class Player implements Runnable{
    
    Socket socket;
    Player otherplayer;
    String action = "MOVE 0";
    
    public Player(Socket socket){
        this.socket = socket;
    }
    
    public void setOthers(Player p){
        otherplayer = p;
    }
    
    public String getAction(){
        return action;
    }

    @Override
    public void run() {
        try (
                BufferedReader socketinput = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter socketoutput = new PrintWriter(socket.getOutputStream());) {
            socketoutput.println("START");
            while (true){
                String message = socketinput.readLine();
                if (message.split(" ")[0]=="EXIT"){
                    break;
                }
                action = message;
                socketoutput.println(otherplayer.getAction());
            }
        } catch (IOException e) {
            System.out.println("Jogador morreu");
        }
    }
}

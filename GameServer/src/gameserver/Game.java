package gameserver;

import java.net.*;
import java.io.*;
import java.util.Random;

/*Protocolo
Client->Server

BEGIN // ponto de partida do jogo
MOVE x y// x = velocidade, y = 1 indica que a nave disparou
ALIEN x// número de aliens no jogo
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
            boolean exit = false;
            while (!exit){
                String command1 = socketinput1.readLine();
                String command2 = socketinput2.readLine();
                String[] c1 = command1.split(" ");
                String[] c2 = command2.split(" ");
                if (c1[0].equals("EXIT")||c2[0].equals("EXIT")){
                    command1 = command2 = "EXIT";
                    exit=true;
                } else if (c1[0].equals("BEGIN")){
                    command1 = command2 = "Game began";
                } else if (c1[0].equals("MOVE")){
                    String aux = command1;
                    command1=command2;
                    command2=aux;
                } else if (c1[0].equals("ALIENS")){
                    Random generator = new Random();
                    int qtt = Integer.parseInt(c1[1]);
                    command1 = "";
                    for (int i=0;i<qtt;i++){
                        command1 = command1+generator.nextInt(15);
                        command1+=" ";
                    }
                    command2 = command1;
                }
                socketoutput1.println(command1);
                socketoutput2.println(command2);
                socketoutput1.flush();
                socketoutput2.flush();
            }
        } catch (IOException e) {
            System.out.println("Jogador morreu");
        }
    }
}

package dk.dtu.mtd.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jspace.*;

public class Server {

    SpaceRepository server;
    Space lobby;
    String ip;
    int IDrequest = 0; 

    public Server(){
        server = new SpaceRepository();
        lobby = new SequentialSpace();
        server.add("lobby", lobby);
        try {
            ip = (InetAddress.getLocalHost().getHostAddress()).trim();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        server.addGate("tcp://" + ip + ":37331/?keep");
        System.out.println("Server ip: " + ip);
    }

    public void launch(){
        while(true){
            try {
                Object[] request = lobby.get(new ActualField("request"), new FormalField(String.class), new FormalField(Integer.class));
                
                if((request[1].toString()).equals("id")){
                    lobby.put(IDrequest);
                    IDrequest++;
                } else if ((request[1].toString()).equals("game")){
                    
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server ser = new Server();
        ser.launch();
    }
}

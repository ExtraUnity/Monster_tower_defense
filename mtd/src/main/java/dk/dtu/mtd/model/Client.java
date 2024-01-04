package dk.dtu.mtd.model;

import java.io.IOException;

import org.jspace.ActualField;
import org.jspace.RemoteSpace;

public class Client {
    RemoteSpace lobby;

    public Client(String hostIP){
        try {
            lobby = new RemoteSpace("tcp://"+ hostIP +":37331/lobby?keep");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        Client cli = new Client("10.209.240.41");
        try {
            cli.lobby.put("hey");
            System.out.println(cli.lobby.get(new ActualField("hey"))[0].toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

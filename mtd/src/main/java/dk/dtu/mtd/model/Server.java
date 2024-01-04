package dk.dtu.mtd.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jspace.*;

public class Server {

    SpaceRepository server;
    String ip;

    public Server(){
        server = new SpaceRepository();
        server.add("lobby", new SequentialSpace());
        try {
            ip = (InetAddress.getLocalHost().getHostAddress()).trim();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        server.addGate("tcp://" + ip + ":37331/?keep");
    }

    public static void main(String[] args) {
        Server ser = new Server();
    }
}

package dk.dtu.mtd.model;

import org.jspace.*;

public class Server {

    SpaceRepository server;

    public Server(){
        server = new SpaceRepository();

        server.add("lobby", new SequentialSpace());
    }
}

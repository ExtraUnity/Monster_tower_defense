package dk.controller;

import org.jspace.ActualField;

import dk.dtu.mtd.model.Client;

public class Controller {
    static Client client = new Client("10.209.248.191");;


    public static void joinLobby() {
        try {
            client.lobby.put("hey");
            client.lobby.get(new ActualField("hey back"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

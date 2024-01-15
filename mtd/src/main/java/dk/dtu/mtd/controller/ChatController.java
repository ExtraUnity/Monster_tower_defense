package dk.dtu.mtd.controller;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.view.GameGui;
import javafx.application.Platform;

public class ChatController implements Runnable {
    Client client;
    int contactId;
    RemoteSpace joinSpace;
    Space hostSpace;

    public ChatController(Client client, int contactId, RemoteSpace joinSpace) {
        this.client = client;
        this.contactId = contactId;
        this.joinSpace = joinSpace;
    }

    public ChatController(Client client, int contactId, Space hostSpace) {
        this.client = client;
        this.contactId = contactId;
        this.hostSpace = hostSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg;

                // Look for messages
                if (hostSpace == null) {
                    msg = (String) joinSpace.get(new ActualField("chat"), new ActualField(client.id),
                            new FormalField(String.class))[2];
                } else {
                    msg = (String) hostSpace.get(new ActualField("chat"), new ActualField(client.id),
                            new FormalField(String.class))[2];
                }

                // Update gui
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        GameGui.updateGameGui(msg);
                    }

                });
            } catch (InterruptedException e) {
                System.out.println("Chat closed");
            }
        }
    }

    public void sendMessage(String msg) {
        try {
            msg = "Player " + client.id + ": " + msg;

            // Send copy to both parties
            if (hostSpace == null) {
                joinSpace.put("chat", contactId, msg);
                joinSpace.put("chat", client.id, msg);
            } else {
                hostSpace.put("chat", contactId, msg);
                hostSpace.put("chat", client.id, msg);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
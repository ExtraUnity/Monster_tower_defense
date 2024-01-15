package dk.dtu.mtd.controller;

import java.util.LinkedList;

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
    @SuppressWarnings("unchecked")
    public void run() {
        while (true) {
            try {
                String msg;
                LinkedList<String> chat;
                if (hostSpace == null) {
                    msg = (String) joinSpace.get(new ActualField("chat"), new ActualField(client.id), new FormalField(String.class))[2];
                    chat = (LinkedList<String>) joinSpace.get(new ActualField("chatList"), new FormalField(LinkedList.class))[1];
                    chat.add(msg);
                    joinSpace.put("chatList", chat);
                } else {
                    msg = (String) hostSpace.get(new ActualField("chat"), new ActualField(client.id), new FormalField(String.class))[2];
                    chat = (LinkedList<String>) hostSpace.get(new ActualField("chatList"), new FormalField(LinkedList.class))[1];
                    chat.add(msg);
                    hostSpace.put("chatList", chat);
                }
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            GameGui.updateGameGui(chat);
                        }

                    });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg) {
        try {
            if (hostSpace == null) {
                joinSpace.put("chat", contactId, msg);
                joinSpace.put("chat", client.id, msg);
            } else {
                hostSpace.put("chat", contactId, msg);
                hostSpace.put("chat", client.id, msg);
            }
        } catch (InterruptedException e) {

        }
    }

}
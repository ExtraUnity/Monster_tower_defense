package dk.dtu.mtd.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;
import org.jspace.SpaceRepository;

import dk.dtu.mtd.controller.ChatController;
import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.shared.EnemyType;

public class Client {
    public RemoteSpace lobby;
    public RemoteSpace gameSpace;
    public RemoteSpace joinChat;
    public SpaceRepository hostServer;
    public Space hostChat;
    private static Thread chatThread;
    GameMonitor gameMonitor;
    ChatController chatController;
    public int id;
    private int gameId = -1;
    String hostIP;

    public Client() {

    }

    /**
     * Joins the lobby with the specified id.
     * @param serverIp
     * @throws UnknownHostException
     * @throws IOException
     * @throws InterruptedException
     */
    public void joinLobby(String serverIp) throws UnknownHostException, IOException, InterruptedException {
        // Join lobby
        this.hostIP = serverIp;
        lobby = new RemoteSpace("tcp://" + hostIP + ":37331/lobby?keep");

        // Get unique id from server
        lobby.put("request", "id", -1);
        id = (int) lobby.get(new ActualField("id"), new FormalField(Integer.class))[1]; // Get new id
        System.out.println("Successful connection to lobby");
    }

    /**
     * Sends a request to lobby to join a game. Blocks thread until joined.
     * @return "host" or "guest"
     */
    public String requestGame() {
        try {
            // Look for a game
            lobby.put("request", "game", id);
            Object[] res = lobby.get(new ActualField("game"), new ActualField(id),
                    new FormalField(Integer.class), new FormalField(String.class));
            gameId = (int) res[2];
            return (String) res[3];
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Joins the game given that a gameId has been created.
     */
    public void joinGame() {
        try {
            // Join game
            gameSpace = new RemoteSpace("tcp://" + hostIP + ":37331/game" + gameId + "?keep");
            gameMonitor = new GameMonitor(this, gameSpace, lobby, id, gameId);
            new Thread(gameMonitor).start();
            System.out.println("Successful connection to game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Join a game chat as a guest. Collect chatIP from gameSpace when host has created chat.
     */
    public void joinChat() {
        try {
            // Wait for host success
            gameSpace.get(new ActualField("connectionStatus"), new ActualField("hostSuccess"));

            // Request hostIP
            String chatIP = (String) gameSpace.get(new ActualField("connectionStatus"), new ActualField("chatHostIP"),
                    new FormalField(String.class))[2];
            System.out.println("Joining chat with ip " + chatIP);
            // Initialise connection to chat server
            joinChat = new RemoteSpace("tcp://" + chatIP + ":37333/chat?keep");

            // Give acknowledgement of connection
            joinChat.put("connectionStatus", "joined", id);
            System.out.println("Joined chat");
            // Recieve acknowledgement of connection and get host id
            int hostId = (int) joinChat.get(new ActualField("connectionStatus"), new ActualField("joinGot"),
                    new FormalField(Integer.class))[2];

            // Init chat controller
            chatController = new ChatController(this, hostId, joinChat);
            chatThread = new Thread(chatController);
            chatThread.start();
            System.out.println("Connected to chat!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Hosts a game chat. Waits for guest to join chat.
     */
    public void hostChat() {
        try {
            // Initialise chat server
            hostServer = new SpaceRepository();
            hostChat = new SequentialSpace();
            hostServer.add("chat", hostChat);
            String ip = (InetAddress.getLocalHost().getHostAddress()).trim();
            hostServer.addGate("tcp://" + ip + ":37333/?keep");
            System.out.println("Chat hosted with ip " + ip + " !");
            // Tell game that chat is ready to be joined
            gameSpace.put("request", "hostChat", id);

            // Give connection ip to guest
            gameSpace.put("connectionStatus", "chatHostIP", ip);
            System.out.println("Waiting for join...");
            // Wait for connection from guest and get id
            int guestId = (int) hostChat.get(new ActualField("connectionStatus"), new ActualField("joined"),
                    new FormalField(Integer.class))[2];
            System.out.println("Join got");
            // Give acknowledgement of connection and give id
            hostChat.put("connectionStatus", "joinGot", id);

            // Init chat controller
            chatController = new ChatController(this, guestId, hostChat);
            chatThread = new Thread(chatController);
            chatThread.start();
            System.out.println("Guest joined chat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the opened chat spaces.
     */ 
    public void resetChats() {
        
        //Closing guest chat
        if (joinChat != null) {
            try {
                joinChat.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Closing host chat server
        if (hostServer != null) {
            hostServer.remove("chat");
            hostServer.shutDown();
        }
        
        //Closing thread
        if (chatThread != null) {
            chatThread.interrupt();
        }

        hostChat = null;
        chatController = null;
    }

    /**
     * Request to damage to player.
     * @param damage
     */
    public void damagePlayer(int damage) {
        try {
            gameSpace.put("request", "damage", id); // Send request to damage
            gameSpace.put("data", "damage", damage); // Send damage amount
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests placement of new tower.
     * @param type
     * @param x
     * @param y
     */
    public void placeTower(String type, int x, int y) {
        try {
            gameSpace.put("request", "placeTower", id);
            gameSpace.put("towerInfo", type, x, y);
        } catch (InterruptedException e) {
            System.out.println("This is a problem");
        }
    }

    /**
     * Try to send a wave of enemies to opponent.
     * @param type
     */
    public void sendEnemies(EnemyType type) {
        try {
            gameSpace.put("request", "sendEnemies", id);
            gameSpace.put("data", "sendEnemies", type);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Requests to upgrade a tower.
     * @param towerId
     * @return newUpgradePrice
     */
    public int upgradeTower(int towerId) {
        try {
            gameSpace.put("request", "upgradeTower", id);
            gameSpace.put("towerId", towerId);
            int newPrice = (int) gameSpace.get(new ActualField("towerUpgradeSucces"), new FormalField(Integer.class),
                    new ActualField(towerId))[1];
            return newPrice;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Requests tower stats of towerId from game.
     * @param towerId
     * @return stats
     */
    public String getTowerStats(int towerId) {
        try {
            gameSpace.put("request", "getTowerStats", id);
            gameSpace.put("towerId", towerId);
            String newStats = (String) gameSpace.get(new ActualField("towerStats"), new FormalField(String.class),
                    new ActualField(towerId))[1];
            return newStats;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "-1";
    }


    /**
     * Sells the selected tower.
     * @param towerId
     */
    public void sellTower(int towerId) {
        try {
            gameSpace.put("request", "sellTower", id);
            gameSpace.put("towerId", towerId);
            int opponentId = (int) gameSpace.get(new ActualField("towerSellSuccess"), new ActualField(id),
                    new FormalField(Integer.class), new ActualField(towerId))[2];
            gameSpace.put("gui", "removeTower", towerId, opponentId);
            Controller.removeTower(towerId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resigns the player from the game
     */
    public void resign() {
        try {
            gameSpace.put("request", "resign", id);
        } catch (InterruptedException | NullPointerException e) {
            System.out.println("Gamespace is already closed");
        }
    }

    /**
     * Resigns, closes chat and game space. Called when a player has closed the
     * application.
     */
    public void exitGame() {
        try {

            resign();

            
            resetChats();

            if (gameSpace != null) {
                gameSpace.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the connection to lobby space if opened
     */
    public void disconnectLobby() {

        try {
            if (lobby != null) {
                // exitGame();
                lobby.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getGameID() {
        return gameId;
    }

    /**
     * Send a message to opponent via the game chat.
     * @param msg
     */
    public void sendMessage(String msg) {
        chatController.sendMessage(msg);
    }

}

/**
 * Continuously monitors if the other player has left the game.
 */
class GameMonitor implements Runnable {
    Client client;
    RemoteSpace gameSpace;
    RemoteSpace lobby;
    int gameId;
    int playerId;

    GameMonitor(Client client, RemoteSpace gameSpace, RemoteSpace lobby, int playerId, int gameId) {
        this.client = client;
        this.gameSpace = gameSpace;
        this.lobby = lobby;
        this.playerId = playerId;
        this.gameId = gameId;

    }

    @Override
    public void run() {
        try {
            //Close game if opponent quit.
            gameSpace.get(new ActualField("gameClosed"), new ActualField(playerId));

            System.out.println("Other player left the game");
            Controller.closeGame();

        } catch (InterruptedException e) {

            System.out.println("The game has been closed");
        }
        try {
            //Request lobby to remove game from server.
            lobby.put("request", "closeGame", gameId);
            lobby.get(new ActualField("closedGame"), new ActualField(gameId));
            gameId = -1;

        } catch (InterruptedException e) {
            System.out.println("Lobby not found");
        }
    }
}

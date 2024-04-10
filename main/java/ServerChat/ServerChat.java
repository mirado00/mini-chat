package ServerChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerChat {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();
    private static Map<String, ClientHandler> clientMap = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexion...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion: " + clientSocket);

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientId = reader.readLine();

                ClientHandler clientHandler = new ClientHandler(clientSocket, clientId);
                clients.add(clientHandler);
                clientMap.put(clientId, clientHandler);

                Thread t = new Thread(clientHandler);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public static void sendMessageToClient(String clientId, String message) {
        ClientHandler client = clientMap.get(clientId);
        if (client != null) {
            client.sendMessage(message);
        } else {
            System.out.println("Client " + clientId + " introuvable.");
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
        clientMap.remove(client.getClientId());
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientId;

    public ClientHandler(Socket socket, String clientId) {
        try {
            clientSocket = socket;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.clientId = clientId;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Message reçu de " + clientId + ": " + message);

                if (message.startsWith("@")) {
                    int index = message.indexOf(' ');
                    if (index != -1) {
                        String recipient = message.substring(1, index);
                        String msgContent = message.substring(index + 1);
                        ServerChat.sendMessageToClient(recipient, clientId + " (privé): " + msgContent);
                    }
                } else {
                    ServerChat.broadcastMessage(clientId + ": " + message, this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ServerChat.removeClient(this);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getClientId() {
        return clientId;
    }
}
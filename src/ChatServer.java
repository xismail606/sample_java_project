import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServer {
    private static final int PORT = 5001;
    private ServerSocket serverSocket;
    private Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    private Set<String> usedNames = ConcurrentHashMap.newKeySet();
    private Consumer<String> logConsumer;
    private Consumer<Set<String>> clientListConsumer;
    private volatile boolean running = true;

    public ChatServer(Consumer<String> logConsumer, Consumer<Set<String>> clientListConsumer) {
        this.logConsumer = logConsumer;
        this.clientListConsumer = clientListConsumer;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            log("Chat server started on port " + PORT);
            updateClientList();

            new Thread(() -> {
                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(clientSocket);
                        clients.add(clientHandler);
                        clientHandler.start();
                    } catch (IOException e) {
                        if (running) {
                            log("Error accepting client: " + e.getMessage());
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            log("Error starting server: " + e.getMessage());
        }
    }

    public void stopServer() throws IOException {
        running = false;
        for (ClientHandler client : clients) {
            client.sendMessage("SERVER_STOPPED");
            client.close();
        }
        clients.clear();
        usedNames.clear();
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        log("Server stopped");
        updateClientList();
    }

    private void log(String message) {
        if (logConsumer != null) {
            logConsumer.accept(message);
        }
        System.out.println(message);
    }

    private void updateClientList() {
        Set<String> clientNames = new HashSet<>();
        for (ClientHandler client : clients) {
            if (client.clientName != null) {
                clientNames.add(client.clientName);
            }
        }
        if (clientListConsumer != null) {
            clientListConsumer.accept(clientNames);
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("SUBMIT_NAME");
                String proposedName = in.readLine();

                // Validate and ensure unique name
                if (proposedName == null || proposedName.trim().isEmpty()) {
                    proposedName = "Anonymous";
                }

                clientName = proposedName;
                int counter = 1;
                while (usedNames.contains(clientName)) {
                    clientName = proposedName + counter;
                    counter++;
                }
                usedNames.add(clientName);

                if (!clientName.equals(proposedName)) {
                    out.println("NAME_CHANGED:" + clientName);
                }

                log("New client joined: " + clientName);
                broadcast(clientName + " has joined the chat.", true);
                updateClientList();

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("EXIT")) {
                        break;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    String timestamp = sdf.format(new Date());
                    log("Received: " + message);
                    broadcast(timestamp + " - " + message, false);
                }
            } catch (IOException e) {
                log("Error with client " + clientName + ": " + e.getMessage());
            } finally {
                try {
                    if (clientName != null) {
                        broadcast(clientName + " has left the chat.", true);
                        usedNames.remove(clientName);
                    }
                    clients.remove(this);
                    close();
                    log("Client " + clientName + " left");
                    updateClientList();
                } catch (IOException e) {
                    log("Error closing client connection: " + e.getMessage());
                }
            }
        }

        private void broadcast(String message, boolean isSystemMessage) {
            for (ClientHandler client : clients) {

                if (isSystemMessage) {
                    // System messages go to everyone
                    client.out.println(message);
                } else {
                    // Regular messages go to everyone (including sender)
                    client.out.println(message);
                }
            }
        }

        private void sendMessage(String message) {
            out.println(message);
        }

        private void close() throws IOException {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null && !socket.isClosed())
                socket.close();
        }
    }
}
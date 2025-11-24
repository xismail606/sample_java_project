import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServer {
    private static final int PORT = 5001;
    private ServerSocket serverSocket;
    private HashSet<ClientHandler> clients = new HashSet<>();
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
                        synchronized (clients) {
                            clients.add(clientHandler);
                        }
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
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage("SERVER_STOPPED");
                client.close();
            }
            clients.clear();
        }
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
        synchronized (clients) {
            for (ClientHandler client : clients) {
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
                clientName = in.readLine();
                if (clientName == null || clientName.trim().isEmpty()) {
                    clientName = "Anonymous";
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
                    broadcast(timestamp + " - " + message, message.startsWith(clientName + ": "));
                }
            } catch (IOException e) {
                log("Error with client " + clientName + ": " + e.getMessage());
            } finally {
                try {
                    broadcast(clientName + " has left the chat.", true);
                    synchronized (clients) {
                        clients.remove(this);
                    }
                    close();
                    log("Client " + clientName + " left");
                    updateClientList();
                } catch (IOException e) {
                    log("Error closing client connection: " + e.getMessage());
                }
            }
        }

        private void broadcast(String message, boolean isSystemMessage) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    if (isSystemMessage || client != this) {
                        client.out.println(message);
                    }
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
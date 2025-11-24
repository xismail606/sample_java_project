import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> onMessageReceived;

    public ChatClient(String host, int port, Consumer<String> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 3000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void startClient() {
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("SUBMIT_NAME")) {
                        continue;
                    }
                    onMessageReceived.accept(message);
                }
                onMessageReceived.accept("SERVER_DISCONNECTED");
            } catch (IOException e) {
                System.err.println("Error receiving messages: " + e.getMessage());
                onMessageReceived.accept("SERVER_DISCONNECTED");
            }
        }).start();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendName(String name) {
        out.println(name);
    }

    public void sendExit() {
        out.println("EXIT");
    }

    public void close() throws IOException {
        if (in != null)
            in.close();
        if (out != null)
            out.close();
        if (socket != null && !socket.isClosed())
            socket.close();
    }
}
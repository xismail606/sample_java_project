import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

public class ChatClientGUI extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(ChatClientGUI.class.getName());
    private JTextField messageField;
    private JPanel chatPanel;
    private String name;
    private ChatClient client;
    private JButton sendButton;
    private static final Color MY_BUBBLE_COLOR = new Color(177, 133, 219);
    private static final Color OTHER_BUBBLE_COLOR = new Color(144, 238, 144, 220);
    private static final Font MESSAGE_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 14);
    private static final Font SENDER_FONT = new Font("Segoe UI Emoji", Font.BOLD, 14);
    private static final Font SYSTEM_FONT = new Font("Segoe UI Emoji", Font.ITALIC, 14);
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 5001;

    public ChatClientGUI(String name, String host, int port) {
        super("Chat 606 - " + name);
        this.name = name;
        LOGGER.info("Initializing chat interface for user: " + name);

        setSize(600, 500);
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(30, 30, 30));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        chatPanel = createGradientPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        messageField = new JTextField();
        messageField.setFont(MESSAGE_FONT);
        messageField.setBackground(new Color(45, 45, 45));
        messageField.setForeground(Color.WHITE);
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        messageField.addActionListener(e -> sendMessage());
        messageField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSendButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSendButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSendButton();
            }

            private void updateSendButton() {
                sendButton.setEnabled(!messageField.getText().trim().isEmpty());
            }
        });

        sendButton = createGradientButton("Send", this::sendMessage);
        sendButton.setEnabled(false);

        JButton exitButton = createGradientButton("Exit", this::exitApplication);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(messageField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(sendButton);
        buttonPanel.add(exitButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        setLayout(new BorderLayout(10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        pack();

        connectToServer(host, port);
    }

    public ChatClientGUI(String name) {
        this(name, DEFAULT_HOST, DEFAULT_PORT);
    }

    private JPanel createGradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40), 0, getHeight(),
                        new Color(40, 20, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private JButton createGradientButton(String text, Runnable action) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(167, 29, 251), 0, getHeight(),
                        new Color(100, 149, 237));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(text, x, y);
                g2d.dispose();
            }
        };
        button.setText(text);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void connectToServer(String host, int port) {
        SwingUtilities.invokeLater(() -> addSystemMessage("Connecting to server..."));
        try {
            this.client = new ChatClient(host, port, this::onMessageReceived);
            client.startClient();
            client.sendName(name);
            LOGGER.info("Connected to server");
            addSystemMessage(" Connected as " + name);
        } catch (Exception e) {
            LOGGER.severe("Failed to connect: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to connect: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            Timer timer = new Timer(1000, e1 -> System.exit(1));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && client != null) {
            client.sendMessage(name + ": " + message);
            messageField.setText("");
            sendButton.setEnabled(false);
        }
    }

    private void exitApplication() {
        LOGGER.info("User  " + name + " is leaving");
        if (client != null) {
            try {
                client.sendExit();
                client.close();
            } catch (IOException ex) {
                LOGGER.severe("Error closing: " + ex.getMessage());
            }
        }
        dispose();
        System.exit(0);
    }

    private void onMessageReceived(String message) {
        LOGGER.info("Received: " + message);
        SwingUtilities.invokeLater(() -> {
            if (message.equals("SERVER_DISCONNECTED") || message.equals("SERVER_STOPPED")) {
                addSystemMessage(" Disconnected from server.");
                messageField.setEnabled(false);
                sendButton.setEnabled(false);
            } else if (message.startsWith("USERS: ")) {
                addSystemMessage("👥 Online users: " + message.substring(6));
            } else if (message.contains("joined") || message.contains("left")) {
                addSystemMessage(" Received: " + message);
            } else {
                String[] parts = message.split(" - ", 2);
                String timestamp = parts.length == 2 ? parts[0]
                        : new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date());
                String msgContent = parts.length == 2 ? parts[1] : message;
                addMessageBubble(msgContent, timestamp);
            }
            scrollToBottom();
        });
    }

    private void addMessageBubble(String message, String timestamp) {
        boolean isMyMessage = message.startsWith(name + ": ");
        FlowLayout layout = new FlowLayout(isMyMessage ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 5);

        JPanel bubblePanel = new JPanel(layout);
        bubblePanel.setOpaque(false);
        bubblePanel.setMaximumSize(new Dimension(chatPanel.getWidth() - 20, Short.MAX_VALUE));
        bubblePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel bubble = new JPanel(new BorderLayout(5, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(isMyMessage ? MY_BUBBLE_COLOR : OTHER_BUBBLE_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g2d.setColor(new Color(40, 30, 93));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
            }
        };
        bubble.setOpaque(false);
        bubble.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        int colonIndex = message.indexOf(": ");
        String sender = colonIndex > 0 ? message.substring(0, colonIndex) : name;
        String content = colonIndex > 0 ? message.substring(colonIndex + 2) : message;

        JLabel senderLabel = new JLabel("<html><b style='color: rgba(29, 0, 86, 1);'>" + sender + "</b></html>");
        senderLabel.setFont(SENDER_FONT);

        JLabel messageLabel = new JLabel("<html>" + content.replace("\n", "<br>") + "</html>");
        messageLabel.setFont(MESSAGE_FONT);

        JLabel timestampLabel = new JLabel("<html><i style='color: black;'>" + timestamp + "</i></html>");
        timestampLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));

        bubble.add(senderLabel, BorderLayout.NORTH);
        bubble.add(messageLabel, BorderLayout.CENTER);
        bubble.add(timestampLabel, BorderLayout.SOUTH);

        bubblePanel.add(bubble);
        chatPanel.add(bubblePanel);
        chatPanel.add(Box.createVerticalStrut(10));
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private void addSystemMessage(String message) {
        JPanel systemPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        systemPanel.setOpaque(false);
        systemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        systemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel systemLabel = new JLabel("<html><i style='color: red;'>" + message + "</i></html>");
        systemLabel.setFont(SYSTEM_FONT);

        systemPanel.add(systemLabel);
        chatPanel.add(systemPanel);
        chatPanel.add(Box.createVerticalStrut(10));
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) chatPanel.getParent().getParent();
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    public static void main(String[] args) {
        String name = args.length > 0 ? args[0] : "User";
        String host = args.length > 1 ? args[1] : DEFAULT_HOST;
        int port = args.length > 2 ? Integer.parseInt(args[2]) : DEFAULT_PORT;
        SwingUtilities.invokeLater(() -> new ChatClientGUI(name, host, port).setVisible(true));
    }
}
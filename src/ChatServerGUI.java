import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class ChatServerGUI extends JFrame {
    private JTextArea logArea;
    private JButton stopButton;
    private JButton startButton;
    private JList<String> clientList;
    private DefaultListModel<String> clientListModel;
    private ChatServer server;
    private static final Font LOG_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 14);

    public ChatServerGUI() {
        super("606 ChatApp - Server");
        setSize(700, 450);
        setMinimumSize(new Dimension(500, 350));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40),
                        0, getHeight(), new Color(40, 20, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // ✅ إصلاح LogArea - إزالة paintComponent المخصص والاعتماد على JTextArea العادي
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(LOG_FONT);
        logArea.setForeground(new Color(255, 215, 0)); // Gold color
        logArea.setBackground(new Color(30, 30, 50));
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 150), 2));
        scrollPane.getViewport().setBackground(new Color(30, 30, 50));

        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        clientList.setFont(LOG_FONT);
        clientList.setForeground(new Color(144, 238, 144));
        clientList.setBackground(new Color(30, 30, 50));
        clientList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane clientScrollPane = new JScrollPane(clientList);
        clientScrollPane.setPreferredSize(new Dimension(180, 0));
        clientScrollPane.getViewport().setBackground(new Color(30, 30, 50));
        clientScrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 150), 2));

        // Title for client list
        JLabel clientListTitle = new JLabel("👥 Connected Users");
        clientListTitle.setForeground(new Color(255, 215, 0));
        clientListTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        clientListTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel clientPanel = new JPanel(new BorderLayout(5, 5));
        clientPanel.setOpaque(false);
        clientPanel.add(clientListTitle, BorderLayout.NORTH);
        clientPanel.add(clientScrollPane, BorderLayout.CENTER);

        stopButton = createGradientButton("Stop Server");
        stopButton.addActionListener(e -> stopServer());

        startButton = createGradientButton("Start Server");
        startButton.addActionListener(e -> startServer());
        startButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(clientPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        server = new ChatServer(this::appendLog, this::updateClientList);
        server.startServer();
        appendLog("✅ Chat server started on port 5001");
    }

    private JButton createGradientButton(String text) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(167, 29, 251),
                        0, getHeight(), new Color(100, 149, 237));
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(130, 35));
        return button;
    }

    public void appendLog(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            logArea.append("[" + timestamp + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void updateClientList(Set<String> clients) {
        SwingUtilities.invokeLater(() -> {
            clientListModel.clear();
            if (clients.isEmpty()) {
                clientListModel.addElement("(No users connected)");
            } else {
                clients.forEach(client -> clientListModel.addElement("🟢 " + client));
            }
        });
    }

    private void startServer() {
        server = new ChatServer(this::appendLog, this::updateClientList);
        server.startServer();
        appendLog("✅ Chat server started on port 5001");
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    private void stopServer() {
        try {
            server.stopServer();
            appendLog("❌ Server stopped");
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        } catch (IOException e) {
            appendLog("⚠️ Error stopping server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ChatServerGUI().setVisible(true));
    }
}
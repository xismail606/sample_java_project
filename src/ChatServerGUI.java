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
        setSize(600, 400);
        setMinimumSize(new Dimension(400, 300));
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
        setContentPane(mainPanel);

        logArea = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 60),
                        0, getHeight(), new Color(60, 20, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                g2d.drawString(getText(), 5, getFontMetrics(getFont()).getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        logArea.setEditable(false);
        logArea.setFont(LOG_FONT);
        logArea.setForeground(new Color(255, 0, 0));
        logArea.setOpaque(false);
        logArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);

        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 60),
                        0, getHeight(), new Color(60, 20, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        clientList.setFont(LOG_FONT);
        clientList.setForeground(new Color(255, 0, 0));
        clientList.setOpaque(false);
        JScrollPane clientScrollPane = new JScrollPane(clientList);
        clientScrollPane.setPreferredSize(new Dimension(150, 0));
        clientScrollPane.getViewport().setOpaque(false);
        clientScrollPane.setBorder(BorderFactory.createEmptyBorder());

        stopButton = createGradientButton("Stop Server");
        stopButton.addActionListener(e -> stopServer());

        startButton = createGradientButton("Start Server");
        startButton.addActionListener(e -> startServer());
        startButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(clientScrollPane, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        server = new ChatServer(this::appendLog, this::updateClientList);
        server.startServer();
        appendLog("Chat server started on port 5001");
    }

    private JButton createGradientButton(String text) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
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
            clients.forEach(clientListModel::addElement);
        });
    }

    private void startServer() {
        server = new ChatServer(this::appendLog, this::updateClientList);
        server.startServer();
        appendLog("Chat server started on port 5001");
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    private void stopServer() {
        try {
            server.stopServer();
            appendLog("Server stopped");
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        } catch (IOException e) {
            appendLog("Error stopping server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatServerGUI().setVisible(true));
    }
}

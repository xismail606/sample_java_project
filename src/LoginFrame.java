import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JButton joinButton;
    private JButton exitButton;
    private JLabel logoLabel;
    private JLabel validationLabel;

    private float scale = 1.0f;
    private boolean growing = true;

    private boolean isJoinButtonHovered = false;
    private boolean isExitButtonHovered = false;

    private static final int MIN_USERNAME_LENGTH = 2;
    private static final int MAX_USERNAME_LENGTH = 15;

    public LoginFrame() {
        super("Login - 606 ChatApp");

        setTitle("606 ChatApp - Login");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 25, 80), 0, getHeight(),
                        new Color(50, 20, 90));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40);

        // --------------- Animated "606" Logo ---------------

        logoLabel = createAnimatedLogo();
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 15, 20);
        panel.add(logoLabel, gbc);

        // ----------Username Field---------------

        usernameField = createUsernameField();
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 40, 5, 40);
        panel.add(usernameField, gbc);

        // Validation Label
        validationLabel = new JLabel(" ");
        validationLabel.setForeground(new Color(255, 100, 100));
        validationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        validationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 40, 10, 40);
        panel.add(validationLabel, gbc);

        // ---------------Buttons Panel---------------

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonsPanel.setOpaque(false);

        joinButton = createJoinButton();
        exitButton = createExitButton();

        buttonsPanel.add(joinButton);
        buttonsPanel.add(exitButton);

        gbc.gridy = 3;
        gbc.insets = new Insets(10, 40, 20, 40);
        panel.add(buttonsPanel, gbc);

        startAnimation();
    }

    private void joinChat() {
        String username = usernameField.getText().trim();

        if (username.isEmpty() || username.equals("Enter username")) {
            showValidationError("Please enter a username");
            return;
        }

        if (username.length() < MIN_USERNAME_LENGTH) {
            showValidationError("Username too short (min " + MIN_USERNAME_LENGTH + " characters)");
            return;
        }

        if (username.length() > MAX_USERNAME_LENGTH) {
            showValidationError("Username too long (max " + MAX_USERNAME_LENGTH + " characters)");
            return;
        }

        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            showValidationError("Only letters, numbers, and underscore allowed");
            return;
        }

        validationLabel.setText(" ");
        setUIEnabled(false);

        SwingWorker<Boolean, Void> connector = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try (Socket testSocket = new Socket()) {
                    testSocket.connect(new InetSocketAddress("127.0.0.1", 5001), 3000);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        System.out.println("Successfully connected as: " + username);

                        ChatClientGUI chatGUI = new ChatClientGUI(username);
                        chatGUI.setVisible(true);

                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "Cannot connect to the server. Please ensure the server is running.",
                                "Connection Error", JOptionPane.ERROR_MESSAGE);
                        setUIEnabled(true);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "An unexpected error occurred: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    setUIEnabled(true);
                }
            }
        };
        connector.execute();
    }

    private void showValidationError(String message) {
        validationLabel.setText("⚠️ " + message);
        usernameField.requestFocus();
    }

    private void setUIEnabled(boolean enabled) {
        usernameField.setEnabled(enabled);
        joinButton.setEnabled(enabled);
        joinButton.setText(enabled ? "Join Chat" : "Connecting...");
    }

    private JLabel createAnimatedLogo() {
        JLabel logo = new JLabel("606") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Font scaledFont = getFont().deriveFont(getFont().getSize2D() * scale);
                g2d.setFont(scaledFont);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.drawString(getText(), x + 3, y + 3);
                g2d.setColor(new Color(220, 180, 255));
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
        logo.setFont(new Font("Arial", Font.BOLD, 80));
        logo.setPreferredSize(new Dimension(200, 120));
        return logo;
    }

    private void startAnimation() {
        Timer animationTimer = new Timer(50, e -> {
            if (growing) {
                scale += 0.01f;
                if (scale >= 1.05f)
                    growing = false;
            } else {
                scale -= 0.01f;
                if (scale <= 1.0f)
                    growing = true;
            }
            logoLabel.repaint();
        });
        animationTimer.start();
    }

    private JTextField createUsernameField() {
        JTextField field = new JTextField("Enter username") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setPaint(getBackground());
                    g2.fill(((RoundedBorder) getBorder()).getBorderShape(0, 0, getWidth() - 1, getHeight() - 1));
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBackground(new Color(255, 255, 255, 30));
        field.setForeground(Color.LIGHT_GRAY);
        field.setCaretColor(Color.WHITE);
        field.setOpaque(false);
        field.setBorder(new RoundedBorder(15));

        // DocumentListener MAX_USERNAME_LENGTH
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (field.getText().length() >= MAX_USERNAME_LENGTH && e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    showValidationError("Maximum length is " + MAX_USERNAME_LENGTH + " characters");
                } else if (validationLabel.getText().contains("Maximum")) {
                    validationLabel.setText(" ");
                }
            }
        });

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals("Enter username")) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText("Enter username");
                    field.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        field.addActionListener(e -> joinChat());
        return field;
    }

    private JButton createJoinButton() {
        JButton button = new JButton("Join Chat") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = isJoinButtonHovered
                        ? new GradientPaint(0, 0, new Color(180, 80, 255), 0, getHeight(), new Color(120, 160, 240))
                        : new GradientPaint(0, 0, new Color(167, 29, 251), 0, getHeight(), new Color(100, 149, 237));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        configureButton(button);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isJoinButtonHovered = true;
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isJoinButtonHovered = false;
                button.repaint();
            }
        });
        button.addActionListener(e -> joinChat());
        return button;
    }

    private JButton createExitButton() {
        JButton button = new JButton("Exit") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = isExitButtonHovered
                        ? new GradientPaint(0, 0, new Color(120, 120, 120), 0, getHeight(), new Color(80, 80, 80))
                        : new GradientPaint(0, 0, new Color(90, 90, 90), 0, getHeight(), new Color(50, 50, 50));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        configureButton(button);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isExitButtonHovered = true;
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isExitButtonHovered = false;
                button.repaint();
            }
        });
        button.addActionListener(e -> System.exit(0));
        return button;
    }

    private void configureButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 40));
    }

    private static class RoundedBorder implements javax.swing.border.Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius / 2, this.radius, this.radius / 2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        }

        public Shape getBorderShape(int x, int y, int w, int h) {
            return new java.awt.geom.RoundRectangle2D.Double(x, y, w, h, radius, radius);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
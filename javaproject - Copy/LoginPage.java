import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton, forgotPasswordButton;
    
    public LoginPage() {
        setTitle("Farmer Support System - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(66, 139, 202);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Title Label
        JLabel titleLabel = new JLabel("Farmer Support System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBounds(50, 30, 300, 40);
        mainPanel.add(titleLabel);
        
        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setBounds(50, 100, 100, 20);
        mainPanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(50, 130, 280, 30);
        mainPanel.add(usernameField);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(50, 180, 100, 20);
        mainPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 210, 280, 30);
        mainPanel.add(passwordField);
        
        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(50, 270, 280, 40);
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        mainPanel.add(loginButton);
        
        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBounds(50, 330, 280, 40);
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        mainPanel.add(registerButton);
        
        // Forgot Password Button
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setBounds(50, 390, 280, 40);
        forgotPasswordButton.setBackground(new Color(155, 89, 182));
        forgotPasswordButton.setForeground(Color.BLACK);
        forgotPasswordButton.setFocusPainted(false);
        mainPanel.add(forgotPasswordButton);
        
        // Add action listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegistration());
        forgotPasswordButton.addActionListener(e -> openForgotPassword());
        
        add(mainPanel);
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                // Open Dashboard
                Dashboard dashboard = new Dashboard(username);
                dashboard.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegistration() {
        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.setVisible(true);
        this.dispose();
    }
    
    private void openForgotPassword() {
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();
        forgotPasswordPage.setVisible(true);
    }
} 
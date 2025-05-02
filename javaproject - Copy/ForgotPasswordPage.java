import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ForgotPasswordPage extends JFrame {
    private JTextField usernameField, emailField;
    private JPasswordField newPasswordField, confirmPasswordField;
    private JButton resetButton, backButton;
    
    public ForgotPasswordPage() {
        setTitle("Farmer Support System - Forgot Password");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                Color color1 = new Color(155, 89, 182);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Title Label
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBounds(50, 30, 300, 40);
        mainPanel.add(titleLabel);
        
        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setBounds(50, 80, 100, 20);
        mainPanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(50, 110, 280, 30);
        mainPanel.add(usernameField);
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setBounds(50, 150, 100, 20);
        mainPanel.add(emailLabel);
        
        emailField = new JTextField();
        emailField.setBounds(50, 180, 280, 30);
        mainPanel.add(emailField);
        
        // New Password
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        newPasswordLabel.setBounds(50, 220, 150, 20);
        mainPanel.add(newPasswordLabel);
        
        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(50, 250, 280, 30);
        mainPanel.add(newPasswordField);
        
        // Confirm New Password
        JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordLabel.setBounds(50, 290, 200, 20);
        mainPanel.add(confirmPasswordLabel);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(50, 320, 280, 30);
        mainPanel.add(confirmPasswordField);
        
        // Reset Button
        resetButton = new JButton("Reset Password");
        resetButton.setBounds(50, 370, 280, 40);
        resetButton.setBackground(new Color(46, 204, 113));
        resetButton.setForeground(Color.BLACK);
        resetButton.setFocusPainted(false);
        mainPanel.add(resetButton);
        
        // Back Button
        backButton = new JButton("Back to Login");
        backButton.setBounds(50, 420, 280, 40);
        backButton.setBackground(new Color(231, 76, 60));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        mainPanel.add(backButton);
        
        // Add action listeners
        resetButton.addActionListener(e -> handlePasswordReset());
        backButton.addActionListener(e -> dispose());
        
        add(mainPanel);
    }
    
    private void handlePasswordReset() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (username.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "UPDATE users SET password = ? WHERE username = ? AND email = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, newPassword);
            pst.setString(2, username);
            pst.setString(3, email);
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Password reset successful!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or email!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Password reset failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 
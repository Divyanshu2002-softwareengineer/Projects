import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Date;

public class RegistrationPage extends JFrame {
    private JTextField usernameField, emailField, phoneField, locationField, addressField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, backButton;
    private JComboBox<String> genderComboBox;
    private JSpinner dobSpinner;
    
    public RegistrationPage() {
        setTitle("Registration");
        setSize(420, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Gradient background panel (like LoginPage)
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(66, 139, 202);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        gradientPanel.setLayout(null);
        
        // Card panel for the form
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBounds(30, 30, 350, 600);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        int y = 20;
        
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBounds(60, y, 250, 30);
        cardPanel.add(titleLabel);
        
        y += 50;
        cardPanel.add(makeLabel("Username:", 20, y));
        usernameField = makeTextField(120, y);
        cardPanel.add(usernameField);
        
        y += 45;
        cardPanel.add(makeLabel("Email:", 20, y));
        emailField = makeTextField(120, y);
        cardPanel.add(emailField);
        
        y += 45;
        cardPanel.add(makeLabel("Phone:", 20, y));
        phoneField = makeTextField(120, y);
        cardPanel.add(phoneField);
        
        y += 45;
        cardPanel.add(makeLabel("Location:", 20, y));
        locationField = makeTextField(120, y);
        cardPanel.add(locationField);
        
        y += 45;
        cardPanel.add(makeLabel("Address:", 20, y));
        addressField = makeTextField(120, y);
        cardPanel.add(addressField);
        
        y += 45;
        cardPanel.add(makeLabel("Gender:", 20, y));
        String[] genders = {"Select Gender", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(120, y, 200, 28);
        cardPanel.add(genderComboBox);
        
        y += 45;
        cardPanel.add(makeLabel("Date of Birth:", 20, y));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dobSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "dd/MM/yyyy");
        dobSpinner.setEditor(dateEditor);
        dobSpinner.setBounds(120, y, 200, 28);
        cardPanel.add(dobSpinner);
        
        y += 45;
        cardPanel.add(makeLabel("Password:", 20, y));
        passwordField = new JPasswordField();
        passwordField.setBounds(120, y, 200, 28);
        cardPanel.add(passwordField);
        
        y += 45;
        cardPanel.add(makeLabel("Confirm:", 20, y));
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(120, y, 200, 28);
        cardPanel.add(confirmPasswordField);
        
        y += 50;
        registerButton = new JButton("Register");
        registerButton.setBounds(20, y, 300, 40);
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        cardPanel.add(registerButton);
        
        y += 50;
        backButton = new JButton("Back");
        backButton.setBounds(20, y, 300, 40);
        backButton.setBackground(new Color(231, 76, 60));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        cardPanel.add(backButton);
        
        gradientPanel.add(cardPanel);
        setContentPane(gradientPanel);
        
        // Add action listeners
        registerButton.addActionListener(e -> handleRegistration());
        backButton.addActionListener(e -> goBackToLogin());
    }
    
    private JLabel makeLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(44, 62, 80));
        label.setBounds(x, y, 100, 28);
        return label;
    }
    
    private JTextField makeTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 200, 28);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    private void handleRegistration() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String location = locationField.getText();
        String address = addressField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        Date dob = (Date) dobSpinner.getValue();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || 
            location.isEmpty() || address.isEmpty() || gender.equals("Select Gender")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO users (username, email, phone, location, address, gender, dob, password) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, location);
            pst.setString(5, address);
            pst.setString(6, gender);
            pst.setDate(7, new java.sql.Date(dob.getTime()));
            pst.setString(8, password);
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            goBackToLogin();
            
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void goBackToLogin() {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
        this.dispose();
    }
} 
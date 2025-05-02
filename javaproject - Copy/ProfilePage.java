import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProfilePage extends JPanel {
    private String username;
    private JTextField usernameField, emailField, phoneField, locationField, addressField;
    private JComboBox<String> genderComboBox;
    private JSpinner dobSpinner;
    private JButton updateButton;
    
    public ProfilePage(String username) {
        this.username = username;
        setLayout(new GridBagLayout()); // Center everything
        setBackground(new Color(236, 240, 241));
        
        // Profile Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        // Profile Icon
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("images/profile/user.png"); // Place a user icon at this path
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            iconLabel.setText("[User]");
        }
        cardPanel.add(iconLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        
        // Title
        JLabel titleLabel = new JLabel("Profile Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        
        // Username
        cardPanel.add(createLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setEditable(false);
        cardPanel.add(createTextField(usernameField));
        
        // Email
        cardPanel.add(createLabel("Email:"));
        emailField = new JTextField();
        cardPanel.add(createTextField(emailField));
        
        // Phone
        cardPanel.add(createLabel("Phone:"));
        phoneField = new JTextField();
        cardPanel.add(createTextField(phoneField));
        
        // Location
        cardPanel.add(createLabel("Location:"));
        locationField = new JTextField();
        cardPanel.add(createTextField(locationField));
        
        // Address
        cardPanel.add(createLabel("Address:"));
        addressField = new JTextField();
        cardPanel.add(createTextField(addressField));
        
        // Gender
        cardPanel.add(createLabel("Gender:"));
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setMaximumSize(new Dimension(300, 30));
        genderComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(genderComboBox);
        
        // Date of Birth
        cardPanel.add(createLabel("Date of Birth:"));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dobSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "dd/MM/yyyy");
        dobSpinner.setEditor(dateEditor);
        dobSpinner.setMaximumSize(new Dimension(300, 30));
        dobSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(dobSpinner);
        
        // Update Button
        updateButton = new JButton("Update Profile");
        updateButton.setBackground(new Color(46, 204, 113));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setMaximumSize(new Dimension(200, 40));
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(e -> updateProfile());
        
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(updateButton);
        
        // Center the card panel using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(cardPanel, gbc);
        
        // Load user data
        loadUserData();
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(8, 0, 2, 0));
        return label;
    }
    
    private JTextField createTextField(JTextField field) {
        field.setMaximumSize(new Dimension(300, 30));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    private void loadUserData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                locationField.setText(rs.getString("location"));
                addressField.setText(rs.getString("address"));
                genderComboBox.setSelectedItem(rs.getString("gender"));
                dobSpinner.setValue(rs.getDate("dob"));
            }
            
            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProfile() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "UPDATE users SET email=?, phone=?, location=?, address=?, gender=?, dob=? WHERE username=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, emailField.getText());
            pst.setString(2, phoneField.getText());
            pst.setString(3, locationField.getText());
            pst.setString(4, addressField.getText());
            pst.setString(5, (String) genderComboBox.getSelectedItem());
            pst.setDate(6, new java.sql.Date(((java.util.Date) dobSpinner.getValue()).getTime()));
            pst.setString(7, username);
            
            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No changes made.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 
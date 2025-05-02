import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Dashboard extends JFrame {
    private String username;
    private JPanel mainPanel;
    private JPanel sidePanel;
    private JPanel contentPanel;
    
    public Dashboard(String username) {
        this.username = username;
        setTitle("Farmer Support System - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        
        // Side panel
        createSidePanel();
        
        // Content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        showWelcomePanel();
        
        // Add panels to main panel
        mainPanel.add(sidePanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void createSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(220, 700));
        sidePanel.setBackground(new Color(44, 62, 80));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        // Padding at the top
        sidePanel.add(Box.createVerticalStrut(30));
        
        // User info panel
        JPanel userPanel = new JPanel();
        userPanel.setBackground(new Color(44, 62, 80));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        
        // User icon
        JLabel userIcon = new JLabel();
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("images/profile/user.png");
            Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            userIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            userIcon.setText("[User]");
        }
        userPanel.add(userIcon);
        userPanel.add(Box.createVerticalStrut(10));
        
        JLabel userLabel = new JLabel("Welcome,");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        userPanel.add(userLabel);
        userPanel.add(Box.createVerticalStrut(5));
        userPanel.add(usernameLabel);
        
        sidePanel.add(userPanel);
        sidePanel.add(Box.createVerticalStrut(30));
        
        // Menu buttons
        String[] menuItems = {
            "Dashboard",
            "Profile",
            "Crop Management",
            "Weather Info",
            "Market Prices",
            "Government Schemes",
            "Market",
            "Logout"
        };
        
        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item);
            sidePanel.add(menuButton);
            sidePanel.add(Box.createVerticalStrut(10));
        }
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(44, 62, 80));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 45));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> handleMenuClick(text));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 73, 94));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(44, 62, 80));
            }
        });
        
        return button;
    }
    
    private void handleMenuClick(String menuItem) {
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
        
        switch (menuItem) {
            case "Dashboard":
                showWelcomePanel();
                break;
            case "Profile":
                showProfilePanel();
                break;
            case "Crop Management":
                showCropManagementPanel();
                break;
            case "Weather Info":
                showWeatherPanel();
                break;
            case "Market Prices":
                showMarketPricesPanel();
                break;
            case "Government Schemes":
                showGovernmentSchemesPanel();
                break;
            case "Market":
                showMarketPanel();
                break;
            case "Logout":
                handleLogout();
                break;
        }
    }
    
    private void showWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Farmer Support System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Quick stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Card 1: Profile
        statsPanel.add(createStatCard("Profile", "View", () -> {
            contentPanel.removeAll();
            showProfilePanel();
            contentPanel.revalidate();
            contentPanel.repaint();
        }));
        
        // Card 2: Weather
        statsPanel.add(createStatCard("Weather", "Sunny", () -> {
            contentPanel.removeAll();
            showWeatherPanel();
            contentPanel.revalidate();
            contentPanel.repaint();
        }));
        
        // Card 3: Market Updates
        statsPanel.add(createStatCard("Market Updates", "5", () -> {
            contentPanel.removeAll();
            showMarketPricesPanel();
            contentPanel.revalidate();
            contentPanel.repaint();
        }));
        
        // Card 4: Government Schemes
        statsPanel.add(createStatCard("Government Schemes", "Info", () -> {
            contentPanel.removeAll();
            showGovernmentSchemesPanel();
            contentPanel.revalidate();
            contentPanel.repaint();
        }));
        
        welcomePanel.add(statsPanel, BorderLayout.CENTER);
        contentPanel.add(welcomePanel);
    }
    
    private JPanel createStatCard(String title, String value, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(236, 240, 241));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        
        if (onClick != null) {
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onClick.run();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(new Color(220, 220, 220));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBackground(new Color(236, 240, 241));
                }
            });
        }
        
        return card;
    }
    
    private void showProfilePanel() {
        ProfilePage profilePage = new ProfilePage(username);
        contentPanel.add(profilePage);
    }
    
    private void showCropManagementPanel() {
        CropManagementPage cropPage = new CropManagementPage(username);
        contentPanel.add(cropPage);
    }
    
    private void showWeatherPanel() {
        WeatherPage weatherPage = new WeatherPage();
        contentPanel.add(weatherPage);
    }
    
    private void showMarketPricesPanel() {
        MarketPricesPage marketPage = new MarketPricesPage();
        contentPanel.add(marketPage);
    }
    
    private void showGovernmentSchemesPanel() {
        GovernmentSchemesPage schemesPage = new GovernmentSchemesPage();
        contentPanel.add(schemesPage);
    }
    
    private void showMarketPanel() {
        Market marketPage = new Market();
        contentPanel.add(marketPage);
    }
    
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
            this.dispose();
        }
    }
} 
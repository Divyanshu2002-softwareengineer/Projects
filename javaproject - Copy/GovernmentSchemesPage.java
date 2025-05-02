import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GovernmentSchemesPage extends JPanel {
    private JPanel schemesPanel;
    private boolean showAlternate = false; // Toggle for demo refresh

    public GovernmentSchemesPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title and Refresh Button Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Government Schemes for Farmers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.add(refreshButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        schemesPanel = new JPanel();
        schemesPanel.setLayout(new GridLayout(0, 2, 20, 20)); // 2 columns, variable rows
        schemesPanel.setBackground(Color.WHITE);
        schemesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(schemesPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Initial load
        displaySchemes(getSchemes());

        // Refresh button action
        refreshButton.addActionListener(e -> {
            showAlternate = !showAlternate; // Toggle for demo
            displaySchemes(showAlternate ? getAlternateSchemes() : getSchemes());
        });
    }

    private void displaySchemes(List<Scheme> schemes) {
        schemesPanel.removeAll();
        for (Scheme scheme : schemes) {
            schemesPanel.add(createSchemeCard(scheme));
        }
        schemesPanel.revalidate();
        schemesPanel.repaint();
    }

    private JPanel createSchemeCard(Scheme scheme) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(236, 240, 241));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (scheme.imagePath != null && !scheme.imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(scheme.imagePath);
                Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                imageLabel.setText("[No Image]");
            }
        } else {
            imageLabel.setText("[No Image]");
        }
        card.add(imageLabel, BorderLayout.NORTH);

        // Title
        JLabel nameLabel = new JLabel(scheme.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        card.add(nameLabel, BorderLayout.CENTER);

        // Short Description
        JTextArea descArea = new JTextArea(scheme.description.length() > 80 ? scheme.description.substring(0, 80) + "..." : scheme.description);
        descArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setBorder(null);
        card.add(descArea, BorderLayout.SOUTH);

        // Add click listener to show details
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSchemeDetailsDialog(scheme);
            }
        });

        return card;
    }

    private void showSchemeDetailsDialog(Scheme scheme) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), scheme.name, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (scheme.imagePath != null && !scheme.imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(scheme.imagePath);
                Image img = icon.getImage().getScaledInstance(300, 180, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                imageLabel.setText("[No Image]");
            }
        } else {
            imageLabel.setText("[No Image]");
        }
        dialog.add(imageLabel, BorderLayout.NORTH);

        // Description
        JTextArea descArea = new JTextArea(scheme.description);
        descArea.setFont(new Font("Arial", Font.PLAIN, 15));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(null);
        dialog.add(descScroll, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private List<Scheme> getSchemes() {
        List<Scheme> schemes = new ArrayList<>();
        schemes.add(new Scheme(
            "Pradhan Mantri Fasal Bima Yojana",
            "Crop insurance scheme for farmers to protect against crop loss due to natural calamities. The scheme aims to provide financial support to farmers suffering crop loss/damage arising out of unforeseen events, stabilize the income of farmers, and encourage them to adopt innovative and modern agricultural practices.",
            "images/schemes/pmfby.jpg"
        ));
        schemes.add(new Scheme(
            "Pradhan Mantri Krishi Sinchai Yojana",
            "Aims to improve irrigation and water use efficiency for farmers. The scheme focuses on ensuring access to some means of protective irrigation to all agricultural farms in the country, to produce 'per drop more crop'.",
            "images/schemes/pmksy.jpg"
        ));
        schemes.add(new Scheme(
            "Soil Health Card Scheme",
            "Provides soil health cards to farmers with crop-wise recommendations of nutrients and fertilizers required for the individual farms to help them improve productivity through judicious use of inputs.",
            "images/schemes/soilhealth.jpg"
        ));
        schemes.add(new Scheme(
            "Kisan Credit Card",
            "Provides timely access to credit for farmers for their cultivation and other needs. The scheme aims to provide adequate and timely credit support from the banking system under a single window for the farmers for their cultivation and other needs.",
            "images/schemes/kcc.jpg"
        ));
        return schemes;
    }

    // Alternate schemes for demo refresh
    private List<Scheme> getAlternateSchemes() {
        List<Scheme> schemes = new ArrayList<>();
        schemes.add(new Scheme(
            "PM Kisan Samman Nidhi",
            "Provides income support of Rs. 6,000 per year to all farmer families across the country in three equal installments.",
            "images/schemes/pmkisan.jpg"
        ));
        schemes.add(new Scheme(
            "National Agriculture Market (eNAM)",
            "A pan-India electronic trading portal which networks the existing APMC mandis to create a unified national market for agricultural commodities.",
            "images/schemes/enam.jpg"
        ));
        schemes.add(new Scheme(
            "Rashtriya Krishi Vikas Yojana",
            "Aims to achieve 4% annual growth in agriculture through development of Agriculture and its allied sectors.",
            "images/schemes/rkvy.jpg"
        ));
        schemes.add(new Scheme(
            "Paramparagat Krishi Vikas Yojana",
            "Promotes organic farming and the adoption of organic village clusters to improve soil health and organic produce.",
            "images/schemes/pkvy.jpg"
        ));
        return schemes;
    }

    // Helper class for scheme data
    private static class Scheme {
        String name, description, imagePath;
        Scheme(String name, String description, String imagePath) {
            this.name = name;
            this.description = description;
            this.imagePath = imagePath;
        }
    }
} 
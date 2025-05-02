import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;

import java.util.ArrayList;
import java.util.List;


public class Market extends JPanel {
    public Market() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Farming Market");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(0, 3, 20, 20)); // 3 columns, variable rows
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (MarketItem item : getMarketItems()) {
            itemsPanel.add(createItemCard(item));
        }

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createItemCard(MarketItem item) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(236, 240, 241));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (item.imagePath != null && !item.imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(item.imagePath);
                Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                imageLabel.setText("[No Image]");
            }
        } else {
            imageLabel.setText("[No Image]");
        }
        card.add(imageLabel, BorderLayout.NORTH);

        // Name and Price
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(236, 240, 241));

        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("₹" + item.price);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Buy Button
        JButton buyButton = new JButton("Buy");
        buyButton.setFont(new Font("Arial", Font.BOLD, 14));
        buyButton.setBackground(new Color(46, 204, 113));
        buyButton.setForeground(Color.BLACK);
        buyButton.setFocusPainted(false);
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Thank you for showing interest in buying " + item.name + "!\nOur representative will contact you soon.", "Purchase", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(236, 240, 241));
        buttonPanel.add(buyButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private List<MarketItem> getMarketItems() {
        List<MarketItem> items = new ArrayList<>();
        items.add(new MarketItem("Tractor", 350000, "images/market/tractor.jpg"));
        items.add(new MarketItem("Fertilizer (50kg)", 1200, "images/market/fertilizer.jpg"));
        items.add(new MarketItem("Seeds (Wheat, 10kg)", 800, "images/market/seeds.jpg"));
        items.add(new MarketItem("Irrigation Pump", 7500, "images/market/pump.jpg"));
        items.add(new MarketItem("Pesticide (5L)", 950, "images/market/pesticide.jpg"));
        items.add(new MarketItem("Hand Tools Set", 1500, "images/market/tools.jpg"));
        // Add more items as needed
        return items;
    }

    private static class MarketItem {
        String name;
        int price;
        String imagePath;
        MarketItem(String name, int price, String imagePath) {
            this.name = name;
            this.price = price;
            this.imagePath = imagePath;
        }
    }
} 
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class MarketPricesPage extends JPanel {
    private JTable priceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cropTypeComboBox;
    private JButton refreshButton;
    private Timer updateTimer;
    
    public MarketPricesPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Market Prices");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Update timer (every 1 hour)
        updateTimer = new Timer(60 * 60 * 1000, e -> loadPriceData());
        updateTimer.start();
        
        // Initial data load
        loadPriceData();
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        
        // Crop type filter
        JLabel filterLabel = new JLabel("Filter by Crop Type:");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        String[] cropTypes = {"All", "Grains", "Vegetables", "Fruits", "Pulses", "Other"};
        cropTypeComboBox = new JComboBox<>(cropTypes);
        cropTypeComboBox.setPreferredSize(new Dimension(150, 30));
        cropTypeComboBox.addActionListener(e -> loadPriceData());
        
        // Refresh button
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadPriceData());
        
        panel.add(filterLabel);
        panel.add(cropTypeComboBox);
        panel.add(refreshButton);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create table model
        String[] columns = {"Crop Name", "Type", "Min Price (₹/kg)", "Max Price (₹/kg)", "Avg Price (₹/kg)", "Last Updated"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        priceTable = new JTable(tableModel);
        priceTable.setRowHeight(30);
        priceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        // Custom renderer for price columns
        TableCellRenderer priceRenderer = new PriceRenderer();
        priceTable.getColumnModel().getColumn(2).setCellRenderer(priceRenderer);
        priceTable.getColumnModel().getColumn(3).setCellRenderer(priceRenderer);
        priceTable.getColumnModel().getColumn(4).setCellRenderer(priceRenderer);
        
        JScrollPane scrollPane = new JScrollPane(priceTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadPriceData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM market_prices";
            String selectedType = (String) cropTypeComboBox.getSelectedItem();
            
            if (!selectedType.equals("All")) {
                query += " WHERE crop_type = ?";
            }
            
            PreparedStatement pst = conn.prepareStatement(query);
            if (!selectedType.equals("All")) {
                pst.setString(1, selectedType);
            }
            
            ResultSet rs = pst.executeQuery();
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("crop_name"));
                row.add(rs.getString("crop_type"));
                row.add(rs.getDouble("min_price"));
                row.add(rs.getDouble("max_price"));
                row.add(rs.getDouble("avg_price"));
                row.add(rs.getTimestamp("last_updated"));
                tableModel.addRow(row);
            }
            
            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading market prices: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Custom renderer for price columns
    private static class PriceRenderer extends JLabel implements TableCellRenderer {
        public PriceRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            if (value instanceof Number) {
                setText(String.format("₹%.2f", ((Number) value).doubleValue()));
            } else {
                setText(value != null ? value.toString() : "");
            }
            
            return this;
        }
    }
} 
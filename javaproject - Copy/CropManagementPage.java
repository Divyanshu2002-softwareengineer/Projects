import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class CropManagementPage extends JPanel {
    private String username;
    private JTable cropTable;
    private DefaultTableModel tableModel;
    private JTextField cropNameField, areaField, plantingDateField;
    private JComboBox<String> cropTypeComboBox;
    private JButton addButton, updateButton, deleteButton;
    
    public CropManagementPage(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Crop Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create main panel with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        
        // Form panel (left side)
        JPanel formPanel = createFormPanel();
        splitPane.setLeftComponent(formPanel);
        
        // Table panel (right side)
        JPanel tablePanel = createTablePanel();
        splitPane.setRightComponent(tablePanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Load initial data
        loadCropData();
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Crop Name
        panel.add(createLabel("Crop Name:"));
        cropNameField = new JTextField();
        panel.add(createTextField(cropNameField));
        
        // Crop Type
        panel.add(createLabel("Crop Type:"));
        String[] cropTypes = {"Select Type", "Grains", "Vegetables", "Fruits", "Pulses", "Other"};
        cropTypeComboBox = new JComboBox<>(cropTypes);
        cropTypeComboBox.setMaximumSize(new Dimension(200, 30));
        panel.add(cropTypeComboBox);
        
        // Area
        panel.add(createLabel("Area (in acres):"));
        areaField = new JTextField();
        panel.add(createTextField(areaField));
        
        // Planting Date
        panel.add(createLabel("Planting Date:"));
        plantingDateField = new JTextField();
        panel.add(createTextField(plantingDateField));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        addButton = createButton("Add Crop", new Color(46, 204, 113));
        updateButton = createButton("Update", new Color(52, 152, 219));
        deleteButton = createButton("Delete", new Color(231, 76, 60));
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonPanel);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createTextField(JTextField field) {
        field.setMaximumSize(new Dimension(200, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create table model
        String[] columns = {"Crop Name", "Type", "Area (acres)", "Planting Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cropTable = new JTable(tableModel);
        cropTable.setRowHeight(28);
        cropTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        cropTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        cropTable.getSelectionModel().addListSelectionListener(e -> {
            int row = cropTable.getSelectedRow();
            if (row >= 0) {
                cropNameField.setText((String) tableModel.getValueAt(row, 0));
                cropTypeComboBox.setSelectedItem(tableModel.getValueAt(row, 1));
                areaField.setText(tableModel.getValueAt(row, 2).toString());
                plantingDateField.setText(tableModel.getValueAt(row, 3).toString());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(cropTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadCropData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT crop_name, crop_type, area, planting_date FROM crops WHERE username = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            
            ResultSet rs = pst.executeQuery();
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("crop_name"));
                row.add(rs.getString("crop_type"));
                row.add(rs.getDouble("area"));
                row.add(rs.getDate("planting_date"));
                tableModel.addRow(row);
            }
            
            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading crops: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addCrop() {
        if (validateInput()) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "INSERT INTO crops (username, crop_name, crop_type, area, planting_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                
                pst.setString(1, username);
                pst.setString(2, cropNameField.getText());
                pst.setString(3, (String) cropTypeComboBox.getSelectedItem());
                pst.setDouble(4, Double.parseDouble(areaField.getText()));
                pst.setDate(5, java.sql.Date.valueOf(plantingDateField.getText()));
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Crop added successfully!");
                clearForm();
                loadCropData();
                
                pst.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding crop: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateCrop() {
        int selectedRow = cropTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crop to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (validateInput()) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "UPDATE crops SET crop_name = ?, crop_type = ?, area = ?, planting_date = ? WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                
                pst.setString(1, cropNameField.getText());
                pst.setString(2, (String) cropTypeComboBox.getSelectedItem());
                pst.setDouble(3, Double.parseDouble(areaField.getText()));
                pst.setDate(4, java.sql.Date.valueOf(plantingDateField.getText()));
                pst.setInt(5, (int) tableModel.getValueAt(selectedRow, 0));
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Crop updated successfully!");
                clearForm();
                loadCropData();
                
                pst.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating crop: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteCrop() {
        int selectedRow = cropTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crop to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this crop?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "DELETE FROM crops WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                
                pst.setInt(1, (int) tableModel.getValueAt(selectedRow, 0));
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Crop deleted successfully!");
                clearForm();
                loadCropData();
                
                pst.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting crop: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validateInput() {
        if (cropNameField.getText().isEmpty() || cropTypeComboBox.getSelectedIndex() == 0 || 
            areaField.getText().isEmpty() || plantingDateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Double.parseDouble(areaField.getText());
            java.sql.Date.valueOf(plantingDateField.getText());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid area or date format", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void clearForm() {
        cropNameField.setText("");
        cropTypeComboBox.setSelectedIndex(0);
        areaField.setText("");
        plantingDateField.setText("");
        cropTable.clearSelection();
    }
} 
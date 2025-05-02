import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WeatherPage extends JPanel {
    private JLabel temperatureValue, humidityValue, windSpeedValue, weatherConditionValue;
    private JLabel weatherIconLabel;
    private JPanel currentWeatherPanel, forecastPanel;
    private Timer updateTimer;
    private final String API_KEY = "0204e4aba989cce8433e1a4286036fc7"; // Replace with your OpenWeather API key
    private String userLocation = "Delhi"; // Default location
    private JTextField locationInput;
    private JButton searchButton;
    
    public WeatherPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // --- Combined North Panel ---
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBackground(new Color(41, 128, 185));
        
        // Location Input Panel
        JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        locationPanel.setBackground(new Color(41, 128, 185));
        
        JLabel locationLabel = new JLabel("Enter Location:");
        locationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        locationLabel.setForeground(Color.WHITE);
        
        locationInput = new JTextField(userLocation, 15);
        locationInput.setFont(new Font("Arial", Font.PLAIN, 16));
        
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);
        
        locationPanel.add(locationLabel);
        locationPanel.add(locationInput);
        locationPanel.add(searchButton);
        
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Weather Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Add both panels to the northPanel
        northPanel.add(locationPanel);
        northPanel.add(titlePanel);
        
        add(northPanel, BorderLayout.NORTH);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Current Weather Panel
        currentWeatherPanel = createCurrentWeatherPanel();
        contentPanel.add(currentWeatherPanel);
        
        // Forecast Panel
        forecastPanel = createForecastPanel();
        contentPanel.add(forecastPanel);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // --- Search Button Action ---
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newLocation = locationInput.getText().trim();
                if (!newLocation.isEmpty()) {
                    userLocation = newLocation;
                    updateWeatherData();
                }
            }
        });
        
        // Update timer (every 30 minutes)
        updateTimer = new Timer(30 * 60 * 1000, e -> updateWeatherData());
        updateTimer.start();
        
        // Initial data load
        updateWeatherData();
    }
    
    private JPanel createCurrentWeatherPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Weather icon
        weatherIconLabel = new JLabel();
        weatherIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(weatherIconLabel, BorderLayout.NORTH);
        
        // Weather info grid
        JPanel infoGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        infoGrid.setBackground(new Color(236, 240, 241));
        
        // Create and add weather info components
        JPanel tempPanel = createWeatherInfoPanel("Temperature");
        temperatureValue = (JLabel) tempPanel.getComponent(2);
        infoGrid.add(tempPanel);
        
        JPanel humidityPanel = createWeatherInfoPanel("Humidity");
        humidityValue = (JLabel) humidityPanel.getComponent(2);
        infoGrid.add(humidityPanel);
        
        JPanel windPanel = createWeatherInfoPanel("Wind Speed");
        windSpeedValue = (JLabel) windPanel.getComponent(2);
        infoGrid.add(windPanel);
        
        JPanel conditionPanel = createWeatherInfoPanel("Condition");
        weatherConditionValue = (JLabel) conditionPanel.getComponent(2);
        infoGrid.add(conditionPanel);
        
        panel.add(infoGrid, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createWeatherInfoPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(236, 240, 241));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel("Loading...");
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(valueLabel);
        
        return panel;
    }
    
    private JPanel createForecastPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel forecastTitle = new JLabel("5-Day Forecast");
        forecastTitle.setFont(new Font("Arial", Font.BOLD, 18));
        forecastTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(forecastTitle, BorderLayout.NORTH);
        
        JPanel forecastGrid = new JPanel(new GridLayout(1, 5, 10, 0));
        forecastGrid.setBackground(new Color(236, 240, 241));
        
        // Add placeholder forecast days
        for (int i = 0; i < 5; i++) {
            forecastGrid.add(createForecastDayPanel("...", "...", "..."));
        }
        
        panel.add(forecastGrid, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createForecastDayPanel(String day, String temp, String condition) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(236, 240, 241));
        
        JLabel dayLabel = new JLabel(day);
        dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel tempLabel = new JLabel(temp);
        tempLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel conditionLabel = new JLabel(condition);
        conditionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        conditionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(dayLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(tempLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(conditionLabel);
        
        return panel;
    }
    
    private void updateWeatherData() {
        try {
            // Current weather API call
            String currentWeatherUrl = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                userLocation, API_KEY
            );
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(currentWeatherUrl))
                .build();
            
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    JSONObject data = new JSONObject(response);
                    
                    SwingUtilities.invokeLater(() -> {
                        try {
                            JSONObject main = data.getJSONObject("main");
                            JSONObject wind = data.getJSONObject("wind");
                            JSONArray weather = data.getJSONArray("weather");
                            JSONObject weatherData = weather.getJSONObject(0);
                            
                            // Update current weather values
                            temperatureValue.setText(String.format("%.1f°C", main.getDouble("temp")));
                            humidityValue.setText(main.getInt("humidity") + "%");
                            windSpeedValue.setText(String.format("%.1f km/h", wind.getDouble("speed") * 3.6));
                            weatherConditionValue.setText(weatherData.getString("main"));
                            
                            // Update weather icon
                            String iconCode = weatherData.getString("icon");
                            updateWeatherIcon(iconCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
            
            // Forecast API call
            String forecastUrl = String.format(
                "https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric",
                userLocation, API_KEY
            );
            
            HttpRequest forecastRequest = HttpRequest.newBuilder()
                .uri(URI.create(forecastUrl))
                .build();
            
            client.sendAsync(forecastRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    JSONObject data = new JSONObject(response);
                    JSONArray list = data.getJSONArray("list");
                    
                    SwingUtilities.invokeLater(() -> updateForecastPanel(list));
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateWeatherIcon(String iconCode) {
        try {
            String iconUrl = String.format("http://openweathermap.org/img/w/%s.png", iconCode);
            ImageIcon icon = new ImageIcon(new URL(iconUrl));
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            weatherIconLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateForecastPanel(JSONArray forecastData) {
        JPanel forecastGrid = (JPanel) ((JPanel) forecastPanel.getComponent(1));
        forecastGrid.removeAll();
        
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        for (int i = 0; i < 5; i++) {
            JSONObject dayData = forecastData.getJSONObject(i * 8); // Get data for each day
            JSONObject main = dayData.getJSONObject("main");
            JSONArray weather = dayData.getJSONArray("weather");
            JSONObject weatherData = weather.getJSONObject(0);
            
            Date date = new Date(dayData.getLong("dt") * 1000);
            String day = sdf.format(date);
            double temp = main.getDouble("temp");
            String condition = weatherData.getString("main");
            
            forecastGrid.add(createForecastDayPanel(
                day,
                String.format("%.1f°C", temp),
                condition
            ));
        }
        
        forecastGrid.revalidate();
        forecastGrid.repaint();
    }
} 
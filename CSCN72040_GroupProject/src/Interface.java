import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Window.Type;
/**
 * This class creates the main user interface for the Weather Application.
 * It sets up the GUI components like text fields, labels, and buttons, 
 * and handles the actions to retrieve and display weather data.
 */
public class Interface extends JFrame {
	// Field to store weather data in JSON format
	private JSONObject weatherData;
	 /**
     * Constructor that initializes the WeatherAppGui frame and its components.
     */
    public Interface(){
    	// Set the title of the JFrame
        super("SkyCast");
        setResizable(false);

        // Ensure the application exits when the JFrame is closed

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set the initial size of the JFrame
        setSize(649, 764);

        // Position the JFrame in the center of the screen
        setLocationRelativeTo(null);

        // Disable the use of a layout manager to allow absolute positioning of components
        getContentPane().setLayout(null);
        // Add the GUI components to the JFrame
        addGuiComponents();
    }
    /**
     * This method adds all necessary GUI components to the frame.
     */
    private void addGuiComponents(){
        // search field
        JTextField searchTextField = new JTextField();

        // set the location and size of our component
        searchTextField.setBounds(85, 18, 351, 45);

        // change the font style and size
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));

        getContentPane().add(searchTextField);

        // Label for displaying the weather condition image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(154, 74, 337, 156);
        getContentPane().add(weatherConditionImage);

        // Label for displaying the temperature
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(95, 228, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 45));

        // center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(temperatureText);

        // Label for displaying the weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(105, 293, 450, 45);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 30));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(weatherConditionDesc);
        
        // Label for displaying the humidity icon
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(63, 360, 74, 66);
        getContentPane().add(humidityImage);
        

     // Label for displaying the humidity value
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(48, 414, 177, 75);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        getContentPane().add(humidityText);
        
        // Label for displaying the UV index
        JLabel uvIndexText = new JLabel("<html><b>UV Index</b> 0</html>");
        uvIndexText.setBounds(495, 424, 138, 55);
        uvIndexText.setFont(new Font("Dialog", Font.PLAIN, 16));
        getContentPane().add(uvIndexText);
        
        JLabel uvIndexImage = new JLabel(loadImage("src/assets/uv-index.png"));
        uvIndexImage.setBounds(495, 360, 74, 66);
        getContentPane().add(uvIndexImage);
        
        // Label for displaying sunrise icon
        JLabel sunriseImage = new JLabel(loadImage("src/assets/sunrise.png"));
        sunriseImage.setBounds(181, 512, 74, 66);
        getContentPane().add(sunriseImage);

        // Label for displaying the sunrise time
        JLabel sunriseText = new JLabel("<html><b>Sunrise</b> 06:00 AM</html>");
        sunriseText.setBounds(153, 577, 161, 55);
        sunriseText.setFont(new Font("Dialog", Font.PLAIN, 16));
        getContentPane().add(sunriseText);

        // Label for displaying sunset icon
        JLabel sunsetImage = new JLabel(loadImage("src/assets/sunset.png"));
        sunsetImage.setBounds(443, 512, 74, 66);
        getContentPane().add(sunsetImage);

        // Label for displaying the sunset time
        JLabel sunsetText = new JLabel("<html><b>Sunset</b> 06:00 PM</html>");
        sunsetText.setBounds(413, 577, 170, 55);
        sunsetText.setFont(new Font("Dialog", Font.PLAIN, 16));
        getContentPane().add(sunsetText);
        
        
    

        // Label for displaying the wind speed icon
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(281, 360, 74, 66);
        getContentPane().add(windspeedImage);
        
        // Label for displaying the wind speed value
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(251, 424, 185, 55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        getContentPane().add(windspeedText);

        // Button that initiates a search for weather data
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        // change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(523, 18, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get location from user
                String userInput = searchTextField.getText();

                // validate input - remove whitespace to ensure non-empty text
                if(userInput.replaceAll("\\s", "").length() <= 0){
                    return;
                }

             // Assuming WeatherApp.getWeatherData is a method that fetches weather data for the specified location
                weatherData = DataManagement.getWeatherData(userInput);

                // update gui*********************************
              
                String weatherCondition = (String) weatherData.get("weather_condition");

                // depending on the condition, we will update the weather image that corresponds with the condition
                switch(weatherCondition){
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                // update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " C");

                // update weather condition text
                weatherConditionDesc.setText(weatherCondition);

                // update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");


                // update uvindex text
                double uvIndex = (double) weatherData.get("uv_index");
                uvIndexText.setText("<html><b>UV Index</b> " + uvIndex + "</html>");
                
                // update sunrise text
                String sunriseTime = (String) weatherData.get("sunrise");
                sunriseText.setText("<html><b>Sunrise</b> " + sunriseTime + "</html>");

                // update sunset text
                String sunsetTime = (String) weatherData.get("sunset");
                sunsetText.setText("<html><b>Sunset</b> " + sunsetTime + "</html>");

                
                // update windspeed text
                double windspeed = (double) weatherData.get("windspeed");
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");
                
                
            }
        });
        getContentPane().add(searchButton);
        

    }

    /**
     * Helper method to load an image from the filesystem and create an ImageIcon.
     *
     * @param resourcePath Path to the image resource.
     * @return ImageIcon object created from the image at the given path, or null if an error occurs.
     */
    private ImageIcon loadImage(String resourcePath){
        try{
            // read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // returns an image icon so that our component can render it
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
            
        }

        System.out.println("Could not find resource");
        return null;
    }
}










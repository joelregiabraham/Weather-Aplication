# Weather App GUI

## Introduction

The Weather App is a Java-based application designed to offer users real-time weather information for a specified location. It interacts with an external API to retrieve weather data and presents it through a graphical user interface (GUI). Users can input a location, and the application fetches and displays weather details such as temperature, weather condition, humidity, and wind speed. This documentation provides insights into the project's architecture, utilized technologies, and the functionality of each class within the application.

![image](https://github.com/TechNerd-019/CSCN72040_GroupProject/assets/122996489/0fe52af3-7012-4ce5-8667-fc4d8ebc5c4e)


## Technologies Used

The Weather App leverages the following technologies and libraries:

- Java 18
- [JSON Simple](https://code.google.com/archive/p/json-simple/downloads): Utilized for parsing and processing JSON data.
- [HTTPURLConnection](https://docs.oracle.com/en/java/javase/11/docs/api/java.net/java/net/HttpURLConnection.html): Java's built-in library for making HTTP requests to fetch data from external APIs.

## Class Summaries

### 3.1. AppLauncher

**Description:** The AppLauncher class serves as the entry point for the Weather App. It initializes the GUI and displays the main application window.

### 3.2. WeatherAppGui

**Description:** The WeatherAppGui class represents the graphical user interface (GUI) of the Weather App. It is responsible for displaying weather information for a specified location.

**Summary:** This class manages the layout and presentation of GUI components, including text fields, labels, buttons, and images. It implements the user interface for entering a location and updating weather information based on user input.

### 3.3. WeatherApp

**Description:** The WeatherApp class contains the backend logic for fetching weather data from an external API. It retrieves geographic coordinates for a location, fetches weather data for that location, and provides methods to interpret weather codes.

**Summary:** This class encapsulates the core functionality of the Weather App. It includes methods for fetching weather data and location coordinates, converting weather codes into readable weather conditions, and managing API requests. Acting as a bridge between the GUI and the external weather data source, it ensures accurate retrieval and display of weather information.

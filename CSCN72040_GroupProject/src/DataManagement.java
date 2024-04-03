import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Backend logic for fetching weather data from an external API.
 */
public class DataManagement {

    /**
     * Fetches weather data for a given location.
     *
     * @param locationName The name of the location.
     * @return JSONObject containing weather data.
     */
    public static JSONObject getWeatherData(String locationName) {
        // Get location coordinates using the geolocation API
        JSONArray locationData = getLocationData(locationName);

        // Extract latitude and longitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // Build API request URL with location coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&daily=uv_index_max,sunrise,sunset&timezone=America%2FNew_York";

        try {
            // Call API and get response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // Check for response status
            // 200 - means that the connection was a success
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }

            // Store resulting JSON data
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                // Read and store into the string builder
                resultJson.append(scanner.nextLine());
            }

            // Close scanner
            scanner.close();

            // Close URL connection
            conn.disconnect();

            // Parse through the data
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            // Retrieve daily data
            JSONObject daily = (JSONObject) resultJsonObj.get("daily");

            // Retrieve hourly data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            // Get current hour's data
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            // Get UV index
            JSONArray uvIndexData = (JSONArray) daily.get("uv_index_max");
            double uvIndex = (double) uvIndexData.get(0);

            // Retrieve sunrise and sunset data
            JSONArray sunriseData = (JSONArray) daily.get("sunrise");
            JSONArray sunsetData = (JSONArray) daily.get("sunset");

            // Get sunrise and sunset
            String sunriseTime = (String) sunriseData.get(0);
            String sunsetTime = (String) sunsetData.get(0);

            // Get temperature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // Get weather code
            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            // Get humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            // Get windspeed
            JSONArray windspeedData = (JSONArray) hourly.get("windspeed_10m");
            double windspeed = (double) windspeedData.get(index);

            // Build the weather JSON data object
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);
            weatherData.put("uv_index", uvIndex);
            weatherData.put("sunrise", sunriseTime);
            weatherData.put("sunset", sunsetTime);

            return weatherData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves geographic coordinates for a given location name.
     *
     * @param locationName The name of the location.
     * @return JSONArray containing location data.
     */
    public static JSONArray getLocationData(String locationName) {
        // Replace any whitespace in location name to + to adhere to API's request format
        locationName = locationName.replaceAll(" ", "+");

        // Build API url with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try {
            // Call API and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // Check response status
            // 200 means successful connection
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            } else {
                // Store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // Read and store the resulting JSON data into our string builder
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                // Close scanner
                scanner.close();

                // Close URL connection
                conn.disconnect();

                // Parse the JSON string into a JSON object
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                // Get the list of location data the API generated from the location name
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Couldn't find location
        return null;
    }

    /**
     * Fetches API response for the given URL.
     *
     * @param urlString The URL to fetch response from.
     * @return HttpURLConnection object representing the API response.
     */
    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            // Attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method to get
            conn.setRequestMethod("GET");

            // Connect to the API
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Could not make connection
        return null;
    }

    /**
     * Finds the index of the current time in the given time list.
     *
     * @param timeList The list of times.
     * @return Index of the current time.
     */
    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        // Iterate through the time list and see which one matches our current time
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                // Return the index
                return i;
            }
        }

        return 0;
    }

    /**
     * Gets the current time in a specific format.
     *
     * @return String representing the current time.
     */
    private static String getCurrentTime() {
        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format date to be 2023-09-02T00:00 (this is how it is read in the API)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // Format and print the current date and time
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    /**
     * Converts the weather code to a readable string.
     *
     * @param weathercode The weather code.
     * @return String representing the weather condition.
     */
    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";
        if (weathercode == 0L) {
            // Clear
            weatherCondition = "Clear";
        } else if (weathercode > 0L && weathercode <= 3L) {
            // Cloudy
            weatherCondition = "Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L)
                || (weathercode >= 80L && weathercode <= 99L)) {
            // Rain
            weatherCondition = "Rain";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            // Snow
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}

package services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherService {

    private static final String API_KEY = "fed963eb512cec09db815efeeb64db5d"; // Replace with your OpenWeatherMap API key
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";

    public String getWeatherForDate(String city, LocalDate date) throws IOException {
        // Format the date as required by the API (if needed)
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Build the API request URL
        String requestUrl = API_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";

        // Make the API request
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Read the response
        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        return response.toString();
    }
}
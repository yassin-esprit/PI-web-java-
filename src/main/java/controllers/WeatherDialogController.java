package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WeatherDialogController {

    @FXML
    private Label cityLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label conditionLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label windLabel;

    public void setWeatherData(String city, String date, String temperature, String condition, String humidity, String windSpeed, String iconUrl) {
        cityLabel.setText(city);
        dateLabel.setText(date);
        temperatureLabel.setText(temperature + "°C");
        conditionLabel.setText(condition);
        humidityLabel.setText("Humidity: " + humidity + "%");
        windLabel.setText("Wind: " + windSpeed + " km/h");

        // Load weather icon
        if (iconUrl != null && !iconUrl.isEmpty()) {
            weatherIcon.setImage(new Image(iconUrl));
        }
    }
}
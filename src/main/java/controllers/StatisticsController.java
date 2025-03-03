package controllers;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import models.evenement;
import services.ServiceEvenement;
import services.ServiceParticipation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsController {

    @FXML
    private PieChart pieChart; // PieChart for event types

    @FXML
    private BarChart<String, Number> participationChart; // BarChart for event participations

    private ServiceEvenement serviceEvenement = new ServiceEvenement();
    private ServiceParticipation serviceParticipation = new ServiceParticipation();

    @FXML
    public void initialize() throws SQLException {
        // Load event type statistics
        loadEventTypeStatistics();

        // Load event participation statistics
        loadEventParticipationStatistics();

        // Apply Fade-in Animation to PieChart
        FadeTransition fadePie = new FadeTransition(Duration.seconds(1.5), pieChart);
        fadePie.setFromValue(0);
        fadePie.setToValue(1);
        fadePie.play();

        // Apply Fade-in Animation to BarChart
        FadeTransition fadeBar = new FadeTransition(Duration.seconds(1.5), participationChart);
        fadeBar.setFromValue(0);
        fadeBar.setToValue(1);
        fadeBar.play();
    }

    private void loadEventTypeStatistics() throws SQLException {
        List<evenement> evenements = serviceEvenement.recuperer();
        Map<String, Integer> typeCounts = new HashMap<>();

        // Count the number of events for each type
        for (evenement e : evenements) {
            String type = e.getType().name();
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
        }

        // Create PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : typeCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Set the data to the PieChart
        pieChart.setData(pieChartData);
    }

    private void loadEventParticipationStatistics() throws SQLException {
        // Fetch participation data
        Map<String, Integer> participationCounts = serviceParticipation.getParticipationCounts();

        // Create BarChart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Participations");

        for (Map.Entry<String, Integer> entry : participationCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Set the data to the BarChart
        participationChart.getData().add(series);
    }

    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementdash.fxml"));
            Parent root = loader.load();
            pieChart.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
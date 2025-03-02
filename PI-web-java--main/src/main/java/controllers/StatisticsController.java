package controllers;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import models.evenement;
import services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsController {

    @FXML
    private PieChart pieChart; // Changed from BarChart to PieChart

    private ServiceEvenement serviceEvenement = new ServiceEvenement();

//    @FXML
//    public void initialize() {
//        try {
//            loadStatistics();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert("Erreur", "Une erreur s'est produite lors du chargement des statistiques.", Alert.AlertType.ERROR);
//        }
//    }

    public void initialize() throws SQLException {
        // Sample Data (Replace with actual data)
        loadStatistics();
        // Apply Fade-in Animation
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), pieChart);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        // Apply Rotation Animation (Optional)
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), pieChart);
        rotate.setByAngle(360);
        rotate.play();
    }



    private void loadStatistics() throws SQLException {
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

        // Optional: Add styling to the PieChart
        pieChart.setTitle("Répartition des Types d'Événements");
        pieChart.setLegendVisible(true);
        pieChart.setClockwise(true);
        pieChart.setLabelsVisible(true);
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
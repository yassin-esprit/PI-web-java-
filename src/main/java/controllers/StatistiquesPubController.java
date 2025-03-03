package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import models.publicites;
import models.statut_pub;
import services.ServicePublicite;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class StatistiquesPubController {

    @FXML
    private Label labelStatistiques; // Label for active/inactive stats

    @FXML
    private VBox chartContainer; // Container for likes/dislikes charts

    private Map<publicites, Integer> likesMap;
    private Map<publicites, Integer> dislikesMap;

    private ServicePublicite servicePublicites = new ServicePublicite();

    public void initialize() {
        // Display active/inactive stats (old functionality)
        displayActiveInactiveStats();
    }

    // Method to initialize data (likes and dislikes maps)
    public void initData(Map<publicites, Integer> likesMap, Map<publicites, Integer> dislikesMap) {
        this.likesMap = likesMap;
        this.dislikesMap = dislikesMap;
        displayCharts(); // Display likes/dislikes charts (new functionality)
    }

    // Old functionality: Display active/inactive stats
    private void displayActiveInactiveStats() {
        try {
            // Récupérer les publicités actives et inactives
            List<publicites> actives = servicePublicites.recupererParStatut(statut_pub.active);
            List<publicites> inactives = servicePublicites.recupererParStatut(statut_pub.inactif);

            // Afficher les statistiques
            labelStatistiques.setText("Publicités actives : " + actives.size() + "\nPublicités inactives : " + inactives.size());
        } catch (SQLException e) {
            e.printStackTrace();
            labelStatistiques.setText("Erreur de récupération des statistiques.");
        }
    }

    // New functionality: Display likes/dislikes charts
    private void displayCharts() {
        // Clear the container before adding new charts
        chartContainer.getChildren().clear();

        // Create a BarChart for likes and dislikes
        BarChart<String, Number> barChart = createBarChart();
        chartContainer.getChildren().add(barChart);

        // Create a PieChart for total likes and dislikes
        PieChart pieChart = createPieChart();
        chartContainer.getChildren().add(pieChart);
    }

    private BarChart<String, Number> createBarChart() {
        // Define axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Publicités");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre de Likes/Dislikes");

        // Create BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Likes et Dislikes par Publicité");
        barChart.setPrefSize(600, 300); // Ajuster la taille du BarChart

        // Add data to the BarChart
        XYChart.Series<String, Number> likesSeries = new XYChart.Series<>();
        likesSeries.setName("Likes");
        XYChart.Series<String, Number> dislikesSeries = new XYChart.Series<>();
        dislikesSeries.setName("Dislikes");

        for (Map.Entry<publicites, Integer> entry : likesMap.entrySet()) {
            publicites pub = entry.getKey();
            int likes = entry.getValue();
            int dislikes = dislikesMap.getOrDefault(pub, 0);

            likesSeries.getData().add(new XYChart.Data<>(pub.getTitre_pub(), likes));
            dislikesSeries.getData().add(new XYChart.Data<>(pub.getTitre_pub(), dislikes));
        }

        barChart.getData().addAll(likesSeries, dislikesSeries);
        return barChart;
    }

    private PieChart createPieChart() {
        // Create PieChart
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Total Likes et Dislikes");
        pieChart.setPrefSize(400, 300); // Ajuster la taille du PieChart

        // Calculate total likes and dislikes
        int totalLikes = likesMap.values().stream().mapToInt(Integer::intValue).sum();
        int totalDislikes = dislikesMap.values().stream().mapToInt(Integer::intValue).sum();

        // Add data to the PieChart
        pieChart.getData().add(new PieChart.Data("Likes", totalLikes));
        pieChart.getData().add(new PieChart.Data("Dislikes", totalDislikes));

        return pieChart;
    }}
package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import models.campagnes_publicitaires;
import models.statut_camp;
import services.ServiceCamp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatCampController {

    @FXML
    private BarChart<String, Number> statChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private final ServiceCamp serviceCamp = new ServiceCamp();

    public void afficherStatistiques() {
        try {
            List<campagnes_publicitaires> campagnes = serviceCamp.recuperer();

            // Compter le nombre de campagnes par statut
            Map<statut_camp, Long> stats = campagnes.stream()
                    .collect(Collectors.groupingBy(campagnes_publicitaires::getStatut_camp, Collectors.counting()));

            // Effacer les anciennes données
            statChart.getData().clear();

            // Ajouter une série par statut avec une couleur différente
            for (statut_camp statut : statut_camp.values()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(statut.name());
                series.getData().add(new XYChart.Data<>(statut.name(), stats.getOrDefault(statut, 0L)));

                statChart.getData().add(series);
            }

            // Charger le style CSS
            statChart.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



//package controllers;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.chart.BarChart;
//import javafx.scene.chart.CategoryAxis;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import models.campagnes_publicitaires;
//import models.statut_camp;
//import services.ServiceCamp;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class StatCampController {
//
//    @FXML
//    private BarChart<String, Number> statChart;
//
//    @FXML
//    private CategoryAxis xAxis;
//
//    @FXML
//    private NumberAxis yAxis;
//
//    private final ServiceCamp serviceCamp = new ServiceCamp();
//
//    public void afficherStatistiques() {
//        try {
//            List<campagnes_publicitaires> campagnes = serviceCamp.recuperer();
//
//            // Compter le nombre de campagnes par statut
//            Map<statut_camp, Long> stats = campagnes.stream()
//                    .collect(Collectors.groupingBy(campagnes_publicitaires::getStatut_camp, Collectors.counting()));
//
//            // Effacer les anciennes données
//            statChart.getData().clear();
//
//            // Ajouter les nouvelles données au graphique
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            for (statut_camp statut : statut_camp.values()) {
//                series.getData().add(new XYChart.Data<>(statut.name(), stats.getOrDefault(statut, 0L)));
//            }
//
//            statChart.getData().add(series);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}

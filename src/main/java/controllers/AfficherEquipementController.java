package controllers;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Equipement;
import services.ServiceEquipement;

public class AfficherEquipementController {





    @FXML
    private ListView<Equipement> listViewEquipement;

    private ObservableList<Equipement> equipements = FXCollections.observableArrayList();  // Liste d'objets Equipement





    // Méthode pour mettre à jour la liste des équipements
    public void setEquipements(List<Equipement> equipementsList) {
        this.equipements.clear();
        this.equipements.addAll(equipementsList);
        listViewEquipement.setItems(this.equipements);  // Mettre à jour le ListView
    }

    // Méthode pour mettre à jour la liste des équipements
    public void updateEquipementList() {
        ServiceEquipement service = new ServiceEquipement();
        try {
            // Récupérer la liste des équipements à partir du service
            List<Equipement> equipementsList = service.recuperer();
            setEquipements(equipementsList);  // Utiliser setEquipements pour mettre à jour la ListView
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

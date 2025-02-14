package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AfficherEvenementController {

    @FXML
    private TextField rtitre;

    @FXML
    private TextField rtype;

    @FXML
    private TextField rlist;

    public void setRTitre(String rtitre) {
        this.rtitre.setText(rtitre);
    }

    public void setRType(String rtype) {
        this.rtype.setText(rtype);
    }

    public void setRList(String rlist) {
        this.rlist.setText(rlist);
    }
}

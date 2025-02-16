package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AfficherPersonnecontroller {

    @FXML
    private TextField rlist;

    @FXML
    private TextField rnom;

    @FXML
    private TextField rprenom;

    public void setRlist(String rlist) {
        this.rlist.setText(rlist);
    }

    public void setRnom(String rnom) {
        this.rnom.setText(rnom);
    }

    public void setRprenom(String rprenom) {
        this.rprenom.setText(rprenom);
    }
}

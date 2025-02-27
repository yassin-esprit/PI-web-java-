package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ForgotPassword {

    @FXML
    private TextField email;

    @FXML
    void backtologinpage(MouseEvent event) throws IOException {
        SceneController.loadLoginScene();

    }

    @FXML
    void passrecovery(MouseEvent event) {

    }

}

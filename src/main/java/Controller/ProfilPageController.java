package Controller;

import Models.User;
import Tools.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ProfilPageController {

    @FXML
    private Label email;

    @FXML
    private Label fname;

    @FXML
    private Label lname;

    public void initialize() {
        UserSession session = UserSession.getInstance();
        User currentUser = session.getUser();

// Use the data
        System.out.println(currentUser.getFirstName());
        fname.setText(session.getUser().getFirstName());
        lname.setText(session.getUser().getLastName());
        email.setText(session.getUser().getEmail());
    }

    @FXML
    void adresspage(MouseEvent event) throws IOException {
        SceneController.loadAdressScene();
    }
    @FXML
    void editpage(MouseEvent event) throws IOException {
        SceneController.loadProfileEditScene();

    }

    @FXML
    void gototsetting(MouseEvent event) throws IOException {
        SceneController.loadSettingPageScene();

    }
    @FXML
    void gotoprofile(MouseEvent event) throws IOException {
        SceneController.loadProfilePageScene();

    }
    @FXML
    void logout(MouseEvent event) throws IOException {
        UserSession session = UserSession.getInstance();
        session.cleanUserSession();
        SceneController.loadLoginScene();
    }


    public void gotosetting(MouseEvent mouseEvent) {
    }
}

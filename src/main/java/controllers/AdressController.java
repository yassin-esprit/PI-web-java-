package controllers;

import Models.Adress;
import Models.User;
import Services.AdressServices;
import Tools.MyDB;
import Tools.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AdressController {
private AdressServices adress = new AdressServices();
    @FXML
    private PasswordField adressline;

    @FXML
    private TextField city;

    @FXML
    private TextField country;

    @FXML
    private Hyperlink loginurl;

    @FXML
    private TextField postalcode;

    @FXML
    private Button singupbt;

    @FXML
    private TextField state;

    @FXML
    void skip(ActionEvent event) throws IOException {

        SceneController.loadBackToProfileScene();

    }
    @FXML
    private Label welcomeText;
    UserSession session = UserSession.getInstance();
    User currentUser = session.getUser();
// Use the data


    @FXML
    void addadress(ActionEvent event) throws SQLException {
        Connection cnx = MyDB.getInstance().getConnection();
        System.out.println(adress.getAdress(currentUser.getUserId()));
        System.out.println(session);

        adress.addaddres(new Adress(session.getUser(), adressline.getText(),
                         city.getText(),
                state.getText(),
                postalcode.getText(),
                country.getText()
                            ));

    }
    @FXML
    void addadressSingup(ActionEvent event) throws SQLException, IOException {
        System.out.println(session);
        adress.addaddres(new Adress(session.getUser(), adressline.getText(),
                         city.getText(),
                state.getText(),
                postalcode.getText(),
                country.getText()
                            ));

        SceneController.loadProfilePageScene();

    }

    @FXML
    void loginlink(ActionEvent event) {

    }
    @FXML
    void skipsingup(ActionEvent event) throws IOException {
        SceneController.loadBackToProfileScene();
    }


}

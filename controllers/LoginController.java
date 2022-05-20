/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import state.UserState;

/**
 * FXML Controller class
 *
 * @author MMURRAY
 */
public class LoginController implements Initializable {

    private Stage stage;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button loginButton;
    @FXML
    private Label unknownLabel;
    
    public void setStage( Stage stage ) {
        this.stage = stage;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loginButton.disableProperty().bind(txtUsername.textProperty().isEmpty().or(txtPassword.textProperty().isEmpty()));
    }    

    @FXML
    private void closeLogin(ActionEvent event) {
        this.stage.close();
    }

    @FXML
    private void logUserIn(ActionEvent event) {
        User user = null;
        
        if (!user.checkCredentials(txtUsername.textProperty().getValue(), txtPassword.textProperty().getValue())) {
            displayUnknownUser();
            txtUsername.textProperty().setValue("");
            txtPassword.textProperty().setValue("");
        } else {
        }
    }
    
    private void displayUnknownUser() {
        unknownLabel.visibleProperty().set(true);
    }
    
}

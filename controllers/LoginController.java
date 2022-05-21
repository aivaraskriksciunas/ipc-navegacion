/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DBAccess.NavegacionDAOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Navegacion;
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
    
    private Navegacion nav;
  
    public void setStage( Stage stage ) {
        this.stage = stage;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nav = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException e) {
            Alert alert = new Alert( AlertType.ERROR );
            alert.setTitle("Login error");
            alert.setContentText("There has been an error with the database. Verify your installation and try again.");
            alert.showAndWait();
            
            stage.close();
            return;
        }
        loginButton.disableProperty().bind(txtUsername.textProperty().isEmpty().or(txtPassword.textProperty().isEmpty()));
    }    

    @FXML
    private void closeLogin(ActionEvent event) {
        this.stage.close();
    }

    @FXML
    private void logUserIn(ActionEvent event) {
        User user;
        
        if (!nav.exitsNickName(txtUsername.getText())) {
            unknownLabel.visibleProperty().set(true);

            return;
        }
        
        user = nav.loginUser(txtUsername.getText(), txtPassword.getText());
        
        if (user == null) {
            unknownLabel.setText("Username or password is incorrect");
            unknownLabel.visibleProperty().set(true);
            return;
        }
        
        UserState.getState().setUser(user);
        
        stage.close();
        
    }
}

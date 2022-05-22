/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class WelcomeScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onLoginSelected(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/Login.fxml"));
        
        try {
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
            Stage stage = new Stage();
            
            // Send a reference of the stage to the controller
            fxmlLoader.<LoginController>getController().setStage( stage );
            
            stage.setTitle( "Login" );
            stage.setScene( scene );
            stage.initModality( Modality.APPLICATION_MODAL );
            stage.showAndWait();
        }
        catch ( IOException e ) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void onRegisterSelected(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/Register.fxml"));
        
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            
            // Send a reference of the stage to the controller
            fxmlLoader.<RegisterController>getController().setStage( stage );
            
            stage.setTitle( "Register new user" );
            stage.setScene( scene );
            stage.initModality( Modality.APPLICATION_MODAL );
            stage.showAndWait();
        }
        catch ( IOException e ) {
            
        }
    }
    
}

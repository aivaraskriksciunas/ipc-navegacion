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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class MainScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void onTestMap(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation( getClass().getResource("../views/MapView.fxml") );
        System.out.println( "opening map" );
        
        try {
            Scene scene = new Scene( fxmlLoader.load() );
            Stage stage = new Stage();
            
            stage.setTitle( "Map Tools" );
            stage.setScene( scene );
            stage.show();
}
        catch ( IOException e ) {
            System.out.println( e.toString() );
        }
    }
    
}

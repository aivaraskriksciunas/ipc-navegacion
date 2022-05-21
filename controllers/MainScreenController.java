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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import state.UserState;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class MainScreenController implements Initializable {

    @FXML
    private Label userName;
    @FXML
    private ImageView userAvatar;
    @FXML
    private MenuItem previewMap;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            UserState.getState().addUserListener((o, oldValue, newValue) -> {
                if (newValue == null) return;
                userName.setText(newValue.getNickName());
                userAvatar.setImage(UserState.getState().getUser().getAvatar());
            });
        
    }    
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

    @FXML
    private void editProfile(ActionEvent event) {
    }

    @FXML
    private void logUserOut(ActionEvent event) {
        UserState.getState().logout();
    }

    @FXML
    private void displayAbout(ActionEvent event) {
    }

    @FXML
    private void editProfile(MouseEvent event) {
    }

    @FXML
    private void rdnPrbBtn(ActionEvent event) {
    }

    @FXML
    private void allPrbBtn(ActionEvent event) {
    }

    @FXML
    private void previewMapBtn(ActionEvent event) {
    }
    
}

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.QuestionViewManager;
import state.UserState;
import utils.NotifUtils;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class MainScreenController implements Initializable {
    private QuestionViewManager questionViewManager;

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
    
        questionViewManager = new QuestionViewManager();
        
        UserState.getState().addUserListener((o, oldValue, newValue) -> {
            if (newValue == null) return;
            userName.setText(newValue.getNickName());
            userAvatar.setImage(UserState.getState().getUser().getAvatar());
        });
        
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
        questionViewManager.chooseRandomQuestion();
    }

    @FXML
    private void allPrbBtn(ActionEvent event) {
        questionViewManager.showQuestionList();
    }

    @FXML
    private void previewMapBtn(ActionEvent event) {
        questionViewManager.showMap();
    }

    @FXML
    private void openSessionHistory(ActionEvent event) {
        // Load map
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation( getClass().getResource("../views/UserHistory.fxml") );
        
        try {
            Scene scene = new Scene( fxmlLoader.load() );
            Stage sessionWindow = new Stage();
            
            sessionWindow.setTitle( "Session History" );
            sessionWindow.setScene( scene );
            sessionWindow.initModality( Modality.APPLICATION_MODAL );
            sessionWindow.showAndWait();
        }
        catch ( IOException e ) {
            NotifUtils.showError( "Error", "Could not show user history due to missing files or database error." );
            System.out.println( e );
        }
    }
    
}

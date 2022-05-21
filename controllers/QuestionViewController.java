/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import model.Answer;
import model.Problem;
import model.User;
import state.SessionManager;
import state.UserState;
import utils.QuestionViewManager;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class QuestionViewController implements Initializable {

    private Problem problem;
    private ArrayList<Answer> answers;
    
    @FXML
    private Text questionText;
    @FXML
    private Button openMapButton;
    @FXML
    private RadioButton choice1;
    @FXML
    private RadioButton choice2;
    @FXML
    private RadioButton choice3;
    @FXML
    private RadioButton choice4;
    @FXML
    private ToggleGroup choiceGroup;
    @FXML
    private Button submitButton;
    @FXML
    private Button continueButton;
    
    private QuestionViewManager manager;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        submitButton.disableProperty().bind( 
                choice1.selectedProperty().or( choice2.selectedProperty() )
                        .or( choice3.selectedProperty() )
                        .or( choice4.selectedProperty() ) 
                .not()
        );
    }  
    
    public void setManager( QuestionViewManager manager ) {
        this.manager = manager;
        
        manager.mapShowingProperty().addListener( ( o, ov, nv ) -> {
            if ( nv ) {
                openMapButton.setText( "Open map" );
            }
            else {
                openMapButton.setText( "Close map" );
            }
        });
    }
    
    public void setProblem( Problem p ) {
        problem = p;
        
        questionText.setText( p.getText() );
        
        answers = new ArrayList<>( p.getAnswers() );
        Collections.shuffle( answers );
        
        for ( int i = 0; i < 4; i++ ) {
            RadioButton btn = (RadioButton)choiceGroup.getToggles().get( i );
            
            btn.setText( answers.get( i ).getText() );
            btn.getStyleClass().remove( "correct-choice" );
            btn.getStyleClass().remove( "wrong-choice" );
            btn.setMouseTransparent( false );
            btn.setSelected( false );
        }
        
        submitButton.setVisible( true );
        submitButton.setManaged( true );
        continueButton.setVisible( false );
        continueButton.setManaged( false );
    }

    @FXML
    private void submitQuestion(ActionEvent event) {
        
        submitButton.setVisible( false );
        submitButton.setManaged( false );
        continueButton.setVisible( true );
        continueButton.setManaged( true );
        
        SessionManager session = UserState.getState().getSession();
        boolean hit = false;
        
        for ( int i = 0; i < 4; i++ ) {
            RadioButton btn = (RadioButton)choiceGroup.getToggles().get( i );
            btn.setMouseTransparent( true );
            
            if ( btn.isSelected() && answers.get( i ).getValidity() ) {
                hit = true;
            }
            
            if ( answers.get( i ).getValidity() ) {
                btn.getStyleClass().add( "correct-choice" );
            }
            else if ( btn.isSelected() ) {
                btn.getStyleClass().add( "wrong-choice" );
            }
        }
        
        if ( hit ) {
            session.saveHit();
        }
        else {
            session.saveFault();
        }
    }

    @FXML
    private void onContinue(ActionEvent event) {
        manager.showQuestionList();
    }

    @FXML
    private void onOpenMap(ActionEvent event) {
        if ( manager.mapShowingProperty().getValue() ) {
            manager.closeMap();
        }
        else {
            manager.showMap();
        }
    }
    
}

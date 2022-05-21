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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import model.Answer;
import model.Problem;

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
    
    public void setProblem( Problem p ) {
        problem = p;
        
        questionText.setText( p.getText() );
        
        answers = new ArrayList<Answer>( p.getAnswers() );
        Collections.shuffle( answers );
        
        choice1.setText( answers.get( 0 ).getText() );
        choice2.setText( answers.get( 1 ).getText() );
        choice3.setText( answers.get( 2 ).getText() );
        choice4.setText( answers.get( 3 ).getText() );
    }
    
}

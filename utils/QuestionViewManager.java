/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import controllers.QuestionListController;
import controllers.QuestionViewController;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Navegacion;
import model.Problem;

/**
 *
 * @author Aivaras Kriksciunas
 */
public class QuestionViewManager {
    
    private Stage window;
    
    private Scene questionViewScene;
    private Scene questionListScene;
    
    private QuestionListController questionListController;
    private QuestionViewController questionViewController;
    
    private Navegacion nav;
    
    private List<Problem> problems;
    
    public QuestionViewManager() {
        FXMLLoader questionListView = new FXMLLoader( getClass().getResource( "../views/QuestionList.fxml" ));
        FXMLLoader questionView = new FXMLLoader( getClass().getResource( "../views/QuestionView.fxml" ));
        
        try {
            questionViewScene = new Scene( questionView.load() );
            questionListScene = new Scene( questionListView.load() );
        }
        catch ( IOException e ) {
            NotifUtils.showError( "Missing files", "Application has encountered an error due to possible missing files." );
        }
        
        questionListController = questionListView.getController();
        questionViewController = questionView.getController();
        
        questionListController.setManager( this );
        
        try {
            nav = Navegacion.getSingletonNavegacion();
        }
        catch ( Exception e ) {
            // If there are errors, they should already be handled in previous controllers
        }
        
        problems = nav.getProblems();
        
        window = new Stage();
        window.initModality( Modality.APPLICATION_MODAL );
    }
    
    public void chooseRandomQuestion() {
        Random rand = new Random();
        Problem chosenProblem = problems.get( rand.nextInt( problems.size() ) );
        
        questionViewController.setProblem( chosenProblem );
        showScene( questionViewScene );
    }
    
    public void showQuestionList() {
        questionListController.setProblems( problems );
        showScene( questionListScene );
    }
    
    public void showQuestion( Problem problem ) {
        questionViewController.setProblem( problem );
        showScene( questionViewScene );
    }
    
    private void showScene( Scene scene ) {
        window.setScene( scene );
        
        if ( !window.isShowing() ) {
            window.showAndWait();
        }
        
    }
}

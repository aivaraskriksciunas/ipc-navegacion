/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import DBAccess.NavegacionDAOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Navegacion;
import model.Problem;
import state.UserState;
import utils.NotifUtils;
import utils.QuestionViewManager;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class QuestionListController implements Initializable {

    @FXML
    private ListView<Problem> questionList;
    @FXML
    private Button selectQuestion;
    
    private QuestionViewManager manager;
    private ObservableList<Problem> problems;
    private Navegacion nav;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            nav = Navegacion.getSingletonNavegacion();
        }
        catch ( NavegacionDAOException e ) {
            NotifUtils.showError( "Database error", "Error connecting to the database. Verify your installation and try again." );
        }
        
        problems = FXCollections.observableArrayList();
        
        questionList.setItems( problems );
        questionList.setCellFactory( c -> new QuestionListCell() );
        
        selectQuestion.disableProperty().bind( questionList.getSelectionModel().selectedIndexProperty().lessThan( 0 ) );
    }
    
    public void setManager( QuestionViewManager manager ) {
        this.manager = manager;
    }
    
    public void setProblems( List<Problem> problems ) {
        this.problems.setAll( problems );
    }

    @FXML
    private void onSelectQuestion(ActionEvent event) {
        manager.showQuestion( questionList.getSelectionModel().getSelectedItem() );
    }
    
    class QuestionListCell extends ListCell<Problem> {
        @Override
        protected void updateItem( Problem item, boolean empty ){
            super.updateItem(item, empty);            
            if(item==null || empty) {
                setText(null);
                return;
            }
            
            String probText = item.getText();
            if ( probText.length() > 40 ) {
                probText = probText.substring( 0, 40 ).concat( "..." );
            }
            
            setText( probText );
        }
    }
    
}

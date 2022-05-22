/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Session;
import state.UserState;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class UserHistoryController implements Initializable {

    @FXML
    private TableView<SessionRow> sessionListView;
    private ObservableList<SessionRow> sessionRowList;
    @FXML
    private TableColumn<SessionRow, String> dateCol;
    @FXML
    private TableColumn<SessionRow, String> hitsCol;
    @FXML
    private TableColumn<SessionRow, String> faultsCol;
    @FXML
    private TableColumn<SessionRow, String> totalCol;
    @FXML
    private TableColumn<SessionRow, String> scoreCol;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        List<Session> sessions = UserState.getState().getUser().getSessions();
        
        try {
            dateCol.setCellValueFactory( new PropertyValueFactory<SessionRow, String>( "date" ) );
            hitsCol.setCellValueFactory( new PropertyValueFactory<SessionRow, String>( "hits" ) );
            faultsCol.setCellValueFactory( new PropertyValueFactory<SessionRow, String>( "faults" ) );
            totalCol.setCellValueFactory( new PropertyValueFactory<SessionRow, String>( "total" ) );
            scoreCol.setCellValueFactory( new PropertyValueFactory<SessionRow, String>( "score" ) );
            sessionListView.setItems( sessionRowList );
               
            List<SessionRow> sessionRows = new ArrayList<>();
            for ( Session s : sessions ) {
                sessionRows.add( new SessionRow( s.getLocalDate(), s.getHits(), s.getFaults() ) );
            }
            
            sessionRowList = FXCollections.observableList( sessionRows );
            sessionListView.setItems( sessionRowList );
        }
        catch ( Exception e ) {
            System.out.println( e );
        }
        
        
    }    
    
    public class SessionRow {
        private String date;
        private int hits;
        private int faults;
        private int total;
        private String score;
        
        public SessionRow( LocalDate date, int hits, int faults ) {
            this.date = date.toString();
            this.hits = hits;
            this.faults = faults;
            this.total = hits + faults;
            
            if ( this.total != 0 )
                this.score = Math.round( (double)hits / ( hits + faults ) * 100 ) + "%";
            else this.score = "0%";
        }
        
        public String getDate() { return date; }
        public int getHits() { return hits; }
        public int getFaults() { return faults; }
        public int getTotal() { return total; }
        public String getScore() { return score; }
    }
    
}

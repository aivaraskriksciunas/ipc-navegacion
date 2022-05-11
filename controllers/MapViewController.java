/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class MapViewController implements Initializable {

    @FXML
    private Canvas canvas;
    private GraphicsContext graphics;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        graphics = canvas.getGraphicsContext2D();
        
        Image map = new Image( "resources/carta_nautica.jpg" );
        graphics.drawImage( map, 0, 0 );
    }    
    
}

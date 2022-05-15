/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.scene.control.Alert;

/**
 *
 * @author Aivaras Kriksciunas
 */
public class NotifUtils {
    
    public static void showError( String title, String message ) {
        
        Alert alert = new Alert( Alert.AlertType.ERROR );
        alert.setTitle( title );
        alert.setContentText( message );
        alert.showAndWait();
    
    }
}

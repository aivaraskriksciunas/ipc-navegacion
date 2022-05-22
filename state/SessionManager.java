package state;

import DBAccess.NavegacionDAOException;
import java.time.LocalDateTime;
import model.Session;
import model.User;
import utils.NotifUtils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Aivaras Kriksciunas
 */
public class SessionManager {
    
    private int hits = 0;
    private int faults = 0;
    private User user;
    
    public SessionManager( User user ) {
        this.user = user;
    }
    
    public void saveSession() {
        // Don't save empty sessions
        if ( hits == 0 && faults == 0 ) return;
        
        try {
            user.addSession( new Session( LocalDateTime.now(), hits, faults ) );
        }
        catch ( NavegacionDAOException e ) {
            NotifUtils.showError( "Database error" , "Could not save your session to the database due to a possible installation error. Make sure the application has sufficient permissions to access its files.");
        }
    }
    
    public void saveHit() {
        hits++;
    }
    
    public void saveFault() {
        faults++;
    }
    
    
}

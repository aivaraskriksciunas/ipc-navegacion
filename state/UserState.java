package state;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import model.User;

/**
 *
 * @author Aivaras Kriksciunas
 */
public class UserState {
    
    // State class using the singleton pattern
    private static UserState state;
    
    // Method to retrieve the singleton
    public static UserState getState() {
        if ( state == null ) {
            state = new UserState();
        }
        
        return state;
    }
    
    //
    // Implementation
    // 
    public ObjectProperty<User> user;
    private SessionManager session;
    
    // Prevent other classes from instantiating an object
    private UserState() {
        user = new SimpleObjectProperty();
    }
    
    public void setUser( User user ) {
        this.user.set( user );
        
        if ( user != null ) {
            session = new SessionManager( user );
        }
        else if ( session != null ) {
            session.saveSession();
            session = null;
        }
    }
    
    public User getUser() {
        return user.getValue();
    }
    
    public void logout() {
        this.setUser( null );
    }
    
    public void addUserListener( ChangeListener<? super User> listener ) {
        user.addListener( listener );
    }
    
    public SessionManager getSession() {
        return session;
    }
    
}

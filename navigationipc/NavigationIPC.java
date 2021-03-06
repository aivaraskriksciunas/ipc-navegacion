package navigationipc;

import controllers.MainScreenController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import state.UserState;
import utils.QuestionViewManager;

/**
 *
 * @author Aivaras Kriksciunas, Milan Murray, Kuba Wysocki
 */
public class NavigationIPC extends Application {
    
    private Scene welcomeScene;
    private Scene mainScene;
    
    Stage stage;
    
    public static void main(String[] args) {
        launch( args );
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
        // Load required scenes
        FXMLLoader welcomeLoader = new FXMLLoader( getClass().getResource( "../views/WelcomeScreen.fxml" ));
        FXMLLoader mainLoader = new FXMLLoader( getClass().getResource( "../views/MainScreen.fxml" ));
        
        welcomeScene = new Scene( welcomeLoader.load() );
        mainScene = new Scene( mainLoader.load() );
        
        stage.setScene( welcomeScene ); // set to welcome scene later
        stage.setTitle( "NavEx" );
        stage.show();
        
        this.stage = stage;
        
        UserState userState = UserState.getState();
        userState.addUserListener( new UserStateListener() );
        
    }
    
    @Override
    public void stop() {
        // Automaticall logout and save session
        UserState.getState().logout();
    }
    
    private class UserStateListener implements ChangeListener<User> {

        @Override
        public void changed(ObservableValue<? extends User> ov, User old, User current) {
            
            User model = ov.getValue();
            if ( model == null ) {
                stage.setScene( welcomeScene );
            }
            else {
                stage.setScene( mainScene );
            }
            
        }
        
    }
    
}

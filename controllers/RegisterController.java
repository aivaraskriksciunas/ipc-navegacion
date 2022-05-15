/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import DBAccess.NavegacionDAOException;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;
import state.UserState;
import utils.NotifUtils;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private Text usernameError;
    @FXML
    private TextField emailField;
    @FXML
    private Text emailError;
    @FXML
    private DatePicker dobField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text passwordError;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML
    private Text passwordConfirmError;
    @FXML
    private Text dobError;
    @FXML
    private ImageView avatarImage;
    
    private Stage stage;
    private Navegacion nav;
    
    public void setStage( Stage stage ) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Initially hide all the error messages
        clearErrorMessages();
        
        try {
            nav = Navegacion.getSingletonNavegacion();
        }
        catch ( NavegacionDAOException e ) {
            NotifUtils.showError( "Database error", "There has been an error with the database. Verify your installation and try again." );
            
            stage.close();
            return;
        }
        
        avatarImage.setImage( new Image( "avatars/default.png" ));
        
    }    

    @FXML
    private void onRegister(ActionEvent event) {
        clearErrorMessages();
        
        if ( !validateFields() ) return;
        
        User user;
        
        try {            
            user = nav.registerUser(
                    usernameField.getText(), 
                    emailField.getText(), 
                    passwordField.getText(), 
                    avatarImage.getImage(),
                    dobField.getValue());
            
        } 
        catch( NavegacionDAOException err ) {
            Alert alert = new Alert( AlertType.ERROR );
            alert.setTitle("Database error");
            alert.setContentText("There has been an error with the database. Verify your installation and try again.");
            alert.showAndWait();
            
            return;
        }
        
        UserState state = UserState.getState();
        state.setUser( user );
        
        stage.close();
    }
    
    private boolean validateFields() {
        boolean valid = true;
        
        if ( usernameField.getText().length() < 6 ) {
            valid = false;
            setErrorMessage( usernameError, "Username must be at least 6 characters." );
        }
        else if ( usernameField.getText().length() > 15 ) {
            valid = false;
            setErrorMessage( usernameError, "Username cannot exceed 15 characters." );
        }
        else if ( nav.exitsNickName( usernameField.getText() ) ) {
            valid = false;
            setErrorMessage( usernameError, "Username already taken." );
        }
        
        if ( usernameField.getText().contains( " " ) ) {
            valid = false;
            setErrorMessage( usernameError, "Username must not contain spaces." );
        }
        
        if ( !emailField.getText().contains( "@" ) ) {
            valid = false;
            setErrorMessage( emailError, "Please provide a valid email." );
        }
        
        if ( passwordField.getText().length() < 8 ) {
            valid = false;
            setErrorMessage( passwordError, "Password must be at least 8 characters long." );
        }
        else if ( passwordField.getText().length() > 20 ) {
            valid = false;
            setErrorMessage( passwordError, "Password cannot exceed 20 characters." );
        }
        else if ( !passwordField.getText().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$" ) ) {
            valid = false;
            setErrorMessage( passwordError, "Password does not match the criteria." );
        }
        
        if ( !passwordField.getText().equals( passwordConfirmField.getText() ) ) {
            valid = false;
            setErrorMessage( passwordConfirmError, "Passwords don't match." );
        }
        
        if ( dobField.getValue() == null || dobField.getValue().plusYears( 12 ).isAfter( LocalDate.now() ) ) {
            valid = false;
            setErrorMessage( dobError, "User must be at least 12 years old." );
        }
        
        return valid;
    }
    
    private void setErrorMessage( Text element, String error ) {
        if ( error == null ) {
            element.setVisible( false );
            element.setManaged( false );
            return;
        }
        
        element.setVisible( true );
        element.setManaged( true );
        element.setText( error );
    }
    
    private void clearErrorMessages() {
        setErrorMessage( emailError, null );
        setErrorMessage( usernameError, null );
        setErrorMessage( passwordError, null );
        setErrorMessage( passwordConfirmError, null );
        setErrorMessage( dobError, null );
    }

    @FXML
    private void onCancel(ActionEvent event) {
        stage.close();
    }

    @FXML
    private void onChooseAvatar( ActionEvent event ) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle( "Choose avatar" );
        fileChooser.getExtensionFilters().addAll( new ExtensionFilter("Images", "*.png", "*.jpg") );
        
        File selectedFile = fileChooser.showOpenDialog(
            ((Node)event.getSource()).getScene().getWindow());
        
        if (selectedFile != null) {
            avatarImage.setImage( new Image( selectedFile.getPath() ) );
        }
    }
    
}

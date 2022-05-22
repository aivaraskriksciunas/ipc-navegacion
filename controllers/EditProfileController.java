/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import DBAccess.NavegacionDAOException;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;
import state.UserState;

/**
 * FXML Controller class
 *
 * @author Kuba Wysocki
 */
public class EditProfileController implements Initializable {
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
    private Text dobError;
    @FXML
    private ImageView avatarImage;
    
    private Stage stage;
    private Navegacion nav;
    User user;
@FXML
    private PasswordField newPasswordField;
    @FXML
    private Text NewPasswordError;
    
    
    public void setStage( Stage stage ) {
        this.stage = stage;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Initially hide all the error messages
        clearErrorMessages();
        
        try {
            nav = Navegacion.getSingletonNavegacion();
        }
        catch ( NavegacionDAOException e ) {
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setTitle("Database error");
            alert.setContentText("There has been an error with the database. Verify your installation and try again.");
            alert.showAndWait();
            
            stage.close();
            return;
        }
        
        user = UserState.getState().getUser();
        usernameField.setText(user.getNickName());
        avatarImage.setImage(user.getAvatar());
        emailField.setText(user.getEmail());
        dobField.setValue(user.getBirthdate());
        
        
    }    

    private boolean validateFields() {
        boolean valid = true;
               
        
        if ( !emailField.getText().contains( "@" ) ) {
            valid = false;
            setErrorMessage( emailError, "Please provide a valid email." );
        }
        
                
        if ( dobField.getValue() == null || dobField.getValue().plusYears( 12 ).isAfter( LocalDate.now() ) ) {
            valid = false;
            setErrorMessage( dobError, "User must be at least 12 years old." );
        }
        
        return valid;
    }
    
    private boolean validatePassword(){
        boolean valid = true;
        
            if ( !passwordField.getText().equals(user.getPassword()) ) {
                valid = false;
                setErrorMessage( passwordError, "Password doenst match try again" );
            } else if (newPasswordField.getText().equals(passwordField.getText())){
                valid = false;
                setErrorMessage(NewPasswordError, "New password cannot be the same as old");
            }

            if ( newPasswordField.getText().length() < 8 ) {
                valid = false;
                setErrorMessage( NewPasswordError, "Password must be at least 8 characters long." );
            }
            else if ( newPasswordField.getText().length() > 20 ) {
                valid = false;
                setErrorMessage( NewPasswordError, "Password cannot exceed 20 characters." );
            }

            else if ( !newPasswordField.getText().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$" ) ) {
                valid = false;
                setErrorMessage( NewPasswordError, "Password does not match the criteria." );
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
        setErrorMessage( NewPasswordError, null );
        setErrorMessage( dobError, null );
    }

    @FXML
    private void onCancel(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void onChooseAvatar( ActionEvent event ) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle( "Choose avatar" );
        fileChooser.setInitialDirectory(new File("avatars"));
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg") );
        
        File selectedFile = fileChooser.showOpenDialog(
            ((Node)event.getSource()).getScene().getWindow());
        
        if (selectedFile != null) {
            avatarImage.setImage( new Image( selectedFile.getPath() ) );
        }
    }

    @FXML
    private void onUpdate(ActionEvent event) {
        clearErrorMessages();
        
        if ( !validateFields() ) return;
        

        
        
        try {
            
            user.setEmail(emailField.getText());
            user.setBirthdate(dobField.getValue());
            user.setAvatar(avatarImage.getImage());
            if (!passwordField.getText().isEmpty() || !newPasswordField.getText().isEmpty()){
                if(!validatePassword()){
                    return;
                }else{
                user.setPassword(newPasswordField.getText());
                }
            }
            

            
            
            
        } 
        catch( NavegacionDAOException err ) {
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setTitle("Database error");
            alert.setContentText("There has been an error with the database. Verify your installation and try again.");
            alert.showAndWait();
            
            return;
        }
        

        
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
    
}

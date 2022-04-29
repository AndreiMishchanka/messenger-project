package Application;

import Data.*;
import Utills.LoadXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StartController extends StartApplication{
    @FXML
    private Button loginButton;
    @FXML
    private TextField userLogin;
    @FXML
    private Button signUpButton;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Label errorOutput;

    @FXML
    protected void onloginButtonClick() {   
        User user = null;
        try {
            user = Database.getUser(userLogin.getText(), userPassword.getText());  
            // errorOutput.setTextFill(Color.web("#1EA624", 0.8));
            // errorOutput.setText("OK"); 
            FXMLLoader loader = LoadXML.load("Scenes/Messanger/MainPage.fxml");      
            setScene(loader);                       
        }catch(Database.IncorrectPasswordException e) {
            errorOutput.setTextFill(Color.web("#dd0e0e", 0.8));
            errorOutput.setText("INCORRECT PASSWORD");            
        }catch(Database.IncorrectUserException e) {
            errorOutput.setTextFill(Color.web("#dd0e0e", 0.8));
            errorOutput.setText("INCORRECT USER");            
        }catch(Exception e) {
            e.printStackTrace();
            return;
        }             
                    
    }

    @FXML
    protected void onsignUpButtonClick() {   
        try{
            FXMLLoader loader = LoadXML.load("Scenes/LoginPage/SignUp.fxml");                  
            setScene(loader);                  
        }catch(Exception e) {            
            e.printStackTrace();
        }                              
    }
}
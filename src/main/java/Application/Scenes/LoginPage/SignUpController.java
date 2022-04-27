package Application.Scenes.LoginPage;

import Application.StartApplication;
import Data.Database;
import Data.User;
import Utills.LoadXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class SignUpController extends StartApplication {
    @FXML
    private Button registerButton;
    @FXML
    private TextField userLogin;
    @FXML
    private PasswordField userPassword;
    @FXML
    private PasswordField userPasswordConfirm;
    @FXML
    private Label errorOutput;
    @FXML
    private Button backButton;

    @FXML
    protected void onbackButtonClick(){
        try{
            FXMLLoader loader = LoadXML.load("hello-view.fxml");                  
            setScene(loader);                     
        }catch(Exception e) {
            e.printStackTrace();
        }  
    }

    @FXML
    protected void onregisterButtonClick() {   
        User user = null;
        try {            
            user = Database.getUser(userLogin.getText(), userPassword.getText());
        }catch(Database.IncorrectPasswordException e) {
            errorOutput.setTextFill(Color.web("#dd0e0e", 0.8));
            errorOutput.setText("INCORRECT PASSWORD");            
        }catch(Exception e) {
            e.printStackTrace();
            return;
        }             
                    
    }
    
}

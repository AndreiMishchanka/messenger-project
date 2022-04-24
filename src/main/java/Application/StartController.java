package Application;

import Data.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class StartController {
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
        }catch(Database.IncorrectPasswordException e) {
            errorOutput.setTextFill(Color.web("#dd0e0e", 0.8));
            errorOutput.setText("INCORRECT PASSWORD");            
        }catch(Exception e) {
            e.printStackTrace();
            return;
        }             
        
        // System.out.print(user.getNickname());        
    }
}
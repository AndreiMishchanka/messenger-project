package Application;

import Data.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    protected void onloginButtonClick() {        
        User user = Database.getUser(userLogin.getText(), userPassword.getText());
        System.out.print(user.getNickname());
        // System.out.print(userLogin.getText());
        // System.out.print(userPassword.getText());
    }
}
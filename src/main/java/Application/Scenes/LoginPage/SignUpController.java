package Application.Scenes.LoginPage;

import Application.StartApplication;
import Data.Database;
import Utills.LoadXML;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public void changeSizes(){
        //backButton.setLayoutX(StartApplication.stageWidth-75);
       // backButton.setLayoutY(10);
    }

    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
        StartApplication.stageWidth = StartApplication.primaryStage.getWidth();
        StartApplication.stageHeight = StartApplication.primaryStage.getHeight();
        changeSizes();
    };


    public void initialize(){
        ///change_size
        StartApplication.primaryStage.widthProperty().addListener(stageSizeListener);
        StartApplication.primaryStage.heightProperty().addListener(stageSizeListener);
        changeSizes();
        backButton.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/back.png")), 50, 50, false, false)));

    }

    @FXML
    protected void onbackButtonClick(){
        try{
            FXMLLoader loader = LoadXML.load("hello-view.fxml");                  
            setScene(loader);                     
        }catch(Exception e) {
            e.printStackTrace();
        }  
    }

    private void setError(String colorString, String errorText) {
        errorOutput.setTextFill(Color.web(colorString, 0.8));
        errorOutput.setText(errorText);
    }

    @FXML
    protected void onregisterButtonClick() {           
        if (!userPassword.getText().equals(userPasswordConfirm.getText())) {  
            setError("#dd0e0e", "PASSWORDS not equals");                      
            return;
        }
        try {        
            Database.registerUser(userLogin.getText(), userPassword.getText());    
            setError("#1EA624", "User was registred");                                  
        }catch(Database.IncorrectPasswordException e) {
            setError("#dd0e0e", "INCORRECT PASSWORD");               
        }catch(Database.UserAlreadyRegistred e) {
            setError("#dd0e0e", "User already registred");                           
        }catch(Exception e) {
            e.printStackTrace();
            return;
        }             
                    
    }
    
}

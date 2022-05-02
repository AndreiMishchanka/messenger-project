package Application.Scenes.Settings;

import java.io.File;

import Application.StartApplication;
import Utills.LoadXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import Data.*;
import Data.Database.UserAlreadyRegistred;
import Data.Database.IncorrectPasswordException;

public class SettingsPageController {

    public TextField nicknameChange;
    public Label nicknameError;
    public Label passwordError;
    public ImageView avatar;

    public void initialize(){
        nicknameChange.setText(User.MainUser.getNickname());
       // Image im = new Image("/resources/Application/Photo/image.jpg");
       // avatar.setImage(im);
    }

    public void tryChangeNickname(){

        try{
            throw new UserAlreadyRegistred();
        }catch(UserAlreadyRegistred e){
            nicknameError.setText("User with that nick is already exist");
        }

    }

    public void tryChangePassword(){
        try{
            throw new IncorrectPasswordException();
        }catch(IncorrectPasswordException e){
            passwordError.setText("Incorrect old password");
           // return;
        }
        try{
            throw new IncorrectPasswordException();
        }catch(IncorrectPasswordException e){
            passwordError.setText("Incorrect new password");
            return;
        }
    }


    public void fromSettingsToChats(){
        FXMLLoader loader = LoadXML.load("Scenes/ChatView/ChatView.fxml");      
        StartApplication.setScene(loader);   
    }

}

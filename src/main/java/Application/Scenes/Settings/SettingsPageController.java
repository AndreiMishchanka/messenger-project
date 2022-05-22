package Application.Scenes.Settings;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import Application.Scenes.ChatView.ChatViewController;
import Application.StartApplication;
import Utills.LoadXML;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import Data.*;
import Data.Database.UserAlreadyRegistred;
import Data.Database.IncorrectPasswordException;
import static Data.User.MainUser;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;


public class SettingsPageController {

    public TextField nicknameChange;
    public Label nicknameError;
    public Label passwordError;
    public ImageView avatar;

    @FXML
    private Button uploadImageButton;

    @FXML
    private TextField imageURL;

    public void changeSizes(){

    }

    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
        StartApplication.stageWidth = StartApplication.primaryStage.getWidth();
        StartApplication.stageHeight = StartApplication.primaryStage.getHeight();
        changeSizes();
    };

    public void initialize(){
        ///change_size
        avatar.setImage(ChatViewController.getAvatar(MainUser));
        StartApplication.primaryStage.widthProperty().addListener(stageSizeListener);
        StartApplication.primaryStage.heightProperty().addListener(stageSizeListener);
        changeSizes();
        nicknameChange.setText(MainUser.getNickname());
    }

    public void tryChangeNickname(){
        try{
            Database.changeNickname(nicknameChange.getText());
            // throw new UserAlreadyRegistred();
        }catch(Exception e){
            nicknameError.setText("User with that nick is already exist");
            return;            
        }
        nicknameError.setTextFill(Color.web("#1EA624", 0.8));
        nicknameError.setText("Nickname has been changed");

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

    public void tryChangeAvatar() throws IOException {
        try {
            Image newAvatar = new Image(imageURL.getText());
            String path = new String("Images/" + MainUser.getNickname() + ".png");
            if (StartApplication.class.getResource(path) != null) {
                Files.delete(Path.of(path));
             }
            File newImage = new File(MainUser.getNickname() + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(newAvatar, null), "png", newImage);
        } catch (Exception e) {
            //INCORECT URL;
        }
    }

    public void fromSettingsToChats(){
        FXMLLoader loader = LoadXML.load("Scenes/ChatView/ChatView.fxml");      
        StartApplication.setScene(loader);   
    }

}

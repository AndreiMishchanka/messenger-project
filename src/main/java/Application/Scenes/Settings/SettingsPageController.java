package Application.Scenes.Settings;


import java.io.File;
import java.io.IOException;
import Application.Scenes.ChatView.ChatViewController;
import Application.StartApplication;
import Utills.LoadXML;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import Data.*;
import Data.Database.IncorrectPasswordException;
import static Data.User.MainUser;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.swing.*;


public class SettingsPageController {

    public TextField nicknameChange;
    public Label nicknameError;
    public Label passwordError;
    public ImageView avatar;

    @FXML
    private Button Back;

    @FXML
    private Label URLError;

    @FXML
    private ImageView backIcon;

    @FXML
    private Button openButton;

    @FXML
    private ImageView saveIcon;

    @FXML
    private ImageView saveIcon2;

    @FXML
    private Button saveNick;

    @FXML
    private Button savePassword;

    public void changeSizes(){

    }

    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
        StartApplication.stageWidth = StartApplication.primaryStage.getWidth();
        StartApplication.stageHeight = StartApplication.primaryStage.getHeight();
        changeSizes();
    };

    public void initialize(){
        Back.setGraphic(backIcon);
        savePassword.setGraphic(saveIcon2);
        saveNick.setGraphic(saveIcon);
        ///change_size
        avatar.setImage(ChatViewController.getAvatar(MainUser));
        StartApplication.primaryStage.widthProperty().addListener(stageSizeListener);
        StartApplication.primaryStage.heightProperty().addListener(stageSizeListener);
        changeSizes();
        nicknameChange.setText(MainUser.getNickname());

        FileChooser fileChooser = new FileChooser();
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        Window stage = new Stage();
                        stage.setHeight(600);
                        stage.setWidth(600);
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            try {
                                Image newAvatar = new Image(file.getPath());
                                File newImage = new File("src/main/resources/Application/Images/" + MainUser.getNickname() + ".png");
                                ImageIO.write(SwingFXUtils.fromFXImage(newAvatar, null), "png", newImage);
                            } catch (Exception ex) {
                                URLError.setText("Incorrect URL!");
                                URLError.setTextFill(Color.RED);
                                return;
                            }
                            URLError.setTextFill(Color.web("#1EA624", 0.8));
                            URLError.setText("Avatar has changed!");
                        }
                    }
                });
    }

    public void tryChangeNickname(){
        File lastFile = new File("src/main/resources/Application/Images/" + MainUser.getNickname() + ".png");
        try{
            Database.changeNickname(nicknameChange.getText());
            // throw new UserAlreadyRegistred();
        }catch(Exception e){
            nicknameError.setText("User with that nick is already exist");
            return;
        }
        if (lastFile.exists()) {
            String newPath = new String("src/main/resources/Application/Images/" + MainUser.getNickname() + ".png");
            lastFile.renameTo(new File(newPath));
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

    public void fromSettingsToChats(){
        FXMLLoader loader = LoadXML.load("Scenes/ChatView/ChatView.fxml");      
        StartApplication.setScene(loader);   
    }

}

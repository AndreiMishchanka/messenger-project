package Application.Scenes.Settings;


import java.io.File;

import javax.imageio.ImageIO;

import Application.Scenes.ChatView.ChatViewController;
import Application.StartApplication;
import Utills.LoadXML;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import Data.*;
import static Data.User.MainUser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SettingsPageController {

    public TextField nicknameChange;
    public Label nicknameError;
    public Label passwordError;
    public ImageView avatar;

    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword;

    @FXML
    private Label URLError;

    @FXML
    private Button openButton, nicknameButton, passwordButton, Back;

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
        nicknameButton.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/save.png")), 50, 50, false, false)));
        Back.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/back.png")), 50, 50, false, false)));
        passwordButton.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/save.png")), 50, 50, false, false)));

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
                                Image newAvatar = new Image("file:"+file.getCanonicalPath());
                                File newImage = new File("src/main/resources/Application/Images/" + MainUser.getNickname() + ".png");
                                ImageIO.write(SwingFXUtils.fromFXImage(newAvatar, null), "png", newImage);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                URLError.setText("Incorrect URL!");
                                URLError.setTextFill(Color.RED);
                                return;
                            }
                            URLError.setTextFill(Color.web("#1EA624", 0.8));
                            URLError.setText("Avatar has changed!");
                            
                            Platform.runLater(new Runnable() {
                                public void run(){
                                    avatar.setImage(ChatViewController.getAvatar(MainUser));
                                }
                            });
                            
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

    public void tryChangePassword() throws Exception{
        try{
            Database.getUser(User.MainUser.getNickname(), oldPassword.getText());            
        }catch(Exception e){         
            passwordError.setTextFill(Color.RED);   
            passwordError.setText("Incorrect old password");
            // e.printStackTrace();
           // return;
           return;           
        }
        try{
            Database.changePassword(oldPassword.getText(), newPassword.getText());        
        }catch(Exception e){
            passwordError.setTextFill(Color.RED);   
            passwordError.setText("Incorrect new password");
            return;
        }
        passwordError.setTextFill(Color.web("#1EA624", 0.8));
        passwordError.setText("Password has been changed");
    }

    public void fromSettingsToChats(){
        FXMLLoader loader = LoadXML.load("Scenes/ChatView/ChatView.fxml");      
        StartApplication.setScene(loader);   
    }

}

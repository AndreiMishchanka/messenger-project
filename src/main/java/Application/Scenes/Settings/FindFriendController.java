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
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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


public class FindFriendController {

    @FXML
    private Button Back;

    @FXML
    private Label ErrorField;

    @FXML
    private TextField FriendName;

    @FXML
    private Button addButton;

    public void changeSizes(){

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
    }
  

    @FXML
    public void fromSettingsToChats(){
        FXMLLoader loader = LoadXML.load("Scenes/ChatView/ChatView.fxml");      
        StartApplication.setScene(loader);   
    }

    @FXML
    void AddToFriend(ActionEvent event) throws Exception{
        String Name = FriendName.getText();
        if (!Database.isUserConsist(Name)) {
            ErrorField.setText("User doesn't consist");
            ErrorField.setTextFill(Color.RED);
            return;
        }
        if (Database.isFriend(Name)) {
            ErrorField.setText("He is already your friend");
            ErrorField.setTextFill(Color.RED);
            return;
        }
        try{
            Database.makeFriend(Database.getIdByNick(Name));
            ErrorField.setText("Yeaah. You are friends");
            ErrorField.setTextFill(Color.GREEN);
        }catch(Exception e) {            
            ErrorField.setText("Cannot add to friends");
            ErrorField.setTextFill(Color.RED);
            e.printStackTrace();
            return;
        }
    }


}

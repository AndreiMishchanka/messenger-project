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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import Data.*;
import Data.Database.IncorrectPasswordException;
import static Data.User.MainUser;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.swing.*;


public class StickerChoosingController{

    @FXML
    private Button Back;

    @FXML
    private ScrollPane StickerPane;

    @FXML
    private VBox ForStickers;

    public void changeSizes(){

    }

    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
        StartApplication.stageWidth = StartApplication.primaryStage.getWidth();
        StartApplication.stageHeight = StartApplication.primaryStage.getHeight();
        changeSizes();
    };

    Button generateStickerButton(Image sticker, String stickerName) {
        double stickerSize = 189;
        Canvas canvas = new Canvas(stickerSize, stickerSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();        
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(1);
        gc.fillRect(0, 0, stickerSize, stickerSize);
        gc.drawImage(sticker, 0, 0, stickerSize, stickerSize);
        gc.setFill(Color.BLACK);             
        Button stickerButton = new Button("");
        stickerButton.setMaxSize(stickerSize, stickerSize);
        stickerButton.setMinSize(stickerSize, stickerSize);
      
        stickerButton.setGraphic(canvas);        
        stickerButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {     
                ChatViewController.StickerChooser = ":" + stickerName;
                fromSettingsToChats();                           
            }
            
        });
        return stickerButton;
    }

    public void initialize(){        
        StartApplication.primaryStage.widthProperty().addListener(stageSizeListener);
        StartApplication.primaryStage.heightProperty().addListener(stageSizeListener);        
        changeSizes();        

        StickerPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        StickerPane.setVbarPolicy(ScrollBarPolicy.NEVER);

        for (String Name : Sticker.allStickers) {
            try {
                ImageView mySticker = Sticker.getSticker(":" + Name);                               
                ForStickers.getChildren().add(generateStickerButton(mySticker.getImage(), Name));
            }catch (Exception e) {

            }
        }
    }
  

    @FXML
    public void fromSettingsToChats(){
        FXMLLoader loader = LoadXML.load("Scenes/ChatView/ChatView.fxml");      
        StartApplication.setScene(loader);   
    }

}

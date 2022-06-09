package Application.Scenes.Settings;

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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import Data.*;



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

        Back.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/back.png")), 50, 50, false, false)));


        StickerPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        StickerPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        int i = 0;
        HBox hbox = new HBox();
        hbox.setPrefWidth(400);
        hbox.setPrefHeight(100);
        for (String Name : Sticker.allStickers) {
            if(i == 3){
                ForStickers.getChildren().add(hbox); 
                hbox = new HBox();
                hbox.setPrefWidth(400);
                hbox.setPrefHeight(100);
                i = 0;
                continue;
            }
            try {
                ImageView mySticker = Sticker.getSticker(":" + Name);                        
                hbox.getChildren().add(generateStickerButton(mySticker.getImage(), Name));
                i++;
            }catch (Exception e) {

            }
        }
        ForStickers.getChildren().add(hbox); 
        
    }
  

    @FXML
    public void fromSettingsToChats(){
        FXMLLoader loader = LoadXML.load("Scenes/ChatView/ChatView.fxml");      
        StartApplication.setScene(loader);   
    }

}

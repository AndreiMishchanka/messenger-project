package Application.Scenes.ChatView;

import Application.StartApplication;
import Data.Database;
import Data.Message;
import Data.User;
import Utills.LoadXML;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.SplittableRandom;

import static Data.Database.getMessagesAfterTime;
import static Data.Database.sendMessage;

public class ChatViewController {

    @FXML
    public VBox fieldForMessages;
    @FXML
    public ScrollPane messagesList;
    @FXML
    public Button usersNick;
    @FXML
    public VBox chats;
    @FXML
    public TextField searchInChats;
    @FXML
    public ScrollPane scrolling;
    @FXML
    public TextField textOfSending;
    @FXML
    public Button sendMessageButton;
    @FXML
    public ImageView friendAvatar;

    static User currentFriend = null;

    @FXML
    public Button settings;

    @FXML
    public SplitPane splitPanePageForChats;
    
    static boolean on_start = false, on_end = false;

    static Text s = new Text();
    static public VBox friends = new VBox();
    static public HashMap<Integer, ArrayList<String>> friendsMessages = null;
    static public HashMap<Integer, ArrayList<ArrayList<Message>>> friendsArraysOfMessages = null;
    static public HashMap<Integer, ArrayList<Double>> sizeOfMessages = null;

    public static Image getAvatar(User user) {
        if (StartApplication.class.getResource("Images/" + user.getNickname() + ".png") == null) {
            return new Image(StartApplication.class.getResource("Images/default.png").toString());
        }
        return new Image(StartApplication.class.getResource("Images/" + user.getNickname() + ".png").toString());
    }

    public void setAllSize(){
        splitPanePageForChats.setDividerPosition(0, 180.0/StartApplication.stageWidth);
        scrolling.setPrefHeight(StartApplication.stageHeight-120);
        settings.setLayoutY(StartApplication.stageHeight-75);
        sendMessageButton.setLayoutY(StartApplication.stageHeight-75);
        sendMessageButton.setLayoutX(StartApplication.stageWidth-70-180);
        textOfSending.setLayoutY(StartApplication.stageHeight-75);
        textOfSending.setLayoutX(0);
        textOfSending.setPrefWidth(StartApplication.stageWidth-70-180);
        messagesList.setPrefHeight(StartApplication.stageHeight-120);
        messagesList.setPrefWidth(StartApplication.stageWidth-190);        
        usersNick.setPrefWidth(fieldForMessages.getPrefWidth() - friendAvatar.getFitWidth());
        if(on_end){
            fieldForMessages.setPrefWidth(StartApplication.stageWidth-200);
            if(currentFriend != null){
                fieldForMessages.setPrefHeight(sizeOfMessages.get(currentFriend.getId()).get((int)(fieldForMessages.getPrefWidth())-400));
            }
        }        
        messagesList.setVvalue(1.0);       
    }


    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
        StartApplication.stageWidth = StartApplication.primaryStage.getWidth();
        StartApplication.stageHeight = StartApplication.primaryStage.getHeight();
        setAllSize();        
    };


    public void makeChatToUser(User currentUser) throws Exception {
        friendAvatar.setImage(getAvatar(currentUser));
        currentFriend = currentUser;
        usersNick.setText(currentFriend.getNickname());
        fieldForMessages.getChildren().clear();
        for(ArrayList<Message> cur : friendsArraysOfMessages.get(currentFriend.getId())){
            fieldForMessages.getChildren().add(makeMessage(cur, currentFriend));
        }        
        setAllSize();        
    }


    public static String getMessageText(ArrayList < Message > currentMessage, User user){
        StringBuilder text = new StringBuilder();
        if (Objects.equals(currentMessage.get(0).getText(), "|0|")) {
            text.append("You");
        } else {
            text.append(user.getNickname());
        }
        text.append(" (" + currentMessage.get(1).getTime() + ") : " + currentMessage.get(1).getText());
        return text.toString();
    }

    static Label makeMessage(ArrayList < Message > currentMessage, User user) {
        String text = getMessageText(currentMessage, user);
        Label textMessage = new Label(text);
        textMessage.setFont(Font.font(15));
        textMessage.setWrapText(true);
        return textMessage;
    }

    void addMessagesToVbox(User user) throws Exception {
        ArrayList< ArrayList < Message > > currentMessages = getMessagesAfterTime((on_start ? null : StartApplication.timeOfLastMessage), user.getNickname());
        for (ArrayList<Message> currentMessage : currentMessages) {
            String text = getMessageText(currentMessage, user);
            friendsMessages.get(user.getId()).add(text);
            friendsArraysOfMessages.get(user.getId()).add(currentMessage);
            for(int i = 400; i <= 4000; i++){
                s.setWrappingWidth(i);
                s.setText(text);
                s.setFont(Font.font(15));
                s.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
                sizeOfMessages.get(user.getId()).set(i-400, sizeOfMessages.get(user.getId()).get(i-400)+s.getBoundsInLocal().getHeight() + 10.0);
            }
        }
    }

    public void sendingMessage() throws Exception {
        String text = textOfSending.getText();
        sendMessage(text, User.MainUser.getNickname(), currentFriend.getNickname());
        textOfSending.clear();
    }

    class TakeUserHandler implements EventHandler<ActionEvent> {
        private final User user;
        TakeUserHandler(User new_user) {
            this.user = new_user;
        }
        @Override
        public void handle(ActionEvent event) {            
            try {
                makeChatToUser(user);                
            } catch (Exception e) {                
                e.printStackTrace();
            }                    
        }

    }


    public Button generateUserField(User current){
        Canvas canvas = new Canvas(160, 30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextBaseline(VPos.CENTER);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(1);
        gc.fillRect(0, 0, 160, 30);
        gc.drawImage(getAvatar(current), 5, 5, 20, 20);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(15));
        gc.fillText(
                current.getNickname(),
                Math.round(canvas.getWidth()  / 4),
                Math.round(canvas.getHeight() / 2));
        Button userField = new Button("");
        userField.setMaxSize(162, 32);
        userField.setMinSize(162, 32);
        userField.setGraphic(canvas);
        userField.setOnAction(new TakeUserHandler(current));
        return userField;
    }

    public void setUp(){
        messagesList.setHbarPolicy(ScrollBarPolicy.NEVER);
        messagesList.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrolling.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrolling.setVbarPolicy(ScrollBarPolicy.NEVER);
        fieldForMessages.setSpacing(10);
        try{
            StartApplication.allFriends = Database.getChats();
        }catch(Exception e){
            return;
        }
        chats.setSpacing(10);
        setAllSize();
        StartApplication.primaryStage.widthProperty().addListener(stageSizeListener);
        StartApplication.primaryStage.heightProperty().addListener(stageSizeListener);
    }

    public void initialize() throws Exception{
        setUp();
        on_start = true;
       
        if(friendsMessages == null){
            friendsMessages = new HashMap<>();
            sizeOfMessages = new HashMap<>();
            friendsArraysOfMessages = new HashMap<>();
            for(User user : StartApplication.allFriends){                
                friendsMessages.put(user.getId(), new ArrayList<>());
                friendsArraysOfMessages.put(user.getId(), new ArrayList<>());
                sizeOfMessages.put(user.getId(), new ArrayList<>());              
                for(int i = 400; i <= 4000; i++){
                    sizeOfMessages.get(user.getId()).add(0.0);
                }
                addMessagesToVbox(user);
                chats.getChildren().add(generateUserField(user));
            }
            on_end = true;
        }
        else{
            for(User user : StartApplication.allFriends){
                chats.getChildren().add(generateUserField(user));
            }
        }        
        setAllSize();
        if(currentFriend != null){
            makeChatToUser(currentFriend);
        }          
        //UpdateMessages.StartThread(this);
        
    }

    public void goToSettings(){
        FXMLLoader loader = LoadXML.load("Scenes/Settings/SettingsPage.fxml");
        StartApplication.setScene(loader);
    }

}
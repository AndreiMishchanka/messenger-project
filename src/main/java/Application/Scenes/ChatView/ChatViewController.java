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
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static Data.Database.getMessagesAfterTime;
import static Data.Database.sendMessage;

public class ChatViewController {

    ChatViewController _this;


    @FXML
    public Button BackToLoginButton;

    @FXML
    public Button FindFriends;

    @FXML
    public VBox fieldForMessages;
    @FXML
    public ScrollPane messagesList;
    @FXML
    public Button usersNick;
    @FXML
    public VBox chats;
   // ArrayList


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
    @FXML
    private ImageView exitIcon;
    @FXML
    private ImageView settingsIcon;
    @FXML
    private ImageView searchIcon;
    @FXML
    private ImageView sendIcon;

    static User currentFriend = null;

    @FXML
    public Button settings;

    public static boolean isSticker(String text) {
        if (text == null || text.isEmpty() || text.length() > 30 || text.charAt(0) != ':') {
            return false;
        }
        return (StartApplication.class.getResource("Stickers/" + text.substring(1, text.length()) + ".png") != null);
    }

    public static ImageView getSticker(String text) {
        ImageView striker = new ImageView(new Image(String.valueOf(StartApplication.class.getResource("Stickers/" + text.substring(1, text.length()) + ".png")), 100, 100, false, false));
        return striker;
    }

     class ThreadHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                for(User user : StartApplication.allFriends){
                    int id = user.getId();
                    if(threadFriendsArraysOfMessages.get(id).size() == 0){
                        continue;
                    }
                    for(Message mes : threadFriendsArraysOfMessages.get(id)){
                        friendsArraysOfMessages.get(id).add(mes);
                        String text = getMessageText(mes, user);
                        Message m = mes;
                        
                        if(isSticker(m.getText())){
                            for(int i = 400; i <= 4000; i++){
                                sizeOfMessages.get(user.getId()).set(i-400, sizeOfMessages.get(id).get(i-400)+ 100.0 + 10.0);
                                s.setWrappingWidth(i);
                                s.setText(text);
                                s.setFont(Font.font(15));
                                s.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
                                sizeOfMessages.get(user.getId()).set(i-400, sizeOfMessages.get(id).get(i-400)+s.getBoundsInLocal().getHeight() + 10.0);
                            }
                            if(id == currentFriend.getId()){
                                fieldForMessages.getChildren().add(makeMessage(mes, user));
                                fieldForMessages.getChildren().add(getSticker(m.getText()));
                            }
                        }
                        else{
                            for(int i = 400; i <= 4000; i++){
                                s.setWrappingWidth(i);
                                s.setText(text);
                                s.setFont(Font.font(15));
                                s.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
                                sizeOfMessages.get(id).set(i-400, sizeOfMessages.get(id).get(i-400)+s.getBoundsInLocal().getHeight() + 10.0);
                            }
                            if(id == currentFriend.getId()){
                                fieldForMessages.getChildren().add(makeMessage(mes, user));
                                if(!m.isIamSender()) {
                                    Database.makeReadMessage(m.getId());
                                    mes.read();
                                }
                            }
                        }
                    }
                    if(id == currentFriend.getId()){

                       
                        setAllSize();
                        messagesList.setVvalue(1.0);
                    }
                    threadFriendsArraysOfMessages.get(id).clear();
                }
                Collections.sort(StartApplication.allFriends, new Compar());
                chats.getChildren().clear();
                for(User user : StartApplication.allFriends){                   
                    chats.getChildren().add(generateUserField(user));
                }
                UpdateMessages.StartThread(_this);
            } catch (Exception e) {
              
                e.printStackTrace();
            }
        }
    }

    static public Button xx;

    @FXML
    public SplitPane splitPanePageForChats;
    
    static boolean on_start = false, on_end = false;

    static Text s = new Text();
   
    static public HashMap<Integer, ArrayList<Message>> friendsArraysOfMessages = null;
    static public HashMap<Integer, ArrayList<Double>> sizeOfMessages = null;

    static public HashMap<Integer, ArrayList<Message>> threadFriendsArraysOfMessages = null;


    public static Image getAvatar(User user) {
        if (StartApplication.class.getResource("Images/" + user.getNickname() + ".png") == null) {
            return new Image(StartApplication.class.getResource("Images/default.png").toString(), 1000, 1000, false, false);
        }
        return new Image(StartApplication.class.getResource("Images/" + user.getNickname() + ".png").toString(), 1000, 1000, false, false);
    }

    public void setAllSize(){
        splitPanePageForChats.setDividerPosition(0, 180.0/StartApplication.stageWidth);
        scrolling.setPrefHeight(StartApplication.stageHeight-120);
        settings.setLayoutY(StartApplication.stageHeight-75);
        BackToLoginButton.setLayoutY(StartApplication.stageHeight-75);
        FindFriends.setLayoutY(StartApplication.stageHeight-75);
        sendMessageButton.setLayoutY(StartApplication.stageHeight-75);
        sendMessageButton.setLayoutX(StartApplication.stageWidth-70-180);
        textOfSending.setLayoutY(StartApplication.stageHeight-75);
        textOfSending.setLayoutX(0);
        textOfSending.setPrefWidth(StartApplication.stageWidth-70-180);
        messagesList.setPrefHeight(StartApplication.stageHeight-120);
        messagesList.setPrefWidth(StartApplication.stageWidth-190);        
        usersNick.setPrefWidth(fieldForMessages.getPrefWidth() - friendAvatar.getFitWidth());
        fieldForMessages.setPrefWidth(StartApplication.stageWidth-190);
        if(on_end){
            if(currentFriend != null){
                fieldForMessages.setPrefHeight(max((double)sizeOfMessages.get(currentFriend.getId()).get((int)(fieldForMessages.getPrefWidth())-400), StartApplication.stageHeight-125));
            }
            else{
                fieldForMessages.setPrefHeight(StartApplication.stageHeight-125);
            }
        }    
        else{
            fieldForMessages.setPrefHeight(StartApplication.stageHeight-125);
        }    
        fieldForMessages.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        messagesList.setVvalue(1.0);       
    }


    private double max(double d, double e) {
        if(d > e) return d;
        return e; 
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
        for(Message cur : friendsArraysOfMessages.get(currentFriend.getId())){
            Message m = cur;
            if(isSticker(m.getText())){
                fieldForMessages.getChildren().add(makeMessage(cur, currentFriend));
                fieldForMessages.getChildren().add(getSticker(m.getText()));
            }
            else{
                fieldForMessages.getChildren().add(makeMessage(cur, currentFriend));
            }
            if(cur.getIsRead() || cur.isIamSender()) continue;
            Database.makeReadMessage(cur.getId());
            cur.read();
        }        
        setAllSize(); 
        chats.getChildren().clear();
        for(User user : StartApplication.allFriends){                   
            chats.getChildren().add(generateUserField(user));
        }       
    }


    public static String getMessageText( Message  currentMessage, User user){
        StringBuilder text = new StringBuilder();
        if (currentMessage.isIamSender()) {
            text.append("You");
        } else {
            text.append(user.getNickname());
        }

        text.append(" (" + currentMessage.getTime() + ") :  "); text.append(isSticker(currentMessage.getText()) ? "" : currentMessage.getText());
        return text.toString();
    }

    static Label makeMessage(Message  currentMessage, User user) {
        String text = getMessageText(currentMessage, user);
        Label textMessage = new Label(text);
        textMessage.setFont(Font.font(15));
        textMessage.setWrapText(true);

        //String label_style = "-fx-border-color: red;-fx-padding:20;" + "-fx-border-width: 1;"
        //        + "-fx-border-style: dotted;"
        //        + "-fx-font: 44 sans-serif; -fx-stroke: blue; -fx-fill: blue; -fx-scale-y: 1.2;";


        //textMessage.setBackground(Background.fill(Color.WHITE));
        //textMessage.setStyle(" -fx-border-radius: 50%");
        return textMessage;
    }

    void addMessagesToVbox(User user) throws Exception {
        ArrayList < Message > currentMessages = getMessagesAfterTime((on_start ? null : StartApplication.timeOfLastMessage), user.getNickname());
        for (Message currentMessage : currentMessages) {
            String text = getMessageText(currentMessage, user);
            friendsArraysOfMessages.get(user.getId()).add(currentMessage);

            Message m = currentMessage;
            if(isSticker(m.getText())){
                for(int i = 400; i <= 4000; i++){
                    sizeOfMessages.get(user.getId()).set(i-400, sizeOfMessages.get(user.getId()).get(i-400)+ 100.0 + 10.0);
                }
            }
           
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
      
        try{
            boolean is_read = friendsArraysOfMessages.get(current.getId()).get(friendsArraysOfMessages.get(current.getId()).size()-1).getIsRead();
           
            if(is_read == false){
                gc.setFill(Color.BLUE);
                gc.fillOval(150, 20, 5, 5);
            }
        }catch(Exception e){

        }
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
        BackToLoginButton.setGraphic(exitIcon);
        settings.setGraphic(settingsIcon);
        FindFriends.setGraphic(searchIcon);
        sendMessageButton.setGraphic(sendIcon);
        _this = this;
        setUp();
        xx = new Button(); xx.setOnAction(new ThreadHandler());
        on_start = true;
       
        if(friendsArraysOfMessages == null){
            sizeOfMessages = new HashMap<>();
            friendsArraysOfMessages = new HashMap<>();
            threadFriendsArraysOfMessages = new HashMap<>();
           
            for(User user : StartApplication.allFriends){                
                friendsArraysOfMessages.put(user.getId(), new ArrayList<>());
                sizeOfMessages.put(user.getId(), new ArrayList<>());              
                for(int i = 400; i <= 4000; i++){
                    sizeOfMessages.get(user.getId()).add(0.0);
                }

                threadFriendsArraysOfMessages.put(user.getId(), new ArrayList<>());
               
                addMessagesToVbox(user);
            }
            Collections.sort(StartApplication.allFriends, new Compar());
            for(User user : StartApplication.allFriends){                   
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
       UpdateMessages.StartThread(this);
        
    }

    public void goToSettings(){
        FXMLLoader loader = LoadXML.load("Scenes/Settings/SettingsPage.fxml");
        StartApplication.setScene(loader);
    }

    @FXML
    void goToFindFriends(ActionEvent event) {

    }

    @FXML
    void goToLogin(ActionEvent event) {
        on_start = false;
        on_end = false;
        friendsArraysOfMessages = null;
        threadFriendsArraysOfMessages = null;
        sizeOfMessages = null;
        currentFriend = null;
        UpdateMessages.FinishThread();
        FXMLLoader loader = LoadXML.load("hello-view.fxml");
        StartApplication.setScene(loader);
    }

    static public class Compar implements Comparator<User>{
        public int compare(User s1, User s2)
        {
            Timestamp t1 = null;
            Timestamp t2 = null;
            try{
                t1 = friendsArraysOfMessages.get(s1.getId()).get(friendsArraysOfMessages.get(s1.getId()).size()-1).getTimeStamp();
            }catch(Exception e){

            }
            try{
                t2 = friendsArraysOfMessages.get(s2.getId()).get(friendsArraysOfMessages.get(s2.getId()).size()-1).getTimeStamp();
            }catch(Exception e){
                
            }
            if(t1 == null && t2 == null){
                return 0;
            }
            if(t1 == null) return 1;
            if(t2 == null) return -1;
            if(t1.after(t2)) return -1;
            if(t1.before(t2)) return 1;
            return 0;
        }
    } 


}
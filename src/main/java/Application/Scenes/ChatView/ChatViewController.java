package Application.Scenes.ChatView;

import Application.StartApplication;
import Data.Database;
import Data.Message;
import Data.Sticker;
import Data.User;
import Utills.LoadXML;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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

    public static String StickerChooser;

    @FXML
    private Button chooseStickerButton;

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
    public AnchorPane anch1;
    public Pane writepane, friendPane;

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

    static public Button xx;

    @FXML
    public SplitPane splitPanePageForChats;
    
    static boolean on_start = false, on_end = false;

    static Text s = new Text();
   
    static public HashMap<Integer, ArrayList<Message>> friendsArraysOfMessages = null;

    static public HashMap<Integer, ArrayList<Message>> threadFriendsArraysOfMessages = null;


    public static boolean isSticker(String text) {
        if (text == null || text.isEmpty() || text.length() > 30 || text.charAt(0) != ':') {
            return false;
        }
        return (StartApplication.class.getResource("Stickers/" + text.substring(1, text.length()) + ".png") != null);
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
                            if(currentFriend != null && id == currentFriend.getId()){
                                fieldForMessages.getChildren().add(makeMessage(mes, user));
                                fieldForMessages.getChildren().add(Sticker.getSticker(m.getText()));
                            }
                        }
                        else{
                            if(currentFriend != null && id == currentFriend.getId()){
                                fieldForMessages.getChildren().add(makeMessage(mes, user));
                                if(!m.isIamSender()) {
                                    Database.makeReadMessage(m.getId());
                                    mes.read();
                                }
                            }
                        }
                    }
                    if(currentFriend != null && id == currentFriend.getId()){

                       
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

    public static Image getAvatar(User user) {
        if (StartApplication.class.getResource("Images/" + user.getNickname() + ".png") == null) {
            return new Image(StartApplication.class.getResource("Images/default.png").toString(), 1000, 1000, false, false);
        }
        return new Image(StartApplication.class.getResource("Images/" + user.getNickname() + ".png").toString(), 1000, 1000, false, false);
    }

    public void setAllSize(){
        anch1.setPrefHeight(StartApplication.stageHeight-30);
        anch1.setPrefWidth(StartApplication.stageWidth-200);
        anch1.setLayoutX(200);
        anch1.setLayoutY(0);
        writepane.setPrefWidth(anch1.getPrefWidth());
        writepane.setPrefHeight(40);
        writepane.setLayoutY(anch1.getPrefHeight() - 40);
        textOfSending.setLayoutX(0);
        textOfSending.setLayoutY(4);
        textOfSending.setPrefWidth(anch1.getPrefWidth()-70);
        chooseStickerButton.setLayoutX(writepane.getPrefWidth() -90);
        sendMessageButton.setLayoutX(writepane.getPrefWidth() -40);
        friendPane.setPrefHeight(StartApplication.stageHeight-30);
        friendPane.setPrefWidth(200);
        friendPane.setLayoutX(0);
        friendPane.setLayoutY(0);
        settings.setLayoutX(150);
        FindFriends.setLayoutX(80);
        BackToLoginButton.setLayoutX(10);
        settings.setLayoutY(friendPane.getPrefHeight()-50);
        FindFriends.setLayoutY(friendPane.getPrefHeight()-50);
        BackToLoginButton.setLayoutY(friendPane.getPrefHeight()-50);
        messagesList.setLayoutY(52);
        messagesList.setPrefHeight(anch1.getPrefHeight()-100);
        messagesList.setPrefWidth(anch1.getPrefWidth()-5);
        fieldForMessages.setPrefHeight(anch1.getPrefHeight()-100);
        fieldForMessages.setPrefWidth(anch1.getPrefWidth()-5);
        scrolling.setPrefHeight(friendPane.getPrefHeight()-60);
        chats.setPrefHeight(friendPane.getPrefHeight()-60);

       if(currentFriend != null){
                Canvas canvas = new Canvas(anch1.getPrefWidth()-5, 50);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setTextBaseline(VPos.CENTER);
                gc.setStroke(Color.BLACK);
                gc.setFill(Color.web("#E1F5FE"));
                gc.setLineWidth(1);
                gc.fillRect(0, 0, anch1.getPrefWidth()-5, 50);
                gc.drawImage(getAvatar(currentFriend), 5, 5, 50, 50);
                gc.setFill(Color.BLACK);
                gc.setFont(new Font(15));
                gc.fillText(
                    currentFriend.getNickname(),
                        Math.round(canvas.getWidth()  / 4),
                        Math.round(canvas.getHeight() / 2));
                usersNick.setMaxSize(anch1.getPrefWidth()-5, 50);
                usersNick.setMinSize(anch1.getPrefWidth()-5, 50);
                usersNick.setGraphic(canvas);
                usersNick.setVisible(true);
                double h = 0;
                for(Node i : fieldForMessages.getChildren()){
                    try{
                        Label z = (Label)i;
                        z.setPrefWidth(fieldForMessages.getPrefWidth());
                        s.setWrappingWidth(fieldForMessages.getPrefWidth());
                        s.setText(z.getText());
                        s.setFont(Font.font(15));
                        s.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
                        h += s.getBoundsInLocal().getHeight() + 10.0;
                    }catch(Exception e){
                        h += 110.0;
                    }
                }
                fieldForMessages.setPrefHeight(max(h, fieldForMessages.getPrefHeight()));
       }

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
        currentFriend = currentUser;
        fieldForMessages.getChildren().clear();
        for(Message cur : friendsArraysOfMessages.get(currentFriend.getId())){
           // Pane pane = new Pane(); pane.setStyle("-fx-background-color: #0277BD,#039BE5; -fx-background-insets: 0,9 9 8 9,9,10,11; -fx-background-radius: 50; -fx-padding: 15 30 15 30; -fx-font-size: 18px; -fx-text-fill: #311c09; -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.1) , 2, 0.0 , 0 , 1)";
          //  pane.setPrefWidth(400);

            Message m = cur;
            if(isSticker(m.getText())){
                fieldForMessages.getChildren().add(makeMessage(cur, currentFriend));
                fieldForMessages.getChildren().add(Sticker.getSticker(m.getText()));
            }
            else{
                fieldForMessages.getChildren().add(makeMessage(cur, currentFriend));
            }
            if(cur.getIsRead() || cur.isIamSender()) continue;
            Database.makeReadMessage(cur.getId());
            cur.read();
        }         
        Collections.sort(StartApplication.allFriends, new Compar());
        chats.getChildren().clear();
        for(User user : StartApplication.allFriends){                   
            chats.getChildren().add(generateUserField(user));
        }  
        setAllSize(); 
        messagesList.setVvalue(1.0);   
        writepane.setVisible(true); 
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
        return textMessage;
    }

    void addMessagesToVbox(User user) throws Exception {
        ArrayList < Message > currentMessages = getMessagesAfterTime((on_start ? null : StartApplication.timeOfLastMessage), user.getId());
        for (Message currentMessage : currentMessages) {
            String text = getMessageText(currentMessage, user);
            friendsArraysOfMessages.get(user.getId()).add(currentMessage);
            Message m = currentMessage; 
        }
    }

    public void sendingMessage() throws Exception {
        if(textOfSending.getText() == "" || textOfSending.getText().charAt(0) == ' '){
            return;
        }
        String text = textOfSending.getText();
        sendMessage(text, User.MainUser.getId(), currentFriend.getId());
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
        Canvas canvas = new Canvas(195, 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextBaseline(VPos.CENTER);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.web("#E1F5FE"));
        gc.setLineWidth(1);
        gc.fillRect(0, 0, 195, 40);
        gc.drawImage(getAvatar(current), 5, 5, 30, 30);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(15));
        gc.fillText(
                current.getNickname(),
                Math.round(canvas.getWidth()  / 4),
                Math.round(canvas.getHeight() / 2));
        Button userField = new Button("");
        userField.setMaxSize(195, 40);
        userField.setMinSize(195, 40);
      
        try{
            boolean is_read = friendsArraysOfMessages.get(current.getId()).get(friendsArraysOfMessages.get(current.getId()).size()-1).getIsRead();
            if(is_read == false){
                gc.setFill(Color.BLUE);
                gc.fillOval(170, 25, 5, 5);
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
            if(friendsArraysOfMessages != null){
                for(User u : StartApplication.allFriends){
                    if(!ChatViewController.friendsArraysOfMessages.keySet().contains(u.getId())){
                        ChatViewController.friendsArraysOfMessages.put(u.getId(), new ArrayList<>());
                        ChatViewController.threadFriendsArraysOfMessages.put(u.getId(), new ArrayList<>());
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        chats.setSpacing(10);
        setAllSize();
        StartApplication.primaryStage.widthProperty().addListener(stageSizeListener);
        StartApplication.primaryStage.heightProperty().addListener(stageSizeListener);
    }

      
    public void initialize() throws Exception{
        writepane.setVisible(false); 
        sendMessageButton.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/send.png")), 30, 30, false, false)));
        chooseStickerButton.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/stik.png")), 30, 30, false, false)));
        FindFriends.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/find.png")), 30, 30, false, false)));
        settings.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/set.png")), 30, 30, false, false)));
        BackToLoginButton.setGraphic(new ImageView(new Image(String.valueOf(StartApplication.class.getResource("ButtonsImages/bl.png")), 30, 30, false, false)));

        textOfSending.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent ke) {
            if(textOfSending.getText() == "" || textOfSending.getText().charAt(0) == ' '){
                return;
            }
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String text = textOfSending.getText();
                try {
                    sendMessage(text, User.MainUser.getId(), currentFriend.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textOfSending.clear();
            }
        }
        });

        _this = this;
       
        xx = new Button(); xx.setOnAction(new ThreadHandler());
        on_start = true;
        setUp();
        if(friendsArraysOfMessages == null){
            friendsArraysOfMessages = new HashMap<>();
            threadFriendsArraysOfMessages = new HashMap<>();
           
            for(User user : StartApplication.allFriends){                
                friendsArraysOfMessages.put(user.getId(), new ArrayList<>());
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
            Collections.sort(StartApplication.allFriends, new Compar());
            for(User user : StartApplication.allFriends){
                chats.getChildren().add(generateUserField(user));
            }
        }        
        setAllSize();
        if(currentFriend != null){
            makeChatToUser(currentFriend);
        }
       UpdateMessages.StartThread(this);
       if (StickerChooser != null) {
           textOfSending.setText(StickerChooser);
           StickerChooser = null;
       }
    }

    public void goToSettings(){
        FXMLLoader loader = LoadXML.load("Scenes/Settings/SettingsPage.fxml");
        StartApplication.setScene(loader);
    }

    @FXML
    void chooseSticker(ActionEvent event) {
        FXMLLoader loader = LoadXML.load("Scenes/Settings/StickerChoosing.fxml");
        StartApplication.setScene(loader);
    }

    @FXML
    void goToFindFriends(ActionEvent event) {
        FXMLLoader loader = LoadXML.load("Scenes/Settings/FindFriend.fxml");
        StartApplication.setScene(loader);
    }

  
    public void goToLogin() {
        on_start = false;
        on_end = false;
        friendsArraysOfMessages = null;
        threadFriendsArraysOfMessages = null;
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
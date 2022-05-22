package Application.Scenes.ChatView;

import Data.*;
import Utills.LoadXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

import Application.StartApplication;

import static Data.Database.getMessages;
import static Data.Database.sendMessage;

public class ChatViewController {

    @FXML
    private VBox fieldForMessages;
    @FXML
    private ScrollPane messagesList;
    @FXML
    private Button usersNick;
    @FXML
    public VBox chats;
    @FXML
    public TextField searchInChats;
    @FXML
    public ScrollPane scrolling;
    @FXML
    private TextField textOfSending;
    @FXML
    private Button sendMessageButton;
    @FXML
    private ImageView friendAvatar;

    static User currentFriend = null;

    @FXML
    private SplitPane splitPanePageForChats;


    public void changeSizes(){
        splitPanePageForChats.setDividerPosition(0, 180.0/StartApplication.stageWidth);
    }

    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
        StartApplication.stageWidth = StartApplication.primaryStage.getWidth();
        StartApplication.stageHeight = StartApplication.primaryStage.getHeight();
        changeSizes();
    };


    public void makeChatToUser(User currentUser) throws Exception {
        Image a = getAvatar(currentUser);
        friendAvatar.setImage(a);
        currentFriend = currentUser;
        usersNick.setText(currentFriend.getNickname());
        fieldForMessages.getChildren().removeAll(fieldForMessages.getChildren());
        printMessages();
        // messagesList.heightProperty().addListener(observable -> messagesList.setVvalue(1D));
    }


    Label makeMessage(ArrayList < Message > currentMessage) {
        StringBuilder text = new StringBuilder();
        if (Objects.equals(currentMessage.get(0).getText(), "|0|")) {
            text.append("You");
        } else {
            text.append(currentFriend.getNickname());
        }
        text.append(" (" + currentMessage.get(1).getTime() + ") : " + currentMessage.get(1).getText());
        Label textMessage = new Label(text.toString());
        textMessage.setFont(Font.font("Arial", 15));
        textMessage.setWrapText(true);
        return textMessage;
    }

    void printMessages() throws Exception {
        ArrayList< ArrayList < Message > > currentMessages = getMessages(currentFriend.getNickname());
        Label emptyChatLabel = new Label();
        fieldForMessages.getChildren().add(emptyChatLabel);
        if (currentMessages == null || currentMessages.isEmpty()) {
            emptyChatLabel.setText("Start a conversation! Write first message to " + currentFriend.getNickname() + '!');
            emptyChatLabel.setFont(Font.font(20));
        } else {
            emptyChatLabel.setText("");
            for (ArrayList<Message> currentMessage : currentMessages) {
                fieldForMessages.getChildren().add(makeMessage(currentMessage));
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.print(user.getNickname());
        }

    }

    public Image getAvatar(User user) {
        Image avatar = new Image(StartApplication.class.getResource("Images/default.png").toString());
        String path = new String("Images/" + user.getNickname() + ".png");
        if (StartApplication.class.getResource(path) == null) {
            return avatar;
        }
        avatar = new Image(StartApplication.class.getResource(path).toString());
        return avatar;
    }

    public Button generateUserField(User current){
        Canvas canvas = new Canvas(160, 30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextBaseline(VPos.CENTER);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(1);
        gc.fillRect(0, 0, 160, 30);
        gc.setFill(Color.RED);
        gc.fillRect(5, 5, 20, 20);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(15));
        Image a = getAvatar(current);
        gc.drawImage(a, 5, 5, 15, 15);
        gc.fillText(
                current.getNickname(),
                Math.round(canvas.getWidth()  / 4),
                Math.round(canvas.getHeight() / 2));
        Button z1 = new Button("");
        z1.setMaxSize(162, 32);
        z1.setMinSize(162, 32);
        z1.setGraphic(canvas);
        z1.setOnAction(new TakeUserHandler(current));
        return z1;
    }

    public void initialize() throws Exception{
        ///change_size
        StartApplication.primaryStage.widthProperty().addListener(stageSizeListener);
        StartApplication.primaryStage.heightProperty().addListener(stageSizeListener);
        changeSizes();

        ///init users
        messagesList.setHbarPolicy(ScrollBarPolicy.NEVER);
        messagesList.setVbarPolicy(ScrollBarPolicy.NEVER);
        fieldForMessages.setSpacing(10);
        ArrayList<User> users;
        try{
            users = Database.getChats();
        }catch(Exception e){
            return;
        }
        scrolling.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrolling.setVbarPolicy(ScrollBarPolicy.NEVER);
        for(User user : users){
            System.out.print(user.getNickname());
            Button userButton = generateUserField(user);
            chats.getChildren().add(userButton);
        }
        //chats.set
        chats.setSpacing(10);
        if(currentFriend != null){
            makeChatToUser(currentFriend);
        }
    }

    public void goToSettings(){
        FXMLLoader loader = LoadXML.load("Scenes/Settings/SettingsPage.fxml");
        StartApplication.setScene(loader);
    }

}
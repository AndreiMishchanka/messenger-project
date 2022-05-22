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
import java.util.Objects;
import java.util.SplittableRandom;

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
    private Button settings;

    @FXML
    private SplitPane splitPanePageForChats;

    static Text s = new Text();

    static {
        s.setFont(Font.font(15));
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
        fieldForMessages.setPrefWidth(StartApplication.stageWidth-200);
        usersNick.setPrefWidth(fieldForMessages.getPrefWidth() - friendAvatar.getFitWidth());
        s.setWrappingWidth(fieldForMessages.getPrefWidth());
    }

    public void setMessageField(){
        fieldForMessages.setPrefHeight(1);
        double sum = 0;
        for(Node field : fieldForMessages.getChildren()){
            Label text = (Label) field;
            s.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
            s.setText(text.getText());
            double h = s.getLayoutBounds().getHeight();
            sum += h + 10.0;
        }
        fieldForMessages.setPrefHeight(sum);
    }

    public void changeSizes(){
        setAllSize();
        setMessageField();

    }

    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
        StartApplication.stageWidth = StartApplication.primaryStage.getWidth();
        StartApplication.stageHeight = StartApplication.primaryStage.getHeight();
        changeSizes();
    };


    public void makeChatToUser(User currentUser) throws Exception {
        if(currentFriend == currentUser) return;
        Image a = new Image(StartApplication.class.getResource("Images/default.png").toString());
        friendAvatar.setImage(a);
        currentFriend = currentUser;
        usersNick.setText(currentFriend.getNickname());
        fieldForMessages.getChildren().removeAll(fieldForMessages.getChildren());
        printMessages();
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
        textMessage.setFont(Font.font(15));
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
            fieldForMessages.getChildren().removeAll(fieldForMessages.getChildren());
            for (ArrayList<Message> currentMessage : currentMessages) {
                fieldForMessages.getChildren().add(makeMessage(currentMessage));
            }
            changeSizes();

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
        Image a = new Image(StartApplication.class.getResource("Images/default.png").toString());
        gc.drawImage(a, 5, 5, 15, 15);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(15));
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
            Button userButton = generateUserField(user);
            chats.getChildren().add(userButton);
        }
        chats.setSpacing(10);

        ///change_size
        changeSizes();
        StartApplication.primaryStage.widthProperty().addListener(stageSizeListener);
        StartApplication.primaryStage.heightProperty().addListener(stageSizeListener);

        //chats.set

        if(currentFriend != null){
            makeChatToUser(currentFriend);
        }
    }

    public void goToSettings(){
        FXMLLoader loader = LoadXML.load("Scenes/Settings/SettingsPage.fxml");
        StartApplication.setScene(loader);
    }

}
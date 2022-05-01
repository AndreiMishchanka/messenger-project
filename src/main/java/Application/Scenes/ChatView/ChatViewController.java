package Application.Scenes.ChatView;

import Data.Database;
import Data.Message;
import Data.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Objects;

import static Data.Database.getMessages;

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

    static User currentFriend = null;

    public void makeChatToUser(User currentUser) throws Exception {
        currentFriend = currentUser;
        usersNick.setText(currentFriend.getNickname());
        fieldForMessages.getChildren().removeAll(fieldForMessages.getChildren());
        printMessages();
        messagesList.hminProperty(); //set scroll coursor to the bottom
    }

    void refresh() {
        
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
        textMessage.setMinWidth(3);
        textMessage.setMaxWidth(300);
        textMessage.setMinHeight(1);
        textMessage.setMaxHeight(100);
        return textMessage;
    }

    void printMessages() throws Exception {
        ArrayList< ArrayList < Message > > currentMessages = getMessages(currentFriend.getNickname());
        //System.out.print(currentMessages);
        for (ArrayList < Message > currentMessage : currentMessages) {
             fieldForMessages.getChildren().add(makeMessage(currentMessage));
        }
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
        //chats.set
        chats.setSpacing(10);
       
    }


}

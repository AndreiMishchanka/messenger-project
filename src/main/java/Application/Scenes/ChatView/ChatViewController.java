package Application.Scenes.ChatView;

import Data.Message;
import Data.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

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
    
    static User currentFriend = null;

    @FXML
    public void initialize() {
        usersNick.setText(User.MainUser.getNickname());
        messagesList.hminProperty(); //set scroll coursor to the bottom
    }

    public void makeChatToUser(User currentUser) throws Exception {
        currentFriend = currentUser;
        printMessages();
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
        for (ArrayList < Message > currentMessage : currentMessages) {
             fieldForMessages.getChildren().add(makeMessage(currentMessage));
        }
        fieldForMessages.getChildren().removeAll(fieldForMessages.getChildren());
    }

}

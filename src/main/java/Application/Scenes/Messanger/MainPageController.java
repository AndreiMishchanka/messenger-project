package Application.Scenes.Messanger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Data.Database;
import Data.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class MainPageController {

    public VBox chats;
    public TextField searchInChats;
    public ScrollPane scrolling;

    class TakeUserHandler implements EventHandler<ActionEvent> {
        private final User user;
        TakeUserHandler(User new_user) {
            this.user = new_user;
        }
        @Override
        public void handle(ActionEvent event) {
            //makeChatToUser(user);
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

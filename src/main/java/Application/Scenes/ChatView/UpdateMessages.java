package Application.Scenes.ChatView;

import java.util.ArrayList;

import Application.StartApplication;
import Data.Database;
import Data.Message;
import Data.User;
import javafx.scene.text.Font;
import javafx.scene.text.TextBoundsType;

public class UpdateMessages {
    public static ChatViewController x;
    public static class  FindMessages implements Runnable{
        public void addMessages(User user){
            ArrayList< ArrayList < Message > > currentMessages = null;
            try{ 
                currentMessages =  Database.getMessagesAfterTime(StartApplication.timeOfLastMessage, user.getNickname());
                System.out.println(StartApplication.timeOfLastMessage);
            }catch(Exception e){
                System.out.println("GEST!!!!!!");
            }
            for (ArrayList<Message> currentMessage : currentMessages) {
                String text = ChatViewController.getMessageText(currentMessage, user);
                ChatViewController.friendsMessages.get(user.getId()).add(text);
                ChatViewController.friendsArraysOfMessages.get(user.getId()).add(currentMessage);
                for(int i = 400; i <= 4000; i++){
                    ChatViewController.s.setWrappingWidth(i);
                    ChatViewController.s.setText(text);
                    ChatViewController.s.setFont(Font.font(15));
                    ChatViewController.s.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
                    ChatViewController.sizeOfMessages.get(user.getId()).set(i-400, ChatViewController.sizeOfMessages.get(user.getId()).get(i-400)+ChatViewController.s.getBoundsInLocal().getHeight() + 10.0);
                }
                if(ChatViewController.currentFriend != null){
                    if(ChatViewController.currentFriend.getId() == user.getId()){
                        x.fieldForMessages.getChildren().add(ChatViewController.makeMessage(currentMessage, user));
                        x.fieldForMessages.setPrefHeight(ChatViewController.sizeOfMessages.get(ChatViewController.currentFriend.getId()).get((int)(x.fieldForMessages.getPrefWidth())-400));
                
                  }
                }
            }
        }

        @Override
        public void run(){
            while(true){
                for(User user : StartApplication.allFriends){
                    System.out.print(user.getNickname() + " " + user.getId());        
                    addMessages(user);
                }
                try{
                    Thread.sleep(1000);
                }catch(Exception e){

                }
            }
        }
    }

    static public void StartThread(ChatViewController xx){
        x = xx;
        Thread find = new Thread(new FindMessages());
        find.setDaemon(true);
        find.start();
    }

}

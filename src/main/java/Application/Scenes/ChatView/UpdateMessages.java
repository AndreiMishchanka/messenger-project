package Application.Scenes.ChatView;

import java.sql.Timestamp;
import java.util.ArrayList;

import Application.StartApplication;
import Data.Database;
import Data.Message;
import Data.User;
import javafx.application.Platform;


public class UpdateMessages {
    public static ChatViewController x;
    public static Thread find;
    public static class  FindMessages implements Runnable{
   
        @Override
        public void run(){
            try{
                while(true){
                    Timestamp time = StartApplication.timeOfLastMessage;
                    boolean need = false;

                    if(ChatViewController.currentFriend != null){
                        User user =ChatViewController.currentFriend;
                        ArrayList< ArrayList < Message > > currentMessages = null;
                        try{ 
                            currentMessages =  Database.getMessagesAfterTime(time, user.getNickname());
                            if(currentMessages.size() > 0){
                                need = true;
                                for (ArrayList<Message> currentMessage : currentMessages) {
                                    ChatViewController.threadFriendsArraysOfMessages.get(user.getId()).add(currentMessage);
                                }

                            }
                        }catch(Exception e){
                            return;
                        }

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run(){
                                ChatViewController.xx.fire();
                            }
                        });
                        return;
                    }

                    for(User user : StartApplication.allFriends){
                        ArrayList< ArrayList < Message > > currentMessages = null;
                        try{ 
                            currentMessages =  Database.getMessagesAfterTime(time, user.getNickname());
                            if(currentMessages.size() > 0){
                                need = true;
                                for (ArrayList<Message> currentMessage : currentMessages) {
                                    ChatViewController.threadFriendsArraysOfMessages.get(user.getId()).add(currentMessage);
                                }

                            }
                        }catch(Exception e){
                        return;
                        }
                    }
                
                    if(need){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run(){
                                ChatViewController.xx.fire();
                            }
                        });
                        return;
                    }

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        return;
                    }
                }
            }catch(Exception e){
                return;
            }
        }
    }

    static public void StartThread(ChatViewController xx){
        x = xx;
        find = new Thread(new FindMessages());
        find.setDaemon(true);
        find.start();
    }
    static public void FinishThread(){
        find.interrupt();
    }

    static public class ScrollDown implements Runnable{

        @Override
        public void run() {
          
         //    x.
        }

    }

}

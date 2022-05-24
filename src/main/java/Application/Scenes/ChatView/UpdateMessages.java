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
    public static class  FindMessages implements Runnable{

        @Override
        public void run(){
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
                        System.out.println("GEST!!!!!!");
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
                        System.out.println("GEST!!!!!!");
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

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
    public static int k = 0;
        @Override
        public void run(){
            try{
                while(true){
                    Timestamp time = StartApplication.timeOfLastMessage;
                    boolean need = false;                                
                    if(k == 100){
                        try{
                            StartApplication.allFriends = Database.getChats();
                            for(User u : StartApplication.allFriends){
                                if(!ChatViewController.friendsArraysOfMessages.keySet().contains(u.getId())){
                                    ChatViewController.friendsArraysOfMessages.put(u.getId(), new ArrayList<>());
                                    ChatViewController.threadFriendsArraysOfMessages.put(u.getId(), new ArrayList<>());
                                }
                            }
                        }catch(Exception e){
                            return;
                        }
                        k = 0;
                    }                    
                    k++;
                    for(User user : StartApplication.allFriends){
                        ArrayList< Message > currentMessages = null;
                        try{ 
                            currentMessages =  Database.getMessagesAfterTime(time, user.getId());
                            if(currentMessages.size() > 0){
                                need = true;
                                for (Message currentMessage : currentMessages) {
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
                        Thread.sleep(100);
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

}

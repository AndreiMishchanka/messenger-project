package Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import Application.StartApplication;

public class Message {
    private String text;
    private int fromUser, toUser;
    private Timestamp time;
    private int id;
    private boolean is_read;


    private Message(int id, String text, int fromUser, int toUser, Timestamp time, boolean is_read) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.is_read = is_read;
    }

    static public Message generateMessage(int id, String text, int fromUser, int toUser, Timestamp time, boolean is_read) {
        return new Message(id, text, fromUser, toUser, time, is_read);
    }

    public String getText() {
        return text;
    }


    public void read(){
        is_read = true;
    }

    public String getTime() {
        
        return new SimpleDateFormat("dd MMMMM HH:mm", Locale.ENGLISH).format(time);
    }
    public Timestamp getTimeStamp() {
        
        return time;
    }
    
    public int getId() {
        return id;
    }

    public int getFromUser() {
        return fromUser;
    }

    public int getToUser() {
        return toUser;
    }

    public boolean getIsRead() {
        return this.is_read;
    }
    public boolean isIamSender(){
        if(fromUser == User.MainUser.getId()){
            return true;
        }
        return false;
    }

}

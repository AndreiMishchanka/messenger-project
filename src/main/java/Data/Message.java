package Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Message {
    private String text;
    private int fromUser, toUser;
    private Timestamp time;
    private int id;


    private Message(int id, String text, int fromUser, int toUser, Timestamp time) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    static public Message generateMessage(int id, String text, int fromUser, int toUser, Timestamp time) {
        return new Message(id, text, fromUser, toUser, time);
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        
        return new SimpleDateFormat("dd MMMMM HH:mm", Locale.ENGLISH).format(time);
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
}

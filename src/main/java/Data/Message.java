package Data;

import java.sql.Time;

public class Message {
    private String text;
    private int fromUser, toUser;
    private Time time;
    private int id;


    private Message(int id, String text, int fromUser, int toUser, Time time) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    static public Message generateMessage(int id, String text, int fromUser, int toUser, Time time) {
        return new Message(id, text, fromUser, toUser, time);
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return "Unknown time";
    }
}

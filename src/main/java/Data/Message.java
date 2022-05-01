package Data;

public class Message {
    private String text;

    private Message(String text) {
        this.text = text;
    }

    static public Message generateMessage(String text) {
        return new Message(text);
    }

    public String getText() {
        return text;
    }
}

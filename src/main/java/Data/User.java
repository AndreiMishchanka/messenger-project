package Data;

public class User {
    private String nickname;

    private User(String Nickname) {
        this.nickname = Nickname;
    }

    public String getNickname(){
        return this.nickname;
    }

    public static User AddUser(String nickname) {
        return new User(nickname);
    }

}

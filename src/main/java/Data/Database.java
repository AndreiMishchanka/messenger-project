package Data;

public class Database {
    static public User getUser(String nickname, String password) {
        return User.AddUser(nickname);
    }
}

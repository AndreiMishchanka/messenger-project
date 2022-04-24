package Data;

public class Database {
    public static class IncorrectPasswordException extends Exception {}
    static public User getUser(String nickname, String password) throws Exception{
        if (password == null || password.length() == 0) {
            throw new IncorrectPasswordException();
        }
        return User.AddUser(nickname);
    }
}

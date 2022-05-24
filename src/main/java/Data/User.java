package Data;


public class User {
    public static User MainUser = null;
    private String nickname;
    Integer id;

    public Integer getId(){
        return id;
    }

    private User(String Nickname, Integer id) {
        this.nickname = Nickname;
        this.id = id;
    }

    public String getNickname(){
        return this.nickname;
    }

    public static User makeUserFromBase(String nickname, Integer id) {
        return new User(nickname, id);
    }

}

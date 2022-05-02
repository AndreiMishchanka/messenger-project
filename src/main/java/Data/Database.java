package Data;

import java.sql.SQLData;
import java.sql.Timestamp;
import java.util.ArrayList;

import Data.SQLBase.SqlCommunicate;

public class Database {

    public static class IncorrectPasswordException extends Exception {}
    public static class IncorrectUserException extends Exception {}
    public static class UserAlreadyRegistred extends Exception {}

    static public User getUser(String nickname, String password) throws Exception{
        try{
            Password passwordCheck = new Password(password);
        }catch(Exception e){
            throw e;
        }
        try {
            String query = "select * from users where nickname = '" + nickname + "' and password = '" + password + "';";                        
            int id = Integer.parseInt(SqlCommunicate.execute(query).get(1).get(0));
            return Database.getUserById(id);
        }catch(Exception e) {
            e.printStackTrace();        
            
        }
        throw new IncorrectUserException();
    }

    static public int getIdByNick(String nickname) throws Exception{        
        String query = "select * from users where nickname = '" + nickname + "';";                        
        return Integer.parseInt(SqlCommunicate.execute(query).get(1).get(0));
    }

    static public User getUserById(int id) throws Exception{        
        try {
            String query = "select * from users where id = " + id + ";";
            ArrayList <String> A = SqlCommunicate.execute(query).get(1);
            return User.makeUserFromBase(A.get(1));
        }catch(Exception e) {
            e.printStackTrace();        
            throw new Exception();
        }        
    }

    static public void registerUser (String nickname, String password) throws Exception {
        try{
            Password passwordCheck = new Password(password);
        }catch(Exception e){
            throw e;
        }
        String query = "select * from users where nickname = '" + nickname + "';";          
        if (SqlCommunicate.execute(query).size() - 1 > 0) {
            throw new UserAlreadyRegistred();
        }         

        int id = SqlCommunicate.execute("select * from users;").size();

        query = "insert into users values(" + id + ", '" + nickname + "', '" + password + "');";
        
        SqlCommunicate.update(query);

    }

    static public ArrayList<User> getChats() throws Exception{
        if (User.MainUser == null) {
            return null;
        }
        ArrayList<User> output = new ArrayList<>();
        ArrayList<ArrayList<String>> arr = SqlCommunicate.execute("select * from users where nickname != '" + User.MainUser.getNickname() + "';");
        for (int i = 1; i < arr.size(); i++) {
            output.add(getUserById(Integer.parseInt(arr.get(i).get(0))));
        }        
        return output;
    }

    static public boolean isUserConsist(String nickname) throws Exception{
        
        String query = "select * from users where nickname = '" + nickname + "';";   
        if (SqlCommunicate.execute(query).size() < 2) {
            return false;                     
        }
        return true;
    }

    /** 
     * return ArrayList<ArrayList<Message>>
     * .get(i) -> return ArrayList<Message>, size of this List 2
     * .get(i).get(0) -> contains message with text "|1|" - it's not our message, and "|0|" - if we wrote this message
     * .get(i).get(1) -> message
    */
    static public ArrayList<ArrayList<Message>> getMessages(String with) throws Exception{
       

        if (User.MainUser == null) {
            return null;
        }
      
        
        if (SqlCommunicate.execute("select * from users where nickname = '" + with + "';").get(0).get(0) == "0") {
            return null;
        }
        
        ArrayList<ArrayList<Message>> output = new ArrayList<>();   

        String query = "select * from messages where (fuser = " + getIdByNick(User.MainUser.getNickname())
                                                    + " AND tuser = " + getIdByNick(with) + ") OR (fuser = " + getIdByNick(with)
                                                    + " AND tuser = " + getIdByNick(User.MainUser.getNickname()) + ");";

        ArrayList<ArrayList<String>> arr = SqlCommunicate.execute(query);

        for (int i = 1; i < arr.size(); i++) {            
            output.add(new ArrayList<Message>());     
            String tp = "|1|";
            int id = Integer.parseInt(arr.get(i).get(0));
            String text = arr.get(i).get(1);
            int fuser = Integer.parseInt(arr.get(i).get(2));
            int tuser = Integer.parseInt(arr.get(i).get(3));            
            Timestamp tm = Timestamp.valueOf(arr.get(i).get(4));
            if (fuser != getIdByNick(with)) tp = "|0|";            
            output.get(i - 1).add(Message.generateMessage(0, tp, 1, 1, null));            
            output.get(i - 1).add(Message.generateMessage(id, text, fuser, tuser, tm));
        }
        
                
        return output;
        
    }

    public static void sendMessage(String text, String nicknameFrom, String nicknameTo) throws Exception{ 
        //add time checking please

    }

}

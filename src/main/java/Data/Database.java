package Data;

import java.sql.SQLClientInfoException;
import java.sql.Timestamp;
import java.util.ArrayList;

import Application.StartApplication;
import Data.SQLBase.SqlCommunicate;

public class Database {

    public static class IncorrectPasswordException extends Exception {}
    public static class IncorrectUserException extends Exception {}
    public static class UserAlreadyRegistred extends Exception {}

    static public User getUser(String nickname, String password) throws Exception{
        try{
            new Password(password);
        }catch(Exception e){
            throw e;
        }
        try {
            String query = "select * from users where nickname = '" + nickname + "' and password = '" + password + "';";                        
            int id = Integer.parseInt(SqlCommunicate.execute(query).get(1).get(0));
            return Database.getUserById(id);
        }catch(Exception e) {
           //  throw new IncorrectUserException();
           // e.printStackTrace();        
            
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
            return User.makeUserFromBase(A.get(1), id);
        }catch(Exception e) {
            e.printStackTrace();        
            throw new Exception();
        }        
    }

    static public void registerUser (String nickname, String password) throws Exception {
        try{
           new Password(password);
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

    /*
    static public ArrayList<User> getChats() throws Exception{
        if (User.MainUser == null) {
            return null;
        }
        ArrayList<User> output = new ArrayList<>();        

        ArrayList<ArrayList<String>> arr = SqlCommunicate.execute(
            String.format("select * from relations where id_f = %s or id_s = %s;", 
                            User.MainUser.getId(), 
                            User.MainUser.getId()));
        
        for (int i = 1; i < arr.size(); i++) {
            if (Integer.parseInt(arr.get(i).get(1)) == User.MainUser.getId()) {
                output.add(getUserById(Integer.parseInt(arr.get(i).get(2))));
            }else {
                output.add(getUserById(Integer.parseInt(arr.get(i).get(1))));
            }            
        }        
        return output;
    }*/
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
    static public ArrayList<ArrayList<Message>> getMessagesAfterTime(Timestamp time, String with) throws Exception{
        if (User.MainUser == null) {
            return null;
        }
        ArrayList<ArrayList<Message>> output = new ArrayList<>();   


        String query = null;
        if(time == null){
             query = "select * from messages where (fuser = " + getIdByNick(User.MainUser.getNickname())
            + " AND tuser = " + getIdByNick(with) + ") OR (fuser = " + getIdByNick(with)
            + " AND tuser = " + getIdByNick(User.MainUser.getNickname()) + ");";
        }
        else{
            ///need to fi
            query = "select * from messages where ((fuser = " + getIdByNick(User.MainUser.getNickname())
                                                    + " AND tuser = " + getIdByNick(with) + ") OR (fuser = " + getIdByNick(with)
                                                    + " AND tuser = " + getIdByNick(User.MainUser.getNickname()) + ")) AND (" + " ' "+ time + "'" + " < tm "  + ");";
           // System.out.println(query);
        }

        ArrayList<ArrayList<String>> arr = SqlCommunicate.execute(query);
        for (int i = 1; i < arr.size(); i++) {            
            output.add(new ArrayList<Message>());     
            String tp = "|1|";
            int id = Integer.parseInt(arr.get(i).get(0));
            String text = arr.get(i).get(1);
            int fuser = Integer.parseInt(arr.get(i).get(2));
            int tuser = Integer.parseInt(arr.get(i).get(3));            
            Timestamp tm = Timestamp.valueOf(arr.get(i).get(4));
            boolean is_read = false;
            if(arr.get(i).get(5).equals("1")) is_read = true;
            if(StartApplication.timeOfLastMessage == null){
                StartApplication.timeOfLastMessage = tm;
            }
            else{
                if(StartApplication.timeOfLastMessage.before(tm)){
                    StartApplication.timeOfLastMessage = tm;
                }
            }
            if (fuser != getIdByNick(with)) tp = "|0|";            
            output.get(i - 1).add(Message.generateMessage(0, tp, 1, 1, null, is_read));            
            output.get(i - 1).add(Message.generateMessage(id, text, fuser, tuser, tm, is_read));
        }
        return output;
    }


    public static void sendMessage(String text, String nicknameFrom, String nicknameTo) throws Exception{ 
        //add time checking please

        if (isUserConsist(nicknameFrom) && isUserConsist(nicknameTo) && User.MainUser.getNickname().equals(nicknameFrom)) {
            int id = SqlCommunicate.execute("select * from messages;").size();
            Message nw = Message.generateMessage(id, text, getIdByNick(nicknameFrom), getIdByNick(nicknameTo), new Timestamp(System.currentTimeMillis()), false);        
            SqlCommunicate.update("insert into messages(id, text, fuser, tuser) values(" + nw.getId() 
                                                                 + ", '" + nw.getText() 
                                                                 + "', " + nw.getFromUser()
                                                                 + ", " + nw.getToUser()
                                                                 + ");");
        }

    }

    public static void changeNickname(String Nickname) throws Exception{
        if (isUserConsist(Nickname)) {
            throw new UserAlreadyRegistred();
        }
        //UPDATE users SET nickname = 'kostyaa' WHERE id = 3
        SqlCommunicate.update("update users set nickname = '" + Nickname + "' where id = " + getIdByNick(User.MainUser.getNickname()) + ";");
        User.MainUser = User.makeUserFromBase(Nickname, User.MainUser.id);
    }

    public static boolean isReadMessage(int id) throws Exception {
        ArrayList<ArrayList<String>> arr = SqlCommunicate.execute(String.format("select is_read from messages where id = %s;", id));
        if (arr.size() != 2) {
            return false;
        }
        if (arr.get(1).get(0).equals("1")) {
            return true;
        }
        return false;
    }

    public static void makeReadMessage(int id) throws Exception {
        SqlCommunicate.update(String.format("update messages set is_read = 1 where id = %s;", id));
    }

    public static void makeFriend(int id) throws Exception{
        if (User.MainUser != null && User.MainUser.getId() != id 
            && SqlCommunicate.execute(String.format("select count(*) from users where id = %s;", id)).get(1).get(0).equals(1)) {
                SqlCommunicate.update(String.format("insert into relations values(select count(*) from relations, %s, %s);", User.MainUser.getClass(), id));
            }
    }
}
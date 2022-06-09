package Data;

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
    static public ArrayList<Message> getMessagesAfterTime(Timestamp time, Integer id) throws Exception{
        if (User.MainUser == null) {
            return null;
        }
        ArrayList<Message> output = new ArrayList<>();   


        String query = null;
        if(time == null){
             query = "select * from messages where (fuser = " + getIdByNick(User.MainUser.getNickname())
            + " AND tuser = " + id + ") OR (fuser = " + id
            + " AND tuser = " + User.MainUser.getId() + ");";
        }
        else{
            ///need to fi
            query = "select * from messages where ((fuser = " + getIdByNick(User.MainUser.getNickname())
                                                    + " AND tuser = " +id + ") OR (fuser = " + id
                                                    + " AND tuser = " +  User.MainUser.getId() + ")) AND (" + " ' "+ time + "'" + " < tm "  + ");";
           // System.out.println(query);
        }

        ArrayList<ArrayList<String>> arr = SqlCommunicate.execute(query);
        for (int i = 1; i < arr.size(); i++) {            
            int idd = Integer.parseInt(arr.get(i).get(0));
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
            if (fuser != id) {is_read = true;}           
            output.add(Message.generateMessage(idd, text, fuser, tuser, tm, is_read));
        }
        return output;
    }


    public static void sendMessage(String text, Integer id_sender, Integer id_usera) throws Exception{ 
        //add time checking please

        if (true) {
            int id = SqlCommunicate.execute("select * from messages;").size();
            Message nw = Message.generateMessage(id, text, id_sender,id_usera, new Timestamp(System.currentTimeMillis()), false);        
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
        SqlCommunicate.update("update users set nickname = '" + Nickname + "' where id = " + getIdByNick(User.MainUser.getNickname()) + ";");
        User.MainUser = User.makeUserFromBase(Nickname, User.MainUser.id);
    }

    public static void changePassword(String oldPassword, String Password) throws Exception{
        try{
            new Password(Password);
         }catch(Exception e){
             throw e;
         }

        try {
            getUser(User.MainUser.getNickname(), oldPassword);
        }catch (Exception e) {            
            throw e;
        }                        
        SqlCommunicate.update("update users set password = '" + Password + "' where id = " + getIdByNick(User.MainUser.getNickname()) + ";");
        User.MainUser = User.makeUserFromBase(User.MainUser.getNickname(), User.MainUser.id);
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
            && SqlCommunicate.execute(String.format("select count(*) from users where id = %s;", id)).get(1).get(0).equals("1")) {                
                SqlCommunicate.update(String.format("insert into relations values((select count(*) from relations), %s, %s);", User.MainUser.getId(), id));                
            }else {
                throw new Exception();
            }
    }

    public static boolean isFriend(String Name) throws Exception {
        try{
            int id = getIdByNick(Name);
            int count  = Integer.parseInt(SqlCommunicate.execute(String.format(
                "select count(*) from relations where (id_f = %s and id_s = %s) or (id_f = %s and id_s = %s);",
                User.MainUser.getId(), 
                id, 
                id, 
                User.MainUser.getId())).get(1).get(0)
            );
            return count == 1;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }                
    }

    public static ArrayList<Integer> getGroups() throws Exception{
        if (User.MainUser == null) {
            return null;
        }        
        try{
            ArrayList<Integer> res = new ArrayList<>();
            ArrayList<ArrayList<String>> cache = SqlCommunicate.execute(String.format("selec group_id from group_users where users_d = %s;", User.MainUser.getId()));
            for (int i = 1; i < cache.size(); i++) {
                res.add(Integer.parseInt(cache.get(i).get(0)));
            }
            return res;
        }catch (Exception e) {
            e.printStackTrace();
        }    
        return null;    
    }

    static public ArrayList<Message> getMessagesAfterTimeFromGroup(Timestamp time, Integer with) throws Exception{
        if (User.MainUser == null) {
            return null;
        }
        ArrayList<Message> output = new ArrayList<>();   

        ArrayList<ArrayList<String>> arr;

        if(time == null){            
                arr = SqlCommunicate.execute(String.format
                    ("select * from group_messages where group_id = %s;", with));                                        
        }
        else{
            arr = SqlCommunicate.execute(String.format
                    ("select * from group_messages where group_id = %s AND %s < tm;", with, time));                                                    
        }
        for (int i = 1; i < arr.size(); i++) {            
            int id = Integer.parseInt(arr.get(i).get(0));
            int fuser = Integer.parseInt(arr.get(i).get(1));
            String text = arr.get(i).get(2);            
            Timestamp tm = Timestamp.valueOf(arr.get(i).get(3));
            int tuser = -with;                        
            boolean is_read = true;            
            if(StartApplication.timeOfLastMessage == null){
                StartApplication.timeOfLastMessage = tm;
            }
            else{
                if(StartApplication.timeOfLastMessage.before(tm)){
                    StartApplication.timeOfLastMessage = tm;
                }
            }
            // if (fuser != getIdByNick(with)) {is_read = true;}           
            output.add(Message.generateMessage(id, text, fuser, tuser, tm, is_read));
        }
        return output;
    }
}
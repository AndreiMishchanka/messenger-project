package Data;

public class Password {
    String password;

    static String correctChars = ".!,?-+";

    static public void checkIncorrectPasswordChar(Character x) throws Exception{
        if(x >= 'A' && x <= 'Z'){
            return;
        }
        if(x >= 'a' && x <= 'z'){
            return;
        }
        if(x >= '0' && x <= '9'){
            return;
        }
        if(correctChars.indexOf(x) != -1){
            return;
        }        
        throw new Database.IncorrectPasswordException();
    }


    Password(String newPassword) throws Exception{        
        if(newPassword == null || newPassword.length() == 0 || newPassword.length() > 100){            
            throw new Database.IncorrectPasswordException();
        }
        for(int posPassword = 0; posPassword < newPassword.length() ; posPassword++){
            checkIncorrectPasswordChar(newPassword.charAt(posPassword));
        }
    }
    
}

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
        if(password == null || password.length() == 0 || password.length() > 100){
            throw new Database.IncorrectPasswordException();
        }
        for(int posPassword = 0; posPassword < password.length() ; posPassword++){
            checkIncorrectPasswordChar(password.charAt(posPassword));
        }
    }
    
}

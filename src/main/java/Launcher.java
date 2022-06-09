import Application.StartApplication;
import Data.SQLBase.SqlCommunicate;
import java.sql.*;

public class Launcher {
    public static void main (String ... args){
        try{
            // SqlCommunicate.connect("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
            // SqlCommunicate.connect("jdbc:sqlserver://tcsenger.database.windows.net:1433;;databaseName=TCSenger ", "postgres", "Tcs12345");
            SqlCommunicate.connect("jdbc:sqlserver://tcsprojectserver.database.windows.net:1433;;databaseName=TCSessanger ", "postgres", "Tcs12345");
        }catch(Exception e) {
            e.printStackTrace();
        }
        StartApplication.main(args);
    }
}

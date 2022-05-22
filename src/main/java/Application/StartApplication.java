package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

import Data.SQLBase.SqlCommunicate;
import Utills.LoadXML;

public class StartApplication extends Application {
    public static Stage primaryStage;
    public static double stageWidth = 600;
    public static double stageHeight = 600;

    public static void setScene(FXMLLoader loader) {
        Parent root = loader.getRoot();
        ((Stage) primaryStage.getScene().getWindow()).setScene(new Scene(root, stageWidth, stageHeight));
    }

    public static void goBack() {
        FXMLLoader loader = LoadXML.load("hello-view.fxml");
        setScene(loader);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        StartApplication.primaryStage = stage;               
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        primaryStage.setTitle("TCSsenger");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }

    public static void main(String[] args){

        try{
            // SqlCommunicate.connect("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");                                    
            SqlCommunicate.connect("jdbc:sqlserver://tcsenger.database.windows.net:1433;;databaseName=TCSenger ", "postgres", "Tcs12345");                        
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        launch();

        SqlCommunicate.disconnect();
    }
}
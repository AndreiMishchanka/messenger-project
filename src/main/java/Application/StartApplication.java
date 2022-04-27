package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import Utills.LoadXML;

public class StartApplication extends Application {
    public static Stage primaryStage;

    public static void setScene(FXMLLoader loader) {
        Parent root = loader.getRoot();
        ((Stage) primaryStage.getScene().getWindow()).setScene(new Scene(root, 600, 600));
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
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
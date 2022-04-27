package Utills;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

import Application.StartApplication;

public class LoadXML {
    public static FXMLLoader load(String path) {
        FXMLLoader loader = new FXMLLoader();
        System.err.println("-" + path + "-");
        System.err.println(StartApplication.class.getResource(path));
        loader.setLocation(StartApplication.class.getResource(path));        
        try {
            loader.load();
        } catch (IOException e) {            
            e.printStackTrace();            
        }
        return loader;
    }
}
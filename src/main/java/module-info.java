module Application.messengerproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Application to javafx.fxml;
    exports Application;

    opens Application.Scenes.ChatView to javafx.fxml;
    exports Application.Scenes.ChatView;
    
    opens Application.Scenes.LoginPage to javafx.fxml;
    exports Application.Scenes.LoginPage;

    opens Application.Scenes.Settings to javafx.fxml;
    exports Application.Scenes.Settings;

}
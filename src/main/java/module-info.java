module Application.messengerproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Application to javafx.fxml;
    exports Application;

    
    opens Application.Scenes.LoginPage to javafx.fxml;
    exports Application.Scenes.LoginPage;

}
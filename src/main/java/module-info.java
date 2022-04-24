module application.messengerproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens application.messengerproject to javafx.fxml;
    exports application.messengerproject;
}
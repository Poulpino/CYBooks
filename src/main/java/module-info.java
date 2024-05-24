module group.projetcybooks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    requires java.desktop;


    opens group.projetcybooks.client.scene to javafx.fxml;
    exports group.projetcybooks.client.scene;
}
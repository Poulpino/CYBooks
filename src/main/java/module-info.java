module group.projetcybooks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    requires java.desktop;


    opens group.projetcybooks to javafx.fxml;
    exports group.projetcybooks;
}
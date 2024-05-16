module group.projetcybooks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;


    opens group.projetcybooks to javafx.fxml;
    exports group.projetcybooks;
}
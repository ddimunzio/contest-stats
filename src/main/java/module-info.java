module org.lw5hr.contest {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    opens org.lw5hr.contest.model;

    exports org.lw5hr.contest.main;
    opens org.lw5hr.contest.main to javafx.fxml;
    exports org.lw5hr.contest.controllers;
    opens org.lw5hr.contest.controllers to javafx.fxml;
}
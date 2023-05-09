module org.lw5hr.contest {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires org.kordamp.ikonli.javafx;

  requires org.hibernate.orm.core;
  requires org.hibernate.commons.annotations;
  requires java.sql;

  requires  java.persistence;
  requires java.naming;
  requires java.xml;
  requires java.logging;

  exports org.lw5hr.contest.model;
  opens org.lw5hr.contest.model to org.hibernate.orm.core;
  exports org.lw5hr.contest.main;
  opens org.lw5hr.contest.main to javafx.fxml;
  exports org.lw5hr.contest.controllers;
  opens org.lw5hr.contest.controllers to javafx.fxml;

  exports org.lw5hr.contest.db;
  opens org.lw5hr.contest.db;

  exports org.lw5hr.contest.model.dxlog;
  opens org.lw5hr.contest.model.dxlog;

}
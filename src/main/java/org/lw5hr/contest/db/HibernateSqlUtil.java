package org.lw5hr.contest.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.lw5hr.contest.model.dxlog.QsoData;
import org.lw5hr.contest.model.dxlog.QsoDataCalc;

public class HibernateSqlUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
      if (sessionFactory == null) {
        Configuration configuration = new Configuration().addAnnotatedClass(QsoData.class)
                .addAnnotatedClass(QsoDataCalc.class)
                .setProperty("hibernate.dialect", "org.sqlite.hibernate.dialect.SQLiteDialect")
                .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
                .setProperty("hibernate.connection.url", "jdbc:sqlite:C:/Users/lw5hr/OneDrive/Documentos/contest/contest-test-stats/contestwpxtest.dxn")
                .setProperty("hibernate.current_session_context_class", "thread")
                .setProperty("hibernate.hbm2ddl.auto", "validate");
        ;
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
      }
      return sessionFactory;
    }

    public static void shutdown() {
      if (sessionFactory != null) {
        sessionFactory.close();
      }
    }

    public static Session getCurrentSession() {
      return getSessionFactory().getCurrentSession();
    }
  }







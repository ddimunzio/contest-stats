package org.lw5hr.contest.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.lw5hr.contest.model.dxlog.HeaderInfo;
import org.lw5hr.contest.model.dxlog.QsoData;
import org.lw5hr.contest.model.dxlog.QsoDataCalc;

public class HibernateSqlUtil {

    private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory(final String dbPath) {
    if (sessionFactory == null) {
      Configuration configuration = new Configuration()
              .addAnnotatedClass(QsoData.class)
              .addAnnotatedClass(HeaderInfo.class)
              .addAnnotatedClass(QsoDataCalc.class)
              .setProperty("hibernate.dialect", "org.sqlite.hibernate.dialect.SQLiteDialect")
              .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
              .setProperty("hibernate.connection.url", "jdbc:sqlite:" + dbPath)
              .setProperty("hibernate.current_session_context_class", "thread")
              .setProperty("hibernate.connection.autocommit", "false")
              .setProperty("hibernate.hbm2ddl.auto", "update")
              .setProperty("hibernate.show_sql", "true");
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

    public static Session getCurrentSession(final String dbPath) {
      return getSessionFactory(dbPath).getCurrentSession();
    }
  }







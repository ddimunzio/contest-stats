package org.lw5hr.contest.db;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.model.dxlog.QsoData;

import java.util.List;

public class QueryUtilSql {
  public void getAllLoggedQso() {
    Session sqlS = HibernateSqlUtil.getSessionFactory().getCurrentSession();
    sqlS.beginTransaction();
    Query querySql = sqlS.createQuery("from QsoData");
    QueryUtil q = new QueryUtil();
    List<QsoData> results = querySql.list();
    sqlS.getTransaction().commit();
    results.forEach(o -> q.saveNewQso(new Qso().convertQsoDataToQso(o)));
  }

}

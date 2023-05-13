package org.lw5hr.contest.db;

import org.hibernate.Session;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.model.dxlog.HeaderInfo;
import org.lw5hr.contest.model.dxlog.QsoData;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.lw5hr.contest.main.MainWindow.getQueryUtil;

public class QueryUtilSql {

  private static CriteriaBuilder getCriteriaBuilder(Session s) {
    return s.getCriteriaBuilder();
  }

  public void getAllLoggedQso(final String dbPath) {
    Session sqlS = HibernateSqlUtil.getSessionFactory(dbPath).getCurrentSession();
    try {
      sqlS.beginTransaction();
      CriteriaBuilder criteriaBuilder = getCriteriaBuilder(sqlS);
      CriteriaQuery<QsoData> qsoData = criteriaBuilder.createQuery(QsoData.class);
      Root<QsoData> root = qsoData.from(QsoData.class);
      qsoData.select(root);
      List<QsoData> result = sqlS.createQuery(qsoData).stream().toList();
      result.forEach(o -> getQueryUtil().saveNewQso(new Qso().convertQsoDataToQso(o)));
      sqlS.getTransaction().commit();
    } catch (Exception e) {
      sqlS.getTransaction().rollback();
    }
  }

  public String getContestName(final String dbPath) {
    String value = "";
    Session sqlS = HibernateSqlUtil.getSessionFactory(dbPath).getCurrentSession();
    try {
      sqlS.beginTransaction();
      CriteriaBuilder criteriaBuilder = getCriteriaBuilder(sqlS);
      CriteriaQuery<HeaderInfo> hi = criteriaBuilder.createQuery(HeaderInfo.class);
      Root<HeaderInfo> root = hi.from(HeaderInfo.class);
      hi.select(root);
      List<HeaderInfo> result = new ArrayList<>(sqlS.createQuery(hi).stream().toList());
      Optional<HeaderInfo> rs = result.stream()
              .filter(r -> r.getHdrname().equalsIgnoreCase(DatabaseConstants.DX_LOG_CONTEST_NAME)).findFirst();
      sqlS.getTransaction().commit();
      if (rs.isPresent()) {
        value = rs.get().getHdrvalue();
      } else {
        value = DatabaseConstants.NEW_CONTEST;
      }
    } catch (Exception e) {
      sqlS.getTransaction().rollback();
    }

    return value;
  }
}

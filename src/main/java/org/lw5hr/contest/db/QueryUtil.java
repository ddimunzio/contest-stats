package org.lw5hr.contest.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.model.Settings;

import javax.persistence.Entity;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class QueryUtil {
  public void saveEntity(Entity entity) {
    Session s = getSession();
    s.save(entity);
  }

  private Session getSession() {
    return HibernateUtil.getSessionFactory().getCurrentSession();
  }

  private static CriteriaBuilder getCriteriaBuilder(Session s) {
    return s.getCriteriaBuilder();
  }

  public List<Qso> getQsoByContest(Long contestId) {
    Session s = getSession();
    s.beginTransaction();
    CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
    CriteriaQuery<Qso> cq = criteriaBuilder.createQuery(Qso.class);
    Root<Qso> root = cq.from(Qso.class);
    cq.select(root);
    TypedQuery<Qso> allQuery = s.createQuery(cq);
    List<Qso> res = allQuery.getResultList().stream().filter(q -> Objects.equals(q.getContest().getId(), contestId))
            .sorted(Comparator.comparing(Qso::getDate)).toList();
    s.getTransaction().commit();

    return res;
  }

  public Boolean contestExist(String name) {
    Session s = getSession();
    s.beginTransaction();
    CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
    CriteriaQuery<Contest> contest = criteriaBuilder.createQuery(Contest.class);
    Root<Contest> root = contest.from(Contest.class);
    contest.select(root);
    TypedQuery<Contest> allQuery = s.createQuery(contest);
    boolean res = allQuery.getResultList().stream().anyMatch(c -> c.getContestName().equalsIgnoreCase(name));
    s.getTransaction().commit();
    return res;
  }

  public Contest getContest(final Long id) {
    Session s = getSession();
    s.beginTransaction();
    CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
    CriteriaQuery<Contest> contest = criteriaBuilder.createQuery(Contest.class);
    Root<Contest> root = contest.from(Contest.class);
    contest.select(root);
    TypedQuery<Contest> allQuery = s.createQuery(contest);
    Optional<Contest> res = allQuery.getResultList().stream().filter(co -> co.getId().equals(id)).findFirst();
    s.getTransaction().commit();
    return res.orElseGet(Contest::new);
  }

  public ObservableList<Contest> getContestList() {
    Session s = getSession();
    s.beginTransaction();
    ObservableList<Contest> result = FXCollections.observableArrayList();
    CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
    CriteriaQuery<Contest> contest = criteriaBuilder.createQuery(Contest.class);
    Root<Contest> root = contest.from(Contest.class);
    contest.select(root);
    TypedQuery<Contest> allQuery = s.createQuery(contest);
    result.addAll(allQuery.getResultList());
    s.getTransaction().commit();
    return result;
  }

  public void updateContest(final Contest contest) {
    Session s = getSession();
    s.beginTransaction();
    s.saveOrUpdate(contest);
    s.getTransaction().commit();
  }

  public void deleteContest(final Contest contest) {
    Session s = HibernateUtil.getSessionFactory().getCurrentSession();
    s.beginTransaction();
    s.delete(contest);
    s.getTransaction().commit();
  }

  public Locale getDefaultLocale() {
    Locale loc;
    Session s = getSession();
    s.beginTransaction();
    CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
    CriteriaQuery<Settings> settings = criteriaBuilder.createQuery(Settings.class);
    Root<Settings> root = settings.from(Settings.class);
    settings.select(root);
    TypedQuery<Settings> allQuery = s.createQuery(settings);
    Optional<Settings> result = allQuery.getResultList().stream()
            .filter(se -> se.getSettingName().equalsIgnoreCase(DatabaseConstants.DEFAULT_LANG)).findFirst();
    s.getTransaction().commit();
    if (result.isPresent()) {
      String[] value = result.get().getSettingValue().split("_");
      loc = Locale.of(value[0], value[1]);
    } else {
      loc = Locale.of(DatabaseConstants.DEFAULT_LOCALE_LANGUAGE, DatabaseConstants.DEFAULT_LOCALE_COUNTRY);
    }
    return loc;
  }

  public Long getSelectedContest() {
    Session s = getSession();
    s.beginTransaction();
    CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
    CriteriaQuery<Settings> settings = criteriaBuilder.createQuery(Settings.class);
    Root<Settings> root = settings.from(Settings.class);
    settings.select(root);
    TypedQuery<Settings> allQuery = s.createQuery(settings);
    Optional<Settings> result = allQuery.getResultList().stream()
            .filter(se -> se.getSettingName().equalsIgnoreCase(DatabaseConstants.CURRENT_CONTEST)).findFirst();
    s.getTransaction().commit();
    if (result.isPresent() && result.get().getSettingValue() != null) {
      return result.map(x -> Long.valueOf(x.getSettingValue())).orElse(null);
    } else {
      return null;
    }

  }

  public void updateSetting(String settingName, String settingValue) {
    Session s = getSession();
    Transaction transaction = s.beginTransaction();
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaUpdate<Settings> criteriaUpdate = cb.createCriteriaUpdate(Settings.class);
    Root<Settings> root = criteriaUpdate.from(Settings.class);
    criteriaUpdate.set("settingValue", settingValue);
    criteriaUpdate.where(cb.equal(root.get("settingName"), settingName));
    s.createQuery(criteriaUpdate).executeUpdate();
    transaction.commit();
  }

  public void saveNewQso(final Qso qso) {
    qso.setContest(getContest(9L));
    Session s = getSession();
    s.beginTransaction();
    CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
    CriteriaQuery<Qso> localQsos = criteriaBuilder.createQuery(Qso.class);
    Root<Qso> root = localQsos.from(Qso.class);
    localQsos.select(root);
    TypedQuery<Qso> allQuery = s.createQuery(localQsos);
    Optional<Qso> localQso = allQuery.getResultList().stream()
            .filter(q -> q.getDate().equals(qso.getDate()))
            .filter(q -> q.getTime().equals(qso.getTime())).findFirst();
    if (localQso.isEmpty()) {
      s.saveOrUpdate(qso);
    } else {
      System.out.println(qso.getCall() + " Already on database");
    }
    s.getTransaction().commit();
  }

  public Boolean settingsExist(String name) {
    Session s = getSession();
    s.beginTransaction();
    CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
    CriteriaQuery<Settings> setting = criteriaBuilder.createQuery(Settings.class);
    Root<Settings> root = setting.from(Settings.class);
    setting.select(root);
    TypedQuery<Settings> allQuery = s.createQuery(setting);
    boolean res = allQuery.getResultList().stream().anyMatch(c -> c.getSettingName().equalsIgnoreCase(name));
    s.getTransaction().commit();
    return res;
  }
  public void initSetting(final String settingName, final String settingValue) {
    Settings settings = new Settings();
    settings.setSettingName(settingName);
    settings.setSettingValue(settingValue);
    Session s = getSession();
    s.beginTransaction();
    s.saveOrUpdate(settings);
    s.getTransaction().commit();
  }

}

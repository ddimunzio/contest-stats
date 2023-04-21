package org.lw5hr.contest.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.model.Settings;

import javax.persistence.Entity;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

public class QueryUtil {
    public void saveEntity (Entity entity) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.save(entity);
    }

    private Session getSession () {
        return HibernateUtil.getSessionFactory().openSession();
    }

    private static CriteriaBuilder getCriteriaBuilder(Session s) {
        return s.getCriteriaBuilder();
    }
    public List<Qso> getQsoByContest(Long contestId) {
        Session s = getSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
        CriteriaQuery<Qso> cq = criteriaBuilder.createQuery(Qso.class);
        Root<Qso> root = cq.from(Qso.class);
        cq.select(root);
        TypedQuery<Qso> allQuery = s.createQuery(cq);
        return allQuery.getResultList().stream().filter(q -> q.getContest().getId() == contestId)
                .sorted(Comparator.comparing(Qso::getDate)).toList();
    }

    public Boolean contestExist(String name) {
        Session s = getSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
        CriteriaQuery<Contest> contest = criteriaBuilder.createQuery(Contest.class);
        Root<Contest> root = contest.from(Contest.class);
        contest.select(root);
        TypedQuery<Contest> allQuery = s.createQuery(contest);
        return allQuery.getResultList().stream().anyMatch(c -> c.getContestName().equalsIgnoreCase(name));
    }

    public ObservableList<Contest> getContestList() {
        Session s = getSession();
        ObservableList<Contest> result = FXCollections.observableArrayList();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
        CriteriaQuery<Contest> contest = criteriaBuilder.createQuery(Contest.class);
        Root<Contest> root = contest.from(Contest.class);
        contest.select(root);
        TypedQuery<Contest> allQuery = s.createQuery(contest);
        result.addAll(allQuery.getResultList());
        return result;
    }

    public Locale getDefaultLocale() {
        Locale loc;
        Session s = getSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(s);
        CriteriaQuery<Settings> settings = criteriaBuilder.createQuery(Settings.class);
        Root<Settings> root = settings.from(Settings.class);
        settings.select(root);
        TypedQuery<Settings> allQuery = s.createQuery(settings);
        Optional<Settings> result = allQuery.getResultList().stream()
                .filter(se -> se.getSettingName().equalsIgnoreCase(DatabaseConstants.DEFAULT_LANG)).findFirst();
        if (result.isPresent()) {
            String[] value = result.get().getSettingValue().split("_");
            loc = new Locale(value[0], value[1]);
        } else {
            loc = new Locale(DatabaseConstants.DEFAULT_LOCALE_LANGUAGE, DatabaseConstants.DEFAULT_LOCALE_COUNTRY);
        }
        return loc;
    }

    public void updateSetting(String settingName, String settingValue) {
        Session s = getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaUpdate<Settings> criteriaUpdate = cb.createCriteriaUpdate(Settings.class);
        Root<Settings> root = criteriaUpdate.from(Settings.class);
        criteriaUpdate.set("settingValue", settingValue);
        criteriaUpdate.where(cb.equal(root.get("settingName"), settingName));

        Transaction transaction = s.beginTransaction();
        s.createQuery(criteriaUpdate).executeUpdate();
        transaction.commit();
    }
}

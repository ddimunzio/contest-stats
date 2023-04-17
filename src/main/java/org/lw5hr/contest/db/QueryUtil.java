package org.lw5hr.contest.db;

import org.hibernate.Session;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.model.Settings;

import javax.persistence.Entity;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class QueryUtil {
    public void saveEntity (Entity entity) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.save(entity);
    }

    public List<Qso> getQsoByContest(Long contestId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
        CriteriaQuery<Qso> cq = criteriaBuilder.createQuery(Qso.class);
        Root<Qso> root = cq.from(Qso.class);
        cq.select(root);
        TypedQuery<Qso> allQuery = s.createQuery(cq);
        return allQuery.getResultList().stream().filter(q -> q.getContest().getId() == contestId)
                .sorted(Comparator.comparing(Qso::getDate)).toList();
    }

    public Boolean contestExist(String name) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
        CriteriaQuery<Contest> contest = criteriaBuilder.createQuery(Contest.class);
        Root<Contest> root = contest.from(Contest.class);
        contest.select(root);
        TypedQuery<Contest> allQuery = s.createQuery(contest);
        return allQuery.getResultList().stream().anyMatch(c -> c.getContestName().equalsIgnoreCase(name));
    }

    public Locale getDefaultLocale() {
        Locale loc;
        Session s = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
        CriteriaQuery<Settings> settings = criteriaBuilder.createQuery(Settings.class);
        Root<Settings> root = settings.from(Settings.class);
        settings.select(root);
        TypedQuery<Settings> allQuery = s.createQuery(settings);
        Optional<Settings> result = allQuery.getResultList().stream()
                .filter(se -> se.getSettingName().equalsIgnoreCase("default_lang")).findFirst();
        if (result.isPresent()) {
            String[] value = result.get().getSettingValue().split("_");
            loc = new Locale(value[0], value[1]);
        } else {
            loc = new Locale("en", "US");
        }
        return loc;
    }
}

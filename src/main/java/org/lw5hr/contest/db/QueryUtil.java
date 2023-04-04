package org.lw5hr.contest.db;

import org.hibernate.Session;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.model.Qso;

import javax.persistence.Entity;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.Comparator;
import java.util.List;

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
}

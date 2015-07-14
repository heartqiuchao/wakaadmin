package edu.cqu.wakaadmin.security.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import edu.cqu.wakaadmin.dao.util.TypedQueryBuilder;
import edu.cqu.wakaadmin.security.domain.AdminUser;

/**
 * @author hui
 */
@Repository("wakaAdminUserDao")
public class AdminUserDaoImpl implements AdminUserDao {

    @PersistenceContext(unitName = "wakaAdminPU")
    protected EntityManager em;

    @Override
    public void deleteAdminUser(AdminUser user) {
        if (!em.contains(user)) {
            user = em.find(AdminUser.class, user.getId());
        }
        em.remove(user);
    }

    @Override
    public AdminUser readAdminUserById(Long id) {
        return em.find(AdminUser.class, id);
    }
    
    @Override
    public List<AdminUser> readAdminUsersByIds(Set<Long> ids) {
        TypedQueryBuilder<AdminUser> tqb = new TypedQueryBuilder<AdminUser>(AdminUser.class, "au");

        if (ids != null && !ids.isEmpty()) {
            tqb.addRestriction("au.id", "in", ids);
        } 

        TypedQuery<AdminUser> query = tqb.toQuery(em);
        return query.getResultList();
    }

    @Override
    public AdminUser saveAdminUser(AdminUser user) {
        if (em.contains(user) || user.getId() != null) {
            return em.merge(user);
        } else {
            em.persist(user);
            return user;
        }
    }

    @Override
    public AdminUser readAdminUserByAccount(String account) {
        TypedQuery<AdminUser> query = em.createNamedQuery("READ_ADMIN_USER_BY_ACCOUNT", AdminUser.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter("account", account);
        List<AdminUser> users = query.getResultList();
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public List<AdminUser> readAllAdminUsers() {
        TypedQuery<AdminUser> query = em.createNamedQuery("READ_ALL_ADMIN_USERS", AdminUser.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

}

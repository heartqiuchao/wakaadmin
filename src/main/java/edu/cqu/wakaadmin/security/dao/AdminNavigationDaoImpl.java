package edu.cqu.wakaadmin.security.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import edu.cqu.wakaadmin.security.domain.AdminFunction;
import edu.cqu.wakaadmin.security.domain.AdminModule;

/**
 * @author Hui
 */
@Repository("wakaAdminNavigationDao")
public class AdminNavigationDaoImpl implements AdminNavigationDao {

    @PersistenceContext(unitName = "wakaAdminPU")
    protected EntityManager em;

    @Override
    public List<AdminModule> readAllAdminModules() {
        TypedQuery<AdminModule> query = em.createNamedQuery("READ_ALL_ADMIN_MODULES", AdminModule.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @Override
    public List<AdminFunction> readAllAdminFunctions() {
        TypedQuery<AdminFunction> query = em.createNamedQuery("READ_ALL_ADMIN_FUNCTIONS", AdminFunction.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }
    
    @Override
    public AdminFunction readAdminFunctionByKey(String functionKey) {
        TypedQuery<AdminFunction> query = em.createNamedQuery("READ_ADMIN_FUNCTION_BY_KEY", AdminFunction.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter("functionKey", functionKey);
        
        AdminFunction adminFunction = null;
        try {
            adminFunction = (AdminFunction) query.getSingleResult();
        } catch (NoResultException e) {
            //
        }
        return adminFunction;
    }
    
    @Override
    public AdminFunction readAdminFunctionByURI(String uri) {
        TypedQuery<AdminFunction> query = em.createNamedQuery("READ_ADMIN_FUNCTION_BY_URI", AdminFunction.class);
        query.setParameter("uri", uri);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        
        AdminFunction adminFunction = null;
        try {
            adminFunction = (AdminFunction) query.getSingleResult();
        } catch (NoResultException e) {
            //
        }
        return adminFunction;
    }

    @Override
    public AdminFunction readAdminFunctionForEntity(String entityName) {
        TypedQuery<AdminFunction> query = em.createNamedQuery("READ_ADMIN_FUNCTION_FOR_ENTITY", AdminFunction.class);
        query.setParameter("entityName", entityName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        
        List<AdminFunction> result = query.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return query.getResultList().get(0);
    }

}

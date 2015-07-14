package edu.cqu.wakaadmin.security.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import edu.cqu.wakaadmin.security.domain.AdminPermission;
import edu.cqu.wakaadmin.security.domain.AdminUser;
import edu.cqu.wakaadmin.security.domain.PermissionType;
import edu.cqu.wakaadmin.security.service.AdminSecurityService;

/**
 * @author hui
 */
@Repository("wakaAdminPermissionDao")
public class AdminPermissionDaoImpl implements AdminPermissionDao {
    
    @PersistenceContext(unitName = "wakaAdminPU")
    protected EntityManager em;

    @Override
    public void deleteAdminPermission(AdminPermission permission) {
        if (!em.contains(permission)) {
            permission = readAdminPermissionById(permission.getId());
        }
        em.remove(permission);
    }

    @Override
    public AdminPermission readAdminPermissionById(Long id) {
        return (AdminPermission) em.find(AdminPermission.class, id);
    }
    
    @Override
    public AdminPermission readAdminPermissionByName(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AdminPermission> criteria = builder.createQuery(AdminPermission.class);
        Root<AdminPermission> adminPerm = criteria.from(AdminPermission.class);
        criteria.select(adminPerm);

        List<Predicate> restrictions = new ArrayList<Predicate>();
        restrictions.add(builder.equal(adminPerm.get("name"), name));

        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
        TypedQuery<AdminPermission> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List<AdminPermission> results = query.getResultList();
        if (results == null || results.size() == 0) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public AdminPermission saveAdminPermission(AdminPermission permission) {
        return em.merge(permission);
    }

    @Override
    public List<AdminPermission> readAllAdminPermissions() {
        TypedQuery<AdminPermission> query = em.createNamedQuery("READ_ALL_ADMIN_PERMISSIONS", AdminPermission.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List<AdminPermission> permissions = query.getResultList();
        return permissions;
    }

    @Override
    public boolean isUserRestrictedForOperationOnEntity(AdminUser adminUser, PermissionType permissionType, String entityName) {
        Query query = em.createNamedQuery("COUNT_PERMISSIONS_FOR_USER_BY_TYPE_AND_ENTITY");
        query.setParameter("adminUser", adminUser);
        query.setParameter("type", permissionType.getTypeName());
        query.setParameter("entityName", entityName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        
        Long count = (Long) query.getSingleResult();
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean doesDefaultOperationsExistForEntity(String entityName) {
        Query query = em.createNamedQuery("COUNT_BY_PERMISSION_AND_ENTITY");
        query.setParameter("permissionNames", Arrays.asList(AdminSecurityService.DEFAULT_PERMISSIONS));
        query.setParameter("entityName", entityName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        
        Long count = (Long) query.getSingleResult();
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean doesOperationExistForEntity(PermissionType permissionType, String entityName) {
            Query query = em.createNamedQuery("COUNT_PERMISSIONS_BY_TYPE_AND_ENTITY");
            query.setParameter("type", permissionType.getTypeName());
            query.setParameter("entityName", entityName);
            query.setHint(QueryHints.HINT_CACHEABLE, true);

            Long count = (Long) query.getSingleResult();
            if (count > 0) {
                return true;
            }
            return false;
    }
    
}

package edu.cqu.wakaadmin.security.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import edu.cqu.wakaadmin.security.domain.AdminRole;

/**
 * @author hui
 */
@Repository("wakaAdminRoleDao")
public class AdminRoleDaoImpl implements AdminRoleDao {

    @PersistenceContext(unitName = "wakaAdminPU")
    protected EntityManager em;

    public void deleteAdminRole(AdminRole role) {
        if (!em.contains(role)) {
            role = readAdminRoleById(role.getId());
        }
        em.remove(role);
    }

    public AdminRole readAdminRoleById(Long id) {
        return (AdminRole) em.find(AdminRole.class, id);
    }

    public AdminRole saveAdminRole(AdminRole role) {
        return em.merge(role);
    }

    public List<AdminRole> readAllAdminRoles() {
        TypedQuery<AdminRole> query = em.createNamedQuery("READ_ALL_ADMIN_ROLES", AdminRole.class);
        List<AdminRole> roles = query.getResultList();
        return roles;
    }

}

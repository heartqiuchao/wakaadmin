package edu.cqu.wakaadmin.security.dao;

import java.util.List;

import edu.cqu.wakaadmin.security.domain.AdminRole;

/**
 * @author hui
 */
public interface AdminRoleDao {
    
    /**
     * 读取所有角色
     */
    public List<AdminRole> readAllAdminRoles();
    
    /**
     * 根据id读取角色
     */
    public AdminRole readAdminRoleById(Long id);
    
    /**
     * 保存角色
     */
    public AdminRole saveAdminRole(AdminRole role);
    
    /**
     * 删除角色
     */
    public void deleteAdminRole(AdminRole role);
    
}

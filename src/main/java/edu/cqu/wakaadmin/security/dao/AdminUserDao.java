package edu.cqu.wakaadmin.security.dao;

import java.util.List;
import java.util.Set;

import edu.cqu.wakaadmin.security.domain.AdminUser;

/**
 * @author hui
 */
public interface AdminUserDao {
    
    /**
     * 读取所有用户
     */
    List<AdminUser> readAllAdminUsers();
    
    /**
     * 根据id读取用户
     */
    AdminUser readAdminUserById(Long id);
    
    /**
     * 读取ids读取用户集合
     */
    List<AdminUser> readAdminUsersByIds(Set<Long> ids);
    
    /**
     * 根据用户名读取用户
     */
    AdminUser readAdminUserByAccount(String account);
    
    /**
     * 保存用户
     */
    AdminUser saveAdminUser(AdminUser user);
    
    /**
     * 删除用户
     */
    void deleteAdminUser(AdminUser user);
    
}

package edu.cqu.wakaadmin.security.service;

import java.util.List;

import edu.cqu.wakaadmin.security.domain.AdminPermission;
import edu.cqu.wakaadmin.security.domain.AdminRole;
import edu.cqu.wakaadmin.security.domain.AdminUser;
import edu.cqu.wakaadmin.security.domain.PermissionType;
import edu.cqu.wakaadmin.security.util.PasswordChange;

/**
 * @author hui
 */
public interface AdminSecurityService {

    public static final String[] DEFAULT_PERMISSIONS = { "PERMISSION_OTHER_DEFAULT" };
    
    /**
     * 读取所有用户
     */
    List<AdminUser> readAllAdminUsers();
    
    /**
     * 根据id读取用户
     */
    AdminUser readAdminUserById(Long id);
    
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
    
    /**
     * 变更密码
     */
    AdminUser changePassword(PasswordChange passwordChange);
    
    /**
     * 变更密码
     */
    GenericResponse changePassword(String username, String oldPassword, String password, String confirmPassword);

    /**
     * 读取所有角色
     */
    List<AdminRole> readAllAdminRoles();
    
    /**
     * 根据id读取角色
     */
    AdminRole readAdminRoleById(Long id);
    
    /**
     * 保存角色
     */
    AdminRole saveAdminRole(AdminRole role);
    
    /**
     * 删除角色
     */
    void deleteAdminRole(AdminRole role);

    /**
     * 读取所有权限项
     */
    List<AdminPermission> readAllAdminPermissions();
    
    /**
     * 根据id读取权限项
     */
    AdminPermission readAdminPermissionById(Long id);
    
    /**
     * 保存权限项
     */
    AdminPermission saveAdminPermission(AdminPermission permission);
    
    /**
     * 删除权限项
     */
    void deleteAdminPermission(AdminPermission permission);
    
    /**
     * 实体是否关联了某项权限（如读该实体的权限，删该实体的权限）
     */
    public boolean doesOperationExistForEntity(PermissionType permissionType, String entityName);

    /**
     * 判断在entityName所指实体上，adminUser的permissionType所指操作（增/删/改/查）是否受限
     */
    public boolean isUserRestrictedForOperationOnEntity(AdminUser adminUser, PermissionType permissionType, String entityName);

}

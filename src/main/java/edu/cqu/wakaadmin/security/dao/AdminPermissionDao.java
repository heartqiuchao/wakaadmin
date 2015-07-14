package edu.cqu.wakaadmin.security.dao;

import java.util.List;

import edu.cqu.wakaadmin.security.domain.AdminPermission;
import edu.cqu.wakaadmin.security.domain.AdminUser;
import edu.cqu.wakaadmin.security.domain.PermissionType;

/**
 * @author hui
 */
public interface AdminPermissionDao {

    /**
     * 读取所有权限项
     */
    public List<AdminPermission> readAllAdminPermissions();

    /**
     * 保存权限项
     */
    public AdminPermission saveAdminPermission(AdminPermission permission);

    /**
     * 根据名字读取权限项
     */
    public AdminPermission readAdminPermissionByName(String name);

    /**
     * 根据id读取权限项
     */
    public AdminPermission readAdminPermissionById(Long id);

    /**
     * 删除权限项
     */
    public void deleteAdminPermission(AdminPermission permission);
    
    /**
     * 实体是否关联了某项权限（如 读该实体的权限，删该实体的权限）
     */
    public boolean doesOperationExistForEntity(PermissionType permissionType, String entityName);

    /**
     * 实体是否关联了某项权限（如读该实体的权限，删该实体的权限）
     */
    public boolean doesDefaultOperationsExistForEntity(String entityName);

    /**
     * 判断在entityName所指实体上，adminUser的permissionType所指操作（增/删/改/查）是否受限
     */
    public boolean isUserRestrictedForOperationOnEntity(AdminUser adminUser, PermissionType permissionType, String entityName);

}

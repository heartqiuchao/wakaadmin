package edu.cqu.wakaadmin.security.dao;


import java.util.List;

import edu.cqu.wakaadmin.security.domain.AdminFunction;
import edu.cqu.wakaadmin.security.domain.AdminModule;

/**
 * @author hui
 */
public interface AdminNavigationDao {
    
    /**
     * 读取所有管理模块
     */
    public List<AdminModule> readAllAdminModules();

    /**
     * 读取所有功能点
     */
    public List<AdminFunction> readAllAdminFunctions();
    
    /**
     * 根据实体类的实体名，找到与之相关的管理模块。如果同时有多个管理模块
     * 与该实体类相关，则返回第一个模块。如果没有找到，则返回null
     */
    public AdminFunction readAdminFunctionForEntity(String entityName);

    /**
     * 根据uri找到对应的功能点
     */
    public AdminFunction readAdminFunctionByURI(String uri);

    /**
     * 根据function key找到对应的功能点
     */
    public AdminFunction readAdminFunctionByKey(String functionKey);

}

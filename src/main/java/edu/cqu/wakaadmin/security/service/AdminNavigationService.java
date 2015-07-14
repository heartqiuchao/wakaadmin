package edu.cqu.wakaadmin.security.service;

import java.util.List;

import edu.cqu.wakaadmin.security.domain.AdminFunction;
import edu.cqu.wakaadmin.security.domain.AdminMenu;
import edu.cqu.wakaadmin.security.domain.AdminModule;
import edu.cqu.wakaadmin.security.domain.AdminUser;

public interface AdminNavigationService {

    /**
     * adminUser所拥有的功能菜单
     */
    public AdminMenu buildMenu(AdminUser adminUser);

    /**
     * 指定的function对用户adminUser是否可见
     */
    public boolean canUserViewFunction(AdminUser adminUser, AdminFunction function);

    /**
     * 指定的module对用户adminUser是否可见，根据module下
     * 是否有adminUser可见的function来做判断
     */
    public boolean canUserViewModule(AdminUser adminUser, AdminModule module);

    /**
     * 根据uri获取对应的function
     */
    public AdminFunction findAdminFunctionByURI(String uri);

    /**
     * 根据key获取对应的funciton
     */
    public AdminFunction findAdminFunctionByKey(String key);

    /**
     * 通过实体名，找到其对应的function
     */
    public AdminFunction findAdminFunctionForEntity(String className);
    
    /**
     * 通过实体的class对象，找到其对应的function
     */
    public AdminFunction findAdminFunctionForEntity(Class<?> clazz);

    /**
     * 找出系统所有的function
     */
    public List<AdminFunction> findAllAdminFunctions();

}

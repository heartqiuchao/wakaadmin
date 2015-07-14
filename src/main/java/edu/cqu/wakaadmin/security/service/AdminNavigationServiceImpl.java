package edu.cqu.wakaadmin.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import edu.cqu.wakaadmin.security.dao.AdminNavigationDao;
import edu.cqu.wakaadmin.security.domain.AdminFunction;
import edu.cqu.wakaadmin.security.domain.AdminMenu;
import edu.cqu.wakaadmin.security.domain.AdminModule;
import edu.cqu.wakaadmin.security.domain.AdminPermission;
import edu.cqu.wakaadmin.security.domain.AdminRole;
import edu.cqu.wakaadmin.security.domain.AdminUser;

@Service("wakaAdminNavigationService")
public class AdminNavigationServiceImpl implements AdminNavigationService {

    @Resource(name = "wakaAdminNavigationDao")
    protected AdminNavigationDao adminNavigationDao;

    @Override
    public AdminMenu buildMenu(AdminUser adminUser) {
        AdminMenu adminMenu = new AdminMenu();
        List<AdminModule> modules = adminNavigationDao.readAllAdminModules();
        doBuildMenu(adminMenu, adminUser, modules);
        return adminMenu;
    }

    protected void doBuildMenu(AdminMenu adminMenu, AdminUser adminUser, List<AdminModule> modules) {
        for (AdminModule module : modules) {
            List<AdminFunction> funtions = findFunctionsForUser(adminUser, module);
            if (funtions != null && funtions.size() > 0) {
                AdminModule dto = module.copy();
                adminMenu.getAdminModules().add(dto);
                dto.setFunctions(funtions);
            }
        }

        Collections.sort(adminMenu.getAdminModules(), new Comparator<AdminModule>() {

                @Override
                public int compare(AdminModule module, AdminModule module2) {
                    if (module.getDisplayOrder() != null) {
                        if (module2.getDisplayOrder() != null) {
                            return module.getDisplayOrder().compareTo(module2.getDisplayOrder());
                        }
                        else
                            return -1;
                    } else if (module2.getDisplayOrder() != null) {
                        return 1;
                    }
    
                    return module.getId().compareTo(module2.getId());
                }

        } );
    }

    //找到module中对adminUser可见的function
    protected List<AdminFunction> findFunctionsForUser(AdminUser adminUser, AdminModule module) {
        List<AdminFunction> functions = new ArrayList<AdminFunction>();
        for (AdminFunction function : module.getFunctions()) {
            if (canUserViewFunction(adminUser, function)) {
                functions.add(function);
            }
        }

        Collections.sort(functions, FUNCTION_COMPARATOR);
        return functions;
    }
    
    @Override
    public boolean canUserViewFunction(AdminUser adminUser, AdminFunction function) {
        boolean result = false;
        List<AdminPermission> funcPermissions = function.getPermissions();
        checkAuth: {
            if (!CollectionUtils.isEmpty(adminUser.getAllRoles())) {
                for (AdminRole role : adminUser.getAllRoles()) {
                    for (AdminPermission permission : role.getAllPermissions()){
                        if (checkPermissions(funcPermissions, permission)) {
                            result = true;
                            break checkAuth;
                        }
                    }
                }
            }
            
            //如果function以default permission发布，则对任何人都可见（所有用户默认都具有Default Permssion）
            for (String defaultPermission : AdminSecurityService.DEFAULT_PERMISSIONS) {
                for (AdminPermission funcPermission : funcPermissions) {
                    if (funcPermission.getName().equals(defaultPermission)) {
                        result = true;
                        break checkAuth;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean canUserViewModule(AdminUser adminUser, AdminModule module) {
        List<AdminFunction> functions = module.getFunctions();
        if (functions != null && !functions.isEmpty()) {
            for (AdminFunction f : functions) {
                if (canUserViewFunction(adminUser, f)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public AdminFunction findAdminFunctionByURI(String uri) {
        return adminNavigationDao.readAdminFunctionByURI(uri);
    }
    
    @Override
    public AdminFunction findAdminFunctionForEntity(String entityName) {
          return adminNavigationDao.readAdminFunctionForEntity(entityName);
    }
    
    @Override
    public AdminFunction findAdminFunctionForEntity(Class<?> clazz) {
        return findAdminFunctionForEntity(clazz.getSimpleName()); 
    }

    @Override
    public AdminFunction findAdminFunctionByKey(String key) {
        return adminNavigationDao.readAdminFunctionByKey(key);
    }

    @Override
    public List<AdminFunction> findAllAdminFunctions() {
        List<AdminFunction> functions = adminNavigationDao.readAllAdminFunctions();
        Collections.sort(functions, FUNCTION_COMPARATOR);
        return functions;
    }

    protected boolean checkPermissions(List<AdminPermission> funcPermissions, AdminPermission permission) {
        if (funcPermissions != null) {
            if (funcPermissions.contains(permission)){
                return true;
            }
            
            //permission为超级权限项（为方便而设计，再想想...  //2015-3-25） 
//            for (AdminPermission funcPermission : funcPermissions) {
//                if (permission.getName().equals(parseForSuperPermission(funcPermission.getName()))) {
//                    return true;
//                }
//            }
        }
        return false;
    }

    protected String parseForSuperPermission(String currentPermission) {
        String[] pieces = currentPermission.split("_");
        StringBuilder builder = new StringBuilder(50);
        builder.append(pieces[0]);
        builder.append("_ALL_");
        for (int j = 2; j<pieces.length; j++) {
            builder.append(pieces[j]);
            if (j < pieces.length - 1) {
                builder.append('_');
            }
        }
        return builder.toString();
    }

    private static Comparator<AdminFunction> FUNCTION_COMPARATOR = new Comparator<AdminFunction>() {

        @Override
        public int compare(AdminFunction section, AdminFunction section2) {
            if (section.getDisplayOrder() != null) {
                if (section2.getDisplayOrder() != null) {
                    return section.getDisplayOrder().compareTo(section2.getDisplayOrder());
                }
                else
                    return -1;
            } else if (section2.getDisplayOrder() != null) {
                return 1;
            }

            return section.getId().compareTo(section2.getId());
        }

    };

}

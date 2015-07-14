package edu.cqu.wakaadmin.security.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.cqu.wakaadmin.security.dao.AdminPermissionDao;
import edu.cqu.wakaadmin.security.dao.AdminRoleDao;
import edu.cqu.wakaadmin.security.dao.AdminUserDao;
import edu.cqu.wakaadmin.security.domain.AdminPermission;
import edu.cqu.wakaadmin.security.domain.AdminRole;
import edu.cqu.wakaadmin.security.domain.AdminUser;
import edu.cqu.wakaadmin.security.domain.PermissionType;
import edu.cqu.wakaadmin.security.util.PasswordChange;

/**
 * @author Hui
 */
@Service("wakaAdminSecurityService")
public class AdminSecurityServiceImpl implements AdminSecurityService {

    @Resource(name = "wakaAdminRoleDao")
    protected AdminRoleDao adminRoleDao;

    @Resource(name = "wakaAdminUserDao")
    protected AdminUserDao adminUserDao;
    
    @Resource(name = "wakaAdminPermissionDao")
    protected AdminPermissionDao adminPermissionDao;
    
    @Resource(name="wakaPasswordEncoder")
    protected PasswordEncoder passwordEncoder;

    @Override
    @Transactional("wakaTransactionManager")
    public void deleteAdminPermission(AdminPermission permission) {
        adminPermissionDao.deleteAdminPermission(permission);
    }

    @Override
    @Transactional("wakaTransactionManager")
    public void deleteAdminRole(AdminRole role) {
        adminRoleDao.deleteAdminRole(role);
    }

    @Override
    @Transactional("wakaTransactionManager")
    public void deleteAdminUser(AdminUser user) {
        adminUserDao.deleteAdminUser(user);
    }

    @Override
    public AdminPermission readAdminPermissionById(Long id) {
        return adminPermissionDao.readAdminPermissionById(id);
    }

    @Override
    public AdminRole readAdminRoleById(Long id) {
        return adminRoleDao.readAdminRoleById(id);
    }

    @Override
    public AdminUser readAdminUserById(Long id) {
        return adminUserDao.readAdminUserById(id);
    }

    @Override
    @Transactional("wakaTransactionManager")
    public AdminPermission saveAdminPermission(AdminPermission permission) {
        return adminPermissionDao.saveAdminPermission(permission);
    }

    @Override
    @Transactional("wakaTransactionManager")
    public AdminRole saveAdminRole(AdminRole role) {
        return adminRoleDao.saveAdminRole(role);
    }

    @Override
    @Transactional("wakaTransactionManager")
    public AdminUser saveAdminUser(AdminUser user) {
        boolean encodePasswordNeeded = false;
        String unencodedPassword = user.getUnencodedPassword();

        if (user.getUnencodedPassword() != null) {
            encodePasswordNeeded = true;
            user.setPassword(unencodedPassword);
        }

        // 如果还没密码, 默认会自动生成一个随机密码 .
        if (user.getPassword() == null) {
            user.setPassword(generateRandomPassword());
        }

        AdminUser returnUser = adminUserDao.saveAdminUser(user);

        if (encodePasswordNeeded) {
            returnUser.setPassword( passwordEncoder.encode(unencodedPassword) );
        }

        return adminUserDao.saveAdminUser(returnUser);
    }

    protected String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(16);
    }

    @Override
    public boolean isUserRestrictedForOperationOnEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        boolean response = adminPermissionDao.isUserRestrictedForOperationOnEntity(adminUser, permissionType, ceilingEntityFullyQualifiedName);
        if (!response) {
            response = adminPermissionDao.doesDefaultOperationsExistForEntity(ceilingEntityFullyQualifiedName);
        }
        return response;
    }

    @Override
    public boolean doesOperationExistForEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        return adminPermissionDao.doesOperationExistForEntity(permissionType, ceilingEntityFullyQualifiedName);
    }

    @Override
    public AdminUser readAdminUserByAccount(String account) {
        return adminUserDao.readAdminUserByAccount(account);
    }

    @Override
    public List<AdminUser> readAllAdminUsers() {
        return adminUserDao.readAllAdminUsers();
    }

    @Override
    public List<AdminRole> readAllAdminRoles() {
        return adminRoleDao.readAllAdminRoles();
    }

    @Override
    public List<AdminPermission> readAllAdminPermissions() {
        return adminPermissionDao.readAllAdminPermissions();
    }

    protected void checkUser(AdminUser user, GenericResponse response) {
        if (user == null) {
            response.addErrorCode("invalidUser");
        } else if (user.getActiveStatusFlag() == null || ! user.getActiveStatusFlag()) {
            response.addErrorCode("inactiveUser");
        }
    }
    
    protected void checkPassword(String password, String confirmPassword, GenericResponse response) {
        if (password == null || confirmPassword == null || "".equals(password) || "".equals(confirmPassword)) {
            response.addErrorCode("invalidPassword");
        } else if (! password.equals(confirmPassword)) {
            response.addErrorCode("passwordMismatch");
        }
    }
    
    protected void checkExistingPassword(String password, AdminUser user, GenericResponse response) {
        if ( !passwordEncoder.matches(password, user.getPassword()) ) {
            response.addErrorCode("invalidPassword");
        }
    }

    @Override
    @Transactional("wakaTransactionManager")
    public GenericResponse changePassword(String account, String oldPassword, String password, String confirmPassword) {
        GenericResponse response = new GenericResponse();
        AdminUser user = null;
        if (account != null) {
            user = adminUserDao.readAdminUserByAccount(account);
        }
        checkUser(user, response);
        checkPassword(password, confirmPassword, response);

        if (!response.getHasErrors()) {
            checkExistingPassword(oldPassword, user, response);
        }

        if (!response.getHasErrors()) {
            user.setUnencodedPassword(password);
            saveAdminUser(user);
        }

        return response;
    }
    
    @Override
    @Transactional("wakaTransactionManager")
    public AdminUser changePassword(PasswordChange passwordChange) {
        AdminUser user = readAdminUserByAccount(passwordChange.getUsername());
        user.setUnencodedPassword(passwordChange.getNewPassword());
        user = saveAdminUser(user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(passwordChange.getUsername(), passwordChange.getNewPassword(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        auth.setAuthenticated(false);
        return user;
    }
    
}

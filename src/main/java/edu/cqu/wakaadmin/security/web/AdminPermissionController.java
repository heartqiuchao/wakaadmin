package edu.cqu.wakaadmin.security.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.cqu.wakaadmin.security.domain.AdminPermission;
import edu.cqu.wakaadmin.security.domain.AdminRole;
import edu.cqu.wakaadmin.security.service.AdminSecurityService;

/**
 * @author qiu
 */

@Controller("wakaAdminPermissionController")
public class AdminPermissionController {
	@Resource(name="wakaAdminSecurityService")
	AdminSecurityService adminSecurityService;
	
	@RequestMapping(value = { "/permission-management" }, method = RequestMethod.GET)
    public String permissionManagement(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<AdminRole> allRoles= adminSecurityService.readAllAdminRoles();
		List<AdminPermission> allPermissions=adminSecurityService.readAllAdminPermissions();
		model.addAttribute("roles", allRoles);
		model.addAttribute("Permissions", allPermissions);
		return "permissionlist";
    }
	
	public String addPermisson(HttpServletRequest request,HttpServletResponse response){
		AdminPermission newPermission=new AdminPermission();
		
		return "add success";
	}
	
	public String deletePermisson(HttpServletRequest request,HttpServletResponse response){
		return "delete success";
	}
	
	public String PermissionsToRole(HttpServletRequest request,HttpServletResponse response){
		AdminRole currentRole=adminSecurityService
				.readAdminRoleById(Long.valueOf(request.getParameter("role_id")));
		Set<AdminPermission> permissions=new  HashSet<AdminPermission>();
		permissions.add(adminSecurityService.readAdminPermissionById(
				Long.valueOf(request.getParameter("permission_id"))));
		
		currentRole.setAllPermissions(permissions);
		return "success";
		
	}
	
}

package edu.cqu.wakaadmin.security.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.cqu.wakaadmin.security.domain.AdminUser;
import edu.cqu.wakaadmin.security.service.AdminSecurityService;

/**
 * @author qiu
 */
@Controller("wakaAdminSecurityController")
public class AdminSecurityController {
	
	@Resource(name="wakaAdminSecurityService")
    protected AdminSecurityService adminSecurityService;
	
    @RequestMapping(value = { "/user-management" }, method = RequestMethod.GET)
    public String userManagement(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<AdminUser> users=adminSecurityService.readAllAdminUsers();
		model.addAttribute("users", users);
		
    	return "tables_dynamic";
    }
    
    @RequestMapping(value = { "/role-management" }, method = RequestMethod.GET)
    public String roleManagement(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "rolelist";
    }
    
    @RequestMapping(value = { "/user-new" }, method = RequestMethod.GET)
    public String newUser(HttpServletRequest request, HttpServletResponse response, Model model){
    	//这里需要在页面判断账户名是否存在
    	List<AdminUser> allUser=adminSecurityService.readAllAdminUsers();
		model.addAttribute("users", allUser);
		return "user_new";
    }
    
    @RequestMapping(value = { "/user-delete" }, method = RequestMethod.GET)
    public String reduceUser(HttpServletRequest request, HttpServletResponse response, Model model){
		return "user_delete";
    }
    @RequestMapping(value = { "/user-edit" }, method = RequestMethod.GET)
    public String uptateUser(HttpServletRequest request, HttpServletResponse response, Model model){
		return "user_update";
    }
    @RequestMapping(value = { "/searchUser" })
    public String searchUser(HttpServletRequest request, HttpServletResponse response, Model model){
    
    	AdminUser searchedUser=adminSecurityService.readAdminUserByAccount(request.getParameter("searchaccount"));
    	
    	if(searchedUser!=null)
    	{
    		model.addAttribute("searchedUser", searchedUser);
    		return "user_search";
    	}
    	
    	return "noAccess";
    }
    
    @RequestMapping(value = { "/AddUser" },method = RequestMethod.POST)
    public String addUser(HttpServletRequest request, HttpServletResponse response, Model model){
		AdminUser user=new AdminUser();
		String currentAccount=request.getParameter("account").toString();
		if(isExist(currentAccount)==false)
		{
			user.setName(request.getParameter("username"));
			user.setAccount(request.getParameter("account"));
			user.setUnencodedPassword(request.getParameter("unencodedpassword"));
			adminSecurityService.saveAdminUser(user);
		}
		
		List<AdminUser> users=adminSecurityService.readAllAdminUsers();
		model.addAttribute("users", users);
    	return "tables_dynamic";
    }
	public boolean isExist(String userAccount){
    	List<AdminUser> users=adminSecurityService.readAllAdminUsers();
    	List<String> accounts = new ArrayList<String>();
    	for(AdminUser user:users)
    	{
    		accounts.add(user.getAccount().toString());
    	}
    	for(String account:accounts)
    	{
    		if(account.equals(userAccount))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    @RequestMapping(value = { "/DeleteUser" }, method = RequestMethod.POST)
    public String deleteUser(HttpServletRequest request, HttpServletResponse response, Model model){
    	AdminUser user=adminSecurityService.readAdminUserByAccount(request.getParameter("account"));
		adminSecurityService.deleteAdminUser(user);
		
		List<AdminUser> users=adminSecurityService.readAllAdminUsers();
		model.addAttribute("users", users);
    	return "tables_dynamic";
    }
    
    @RequestMapping(value = { "/UpdateUser" }, method = RequestMethod.POST)
    public String editUser(HttpServletRequest request, HttpServletResponse response, Model model){
    	AdminUser user=adminSecurityService.readAdminUserByAccount(request.getParameter("edit_account"));
//    	model.addAttribute("editUser",user);
    	user.setName(request.getParameter("edit_username"));
		
		adminSecurityService.saveAdminUser(user);
	
		
		List<AdminUser> users=adminSecurityService.readAllAdminUsers();
		model.addAttribute("users", users);
		
    	return "tables_dynamic";
    }
    @RequestMapping(value = { "/updatePassword" }, method = RequestMethod.POST)
    public String updatePassword(HttpServletRequest request, HttpServletResponse response, Model model){
    	
		adminSecurityService.changePassword(request.getParameter("edit_account"), 
											request.getParameter("old_password"),
											request.getParameter("new_password"), 
											request.getParameter("confirm_password"));
		
		List<AdminUser> users=adminSecurityService.readAllAdminUsers();
		model.addAttribute("users", users);
		
		return "tables_dynamic";
    }
}

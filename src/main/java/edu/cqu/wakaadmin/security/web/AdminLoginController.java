package edu.cqu.wakaadmin.security.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.cqu.wakaadmin.security.domain.AdminMenu;
import edu.cqu.wakaadmin.security.domain.AdminUser;
import edu.cqu.wakaadmin.security.service.AdminNavigationService;
import edu.cqu.wakaadmin.security.service.AdminSecurityService;

@Controller("wakaAdminLoginController")
public class AdminLoginController {

    @Resource(name="wakaAdminSecurityService")
    protected AdminSecurityService adminSecurityService;

    @Resource(name="wakaAdminNavigationService")
    protected AdminNavigationService adminNavigationService;

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String baseLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "login";
    }

    @RequestMapping(value = {"/", "/loginSuccess"}, method = RequestMethod.GET)
    public String loginSuccess(HttpServletRequest request, HttpServletResponse response, Model model) {
        AdminMenu adminMenu = adminNavigationService.buildMenu(getCurrentAdminUser());
        model.addAttribute("adminMenu", adminMenu);
        // 绘制主框架。。。
        if (!adminMenu.getAdminModules().isEmpty()) {
            return "index";
        }
        
        return "noAccess";
    }
   
    protected AdminUser getCurrentAdminUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            if (auth != null) {
                UserDetails temp = (UserDetails) auth.getPrincipal();

                return adminSecurityService.readAdminUserByAccount(temp.getUsername());
            }
        }

        return null;
    }
}

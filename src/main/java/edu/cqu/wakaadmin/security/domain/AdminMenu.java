package edu.cqu.wakaadmin.security.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hui 
 */
public class AdminMenu {

    private List<AdminModule> adminModules = new ArrayList<AdminModule>();

    public List<AdminModule> getAdminModules() {
        return adminModules;
    }

    public void setAdminModule(List<AdminModule> adminModules) {
        this.adminModules = adminModules;
    }
    
}

package edu.cqu.wakaadmin.security.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author hui
 */
@Entity
@Table(name = "WAKA_ADMIN_MODULE")
public class AdminModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ADMIN_MODULE_ID")
    protected Long id;

    @Column(name = "NAME", nullable=false)
    protected String name;

    @Column(name = "MODULE_KEY", nullable=false)
    protected String moduleKey;

    @Column(name = "ICON", nullable=true)
    protected String icon;

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    protected List<AdminFunction> functions = new ArrayList<AdminFunction>();

    @Column(name = "DISPLAY_ORDER", nullable=true)
    protected Integer displayOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<AdminFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<AdminFunction> functions) {
        this.functions = functions;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public AdminModule copy() {
        AdminModule module = new AdminModule();
        module.displayOrder = this.displayOrder;
        module.icon = this.icon;
        module.moduleKey = this.moduleKey;
        module.name = this.name;
        module.id = this.id;
        
        return module;
    }

}

package edu.cqu.wakaadmin.security.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author hui
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "WAKA_ADMIN_FUNCTION")
public class AdminFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ADMIN_FUNCTION_ID")
    protected Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "FUNCTION_KEY", nullable = false, unique = true)
    protected String functionKey;

    @Column(name = "URL", nullable = true)
    protected String url;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ADMIN_MODULE_ID")
    protected AdminModule module;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "WAKA_ADMIN_FUNC_PERM_XREF", 
        joinColumns = @JoinColumn(name = "ADMIN_FUNCTION_ID", referencedColumnName = "ADMIN_FUNCTION_ID"), 
        inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected List<AdminPermission> permissions = new ArrayList<AdminPermission>();

    @Column(name = "DISPLAY_ORDER", nullable = true)
    protected Integer displayOrder;
    
    @Column(name = "RELATE_ENTITY", nullable = true)
    protected String relateEntity;

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

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AdminModule getModule() {
        return module;
    }

    public void setModule(AdminModule module) {
        this.module = module;
    }

    public List<AdminPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<AdminPermission> permissions) {
        this.permissions = permissions;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public String getRelateEntity() {
        return relateEntity;
    }

    public void setRelateEntity(String relateEntity) {
        this.relateEntity = relateEntity;
    }

}

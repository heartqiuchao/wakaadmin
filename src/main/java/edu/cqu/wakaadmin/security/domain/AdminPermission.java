package edu.cqu.wakaadmin.security.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author hui
 */
@Entity
@Table(name = "WAKA_ADMIN_PERMISSION")
public class AdminPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ADMIN_PERMISSION_ID")
    protected Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "TYPE", nullable = false)
    protected String type;

    @Column(name = "DESCRIPTION", nullable = false)
    protected String description;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminRole.class)
    @JoinTable(name = "WAKA_ADMIN_ROLE_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"))
    protected Set<AdminRole> allRoles = new HashSet<AdminRole>();

    @OneToMany(mappedBy = "adminPermission", cascade = { CascadeType.ALL })
    protected List<AdminPermissionEntity> restrictedEntities = new ArrayList<AdminPermissionEntity>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "WAKA_ADMIN_PERMISSION_XREF", 
        joinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), 
        inverseJoinColumns = @JoinColumn(name = "CHILD_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected List<AdminPermission> allChildPermissions = new ArrayList<AdminPermission>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "WAKA_ADMIN_PERMISSION_XREF", 
        joinColumns = @JoinColumn(name = "CHILD_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), 
        inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected List<AdminPermission> allParentPermissions = new ArrayList<AdminPermission>();

    @Column(name = "IS_PARENT")
    protected Boolean isParent = Boolean.FALSE;

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

    public PermissionType getType() {
        return PermissionType.getInstance(type);
    }

    public void setType(PermissionType type) {
        if (type != null) {
            this.type = type.getTypeName();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AdminRole> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(Set<AdminRole> allRoles) {
        this.allRoles = allRoles;
    }

    public List<AdminPermissionEntity> getRestrictedEntities() {
        return restrictedEntities;
    }

    public void setRestrictedEntities(List<AdminPermissionEntity> restrictedEntities) {
        this.restrictedEntities = restrictedEntities;
    }

    public List<AdminPermission> getAllChildPermissions() {
        return allChildPermissions;
    }

    public List<AdminPermission> getAllParentPermissions() {
        return allParentPermissions;
    }

    public Boolean isParent() {
        if (isParent == null) {
            return false;
        }
        return isParent;
    }

}

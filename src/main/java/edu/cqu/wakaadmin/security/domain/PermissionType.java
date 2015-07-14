package edu.cqu.wakaadmin.security.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hui
 */
public class PermissionType implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PermissionType.class);

    private static final Map<String, PermissionType> TYPES = new LinkedHashMap<String, PermissionType>();

    public static final PermissionType READ  = new PermissionType("READ", "读取");
    public static final PermissionType CREATE  = new PermissionType("CREATE", "创建");
    public static final PermissionType UPDATE  = new PermissionType("UPDATE", "更新");
    public static final PermissionType DELETE  = new PermissionType("DELETE", "删除");
    public static final PermissionType ALL  = new PermissionType("ALL", "所有");
    public static final PermissionType OTHER  = new PermissionType("OTHER", "其它");
    
    public static PermissionType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String name;
    private String friendlyName;

    public PermissionType() {
        
    }

    public PermissionType(final String type, final String friendlyType) {
        this.friendlyName = friendlyType;
        addType(type);
    }

    public String getTypeName() {
        return name;                                        
    }

    public String getFriendlyTypeName() {
        return friendlyName;
    }

    private void addType(final String typeName) {
        this.name = typeName;
        if (!TYPES.containsKey(typeName)) {
            TYPES.put(typeName, this);
        }  else {
            if(logger.isWarnEnabled()) {
                logger.warn("权限类型" + typeName + "已经存在！不能重复添加");
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PermissionType) {
            PermissionType anotherObj= (PermissionType) obj;
            if(name == null) {
                if(anotherObj.name == null)
                    return true;
            } else {
                return name.equals(anotherObj.name);
            }
        }
        return false;
    }
}

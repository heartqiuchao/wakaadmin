package edu.cqu.wakaadmin.security.util;

import java.io.Serializable;


/**
 * @author hui
 */
public class PasswordChange implements Serializable{

    private static final long serialVersionUID = 1L;

    private String currentPassword;

    private String newPassword;

    private String newPasswordConfirm;
    
    private String username;

    public PasswordChange(String username) {
        this.username = username;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}

package lk.fujilanka.scm.core.dto;

import java.io.Serializable;
import java.util.Set;

public class AuthResponse implements Serializable {
    private String token;
    private String username;
    private Set<String> roles;
    private boolean requiresPasswordChange;

    public AuthResponse() {}

    public AuthResponse(String token, String username, Set<String> roles) {
        this(token, username, roles, false);
    }

    public AuthResponse(String token, String username, Set<String> roles, boolean requiresPasswordChange) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.requiresPasswordChange = requiresPasswordChange;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isRequiresPasswordChange() {
        return requiresPasswordChange;
    }

    public boolean getRequiresPasswordChange() {
        return requiresPasswordChange;
    }

    public void setRequiresPasswordChange(boolean requiresPasswordChange) {
        this.requiresPasswordChange = requiresPasswordChange;
    }
}

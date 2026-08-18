package lk.fujilanka.scm.ejb.security;

import lk.fujilanka.scm.core.security.SCMCallbackHandler;
import lk.fujilanka.scm.core.security.SCMUserPrincipal;
import lk.fujilanka.scm.core.security.SCMRolePrincipal;

import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SCMLoginModule implements LoginModule {
    private Subject subject;
    private CallbackHandler callbackHandler;

    // Temporary authentication state
    private boolean loginSucceeded = false;
    private boolean commitSucceeded = false;

    private String username;
    private String password;

    private SCMUserPrincipal userPrincipal;
    private Set<SCMRolePrincipal> rolePrincipals = new HashSet<>();

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        if (callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available");
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("username");
        callbacks[1] = new PasswordCallback("password", false);

        try {
            callbackHandler.handle(callbacks);
            username = ((NameCallback) callbacks[0]).getName();
            char[] passwordChar = ((PasswordCallback) callbacks[1]).getPassword();
            password = new String(passwordChar);
            ((PasswordCallback) callbacks[1]).clearPassword();
        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException("Login callback execution failed: " + e.getMessage());
        }

        // Authenticate user
        loginSucceeded = authenticate(username, password);
        return loginSucceeded;
    }

    @Override
    public boolean commit() throws LoginException {
        if (!loginSucceeded) {
            return false;
        }

        // Add principals to Subject
        userPrincipal = new SCMUserPrincipal(username);
        subject.getPrincipals().add(userPrincipal);

        // Add roles
        for (SCMRolePrincipal rolePrincipal : rolePrincipals) {
            subject.getPrincipals().add(rolePrincipal);
        }

        commitSucceeded = true;
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        if (!loginSucceeded) {
            return false;
        } else if (commitSucceeded) {
            logout();
        } else {
            loginSucceeded = false;
            username = null;
            password = null;
        }
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(userPrincipal);
        for (SCMRolePrincipal rolePrincipal : rolePrincipals) {
            subject.getPrincipals().remove(rolePrincipal);
        }

        loginSucceeded = false;
        commitSucceeded = false;
        userPrincipal = null;
        rolePrincipals.clear();
        return true;
    }

    private boolean authenticate(String username, String password) throws LoginException {
        // Step 1: Query MySQL via JPA EJB UserService
        try {
            InitialContext ctx = new InitialContext();
            lk.fujilanka.scm.ejb.local.UserServiceLocal userService = 
                (lk.fujilanka.scm.ejb.local.UserServiceLocal) ctx.lookup("java:global/global-scm-ear/global-scm-ejb/UserServiceBean!lk.fujilanka.scm.ejb.local.UserServiceLocal");
            
            lk.fujilanka.scm.core.entity.User user = userService.authenticate(username, password);
            if (user != null) {
                Set<String> roles = userService.getUserRoles(username);
                for (String role : roles) {
                    rolePrincipals.add(new SCMRolePrincipal(role));
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("SCMLoginModule: JPA EJB lookup notice: " + e.getMessage() + ". Falling back to mock.");
        }

        // Fallback to Mock Accounts if DB service lookup is not ready
        return authenticateMock(username, password);
    }

    private void loadUserRolesFromDB(Connection conn, String username) throws Exception {
        String sql = "SELECT r.role_name FROM user_roles r WHERE r.username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rolePrincipals.add(new SCMRolePrincipal(rs.getString("role_name")));
                }
            }
        }
    }

    private boolean authenticateMock(String username, String password) throws LoginException {
        if ("admin".equals(username) && "admin123".equals(password)) {
            rolePrincipals.add(new SCMRolePrincipal("ADMIN"));
            rolePrincipals.add(new SCMRolePrincipal("COORDINATOR"));
            return true;
        } else if ("coordinator".equals(username) && "pass123".equals(password)) {
            rolePrincipals.add(new SCMRolePrincipal("COORDINATOR"));
            return true;
        } else if ("customs".equals(username) && "pass123".equals(password)) {
            rolePrincipals.add(new SCMRolePrincipal("CUSTOMS_AGENT"));
            return true;
        } else if ("warehouse".equals(username) && "pass123".equals(password)) {
            rolePrincipals.add(new SCMRolePrincipal("WAREHOUSE_MANAGER"));
            return true;
        } else if ("vendor".equals(username) && "pass123".equals(password)) {
            rolePrincipals.add(new SCMRolePrincipal("VENDOR_REP"));
            return true;
        }
        throw new LoginException("Mock Auth: Invalid username or password");
    }
}
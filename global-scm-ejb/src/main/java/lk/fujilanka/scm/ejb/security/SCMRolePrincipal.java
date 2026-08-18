package lk.fujilanka.scm.ejb.security;

import java.io.Serializable;
import java.security.Principal;

public class SCMRolePrincipal implements Principal, Serializable {
    private final String name;

    public SCMRolePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SCMRolePrincipal{" + "name='" + name + '\'' + '}';
    }
}
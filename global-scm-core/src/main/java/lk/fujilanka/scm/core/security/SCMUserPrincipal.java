package lk.fujilanka.scm.core.security;

import java.io.Serializable;
import java.security.Principal;

public class SCMUserPrincipal implements Principal, Serializable {
    private final String name;

    public SCMUserPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SCMUserPrincipal that = (SCMUserPrincipal) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

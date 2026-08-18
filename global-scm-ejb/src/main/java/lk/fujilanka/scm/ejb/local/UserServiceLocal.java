package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.entity.User;
import java.util.Set;

@Local
public interface UserServiceLocal {
    User registerUser(String username, String rawPassword, Set<String> roleNames);
    User authenticate(String username, String rawPassword);
    Set<String> getUserRoles(String username);
}

package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.entity.User;

import java.util.List;
import java.util.Set;

@Local
public interface UserServiceLocal {
    User registerUser(String username, String rawPassword, Set<String> roleNames);
    User authenticate(String username, String rawPassword);
    Set<String> getUserRoles(String username);
    List<User> getAllUsers();
    User toggleUserStatus(Long userId);
    User updateUserRole(Long userId, String roleName);
    User changePassword(String username, String currentPassword, String newPassword);
}

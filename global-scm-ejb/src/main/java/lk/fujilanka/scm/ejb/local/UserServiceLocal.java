package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.entity.User;

import java.util.List;
import java.util.Set;

@Local
public interface UserServiceLocal {
    User registerUser(String username, String rawPassword, Set<String> roleNames);
    User registerUser(String username, String email, String rawPassword, Set<String> roleNames);
    User registerUser(String username, String email, String rawPassword, Set<String> roleNames, boolean requiresPasswordChange);
    User registerUser(String username, String fullName, String email, String phone, String department, String rawPassword, Set<String> roleNames, boolean requiresPasswordChange);
    User registerUser(String username, String fullName, String email, String phone, String department, Long vendorId, String rawPassword, Set<String> roleNames, boolean requiresPasswordChange);
    String resetUserTemporaryPassword(Long userId);
    User authenticate(String username, String rawPassword);
    Set<String> getUserRoles(String username);
    List<User> getAllUsers();
    User toggleUserStatus(Long userId);
    User updateUserRole(Long userId, String roleName);
    User changePassword(String username, String currentPassword, String newPassword);
}

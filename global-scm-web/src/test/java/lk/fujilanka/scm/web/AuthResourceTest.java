package lk.fujilanka.scm.web;

import jakarta.ws.rs.core.Response;
import lk.fujilanka.scm.core.dto.AuthResponse;
import lk.fujilanka.scm.core.dto.LoginRequest;
import lk.fujilanka.scm.core.entity.Role;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.local.UserServiceLocal;
import lk.fujilanka.scm.web.resource.AuthResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthResourceTest {

    @Mock
    private UserServiceLocal userService;

    @InjectMocks
    private AuthResource authResource;

    @BeforeEach
    void setUp() throws Exception {
        Field serviceField = AuthResource.class.getDeclaredField("userService");
        serviceField.setAccessible(true);
        serviceField.set(authResource, userService);
    }

    @Test
    @DisplayName("Should return 200 OK and JWT token with requiresPasswordChange flag on successful login")
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest("coordinator01", "Scm#9876!");
        User mockUser = new User("coordinator01", "Nimal Perera", "nimal@gmail.com", "+94 71 222 3344", "Logistics", "Scm#9876!", Set.of(new Role("COORDINATOR")), true);

        when(userService.authenticate("coordinator01", "Scm#9876!")).thenReturn(mockUser);
        when(userService.getUserRoles("coordinator01")).thenReturn(Set.of("COORDINATOR"));

        Response response = authResource.login(request);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof AuthResponse);

        AuthResponse auth = (AuthResponse) response.getEntity();
        assertEquals("coordinator01", auth.getUsername());
        assertTrue(auth.isRequiresPasswordChange());
        assertNotNull(auth.getToken());
    }

    @Test
    @DisplayName("Should return 401 UNAUTHORIZED for invalid credentials")
    void testLoginInvalidCredentials() {
        LoginRequest request = new LoginRequest("admin", "wrongpass");

        when(userService.authenticate("admin", "wrongpass")).thenReturn(null);

        Response response = authResource.login(request);

        assertEquals(401, response.getStatus());
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST for missing payload fields")
    void testLoginMissingPayload() {
        Response response = authResource.login(new LoginRequest(null, null));
        assertEquals(400, response.getStatus());
    }
}

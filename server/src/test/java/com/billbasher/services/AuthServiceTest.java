package com.billbasher.services;
import com.billbasher.dto.UserDTO;
import com.billbasher.exception.UserNotActiveException;
import com.billbasher.model.UserDAO;
import com.billbasher.pswrdhashing.AuthResponse;
import com.billbasher.pswrdhashing.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLogin_ValidCredentials() {
        UserDAO user = new UserDAO();
        user.setIsActive(true);
        user.setPassword("$2a$10$0xKfhzT11Rc4WD5mVrHsEOLNACcsJ9kRW11Nv8AfraOa572HYgKfq");
        when(userService.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any(UserDAO.class))).thenReturn("testToken");
        AuthResponse authResponse = authService.login("testUser", "password");
        assertNotNull(authResponse);
        UserDTO userDTO = authResponse.getUser();
        assertNotNull(userDTO);
        String jwtToken = authResponse.getToken();
        assertNotNull(jwtToken);
        assertEquals("testToken", jwtToken);
        verify(userService, times(1)).findByUsernameOrEmail(anyString(), anyString());
    }

    @Test
    void testLogin_InvalidCredentials() {
        when(userService.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotActiveException.class, () -> authService.login("nonExistingUser", "password"));
        verify(userService, times(1)).findByUsernameOrEmail(anyString(), anyString());
    }

    @Test
    void testLoginUserNotActive() {
        UserDAO user = new UserDAO();
        user.setIsActive(false);
        when(userService.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
        assertThrows(UserNotActiveException.class, () -> authService.login("testUser", "password"));
        verify(userService, times(1)).findByUsernameOrEmail(anyString(), anyString());
    }

    @Test
    void testLoginUserNotFound() {
        when(userService.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotActiveException.class, () -> authService.login("testUser", "password"));
        verify(userService, times(1)).findByUsernameOrEmail(anyString(), anyString());
    }

    @Test
    void testIsTokenValid_ValidToken() {
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        boolean isValid = authService.isTokenValid("validToken");
        assertTrue(isValid);
        verify(jwtUtil, times(1)).validateToken("validToken");
    }

    @Test
    void testIsTokenValid_InvalidToken() {
        when(jwtUtil.validateToken(anyString())).thenReturn(false);
        boolean isValid = authService.isTokenValid("invalidToken");
        assertFalse(isValid);
        verify(jwtUtil, times(1)).validateToken("invalidToken");
    }
}
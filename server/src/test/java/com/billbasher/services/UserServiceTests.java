package com.billbasher.services;

import com.billbasher.dto.UserDTO;
import com.billbasher.model.UserDAO;
import com.billbasher.repository.UserRep;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.when;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @Mock
    private UserRep userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testDeleteUserById() {

        Long userId = 1L;

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testUpdateUserById_UserExists() {
        Long userId = 1L;
        UserDAO existingUser = new UserDAO();
        existingUser.setUserId(userId);
        existingUser.setUsername("existingUsername");
        existingUser.setEmail("existingUser@example.com");

        UserDAO updatedUser = new UserDAO();
        updatedUser.setUserId(userId);
        updatedUser.setName("Ivans");
        updatedUser.setSurname("Kuropatkins");
        updatedUser.setUsername("IvansK");
        updatedUser.setPassword("newpassword");
        updatedUser.setEmail("IvansKK@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        UserDAO result = userService.updateUserById(userId, updatedUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(updatedUser);
        assertEquals(updatedUser, result);
    }

    @Test
    public void testUpdateUserById_UserNotExists() {
        Long userId = 1L;
        UserDAO updatedUser = new UserDAO();
        updatedUser.setUserId(userId);
        updatedUser.setName("Ivans");
        updatedUser.setSurname("Kuropatkins");
        updatedUser.setUsername("IvansK");
        updatedUser.setPassword("newpassword");
        updatedUser.setEmail("IvansKK@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUserById(userId, updatedUser));
    }

    @Test
    public void testGetAllUsers() {
        UserDAO user1 = new UserDAO();
        user1.setUserId(1L);
        user1.setName("Ivans");
        user1.setSurname("Kuropatkins");
        user1.setUsername("IvansK");
        user1.setPassword("password");
        user1.setEmail("IvansKK@example.com");
        UserDAO user2 = new UserDAO();
        user2.setUserId(2L);
        user2.setName("Jane");
        user2.setSurname("Smith");
        user2.setUsername("janesmith");
        user2.setPassword("newpassword");
        user2.setEmail("janesmith@example.com");

        List<UserDAO> userDAOList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userDAOList);
        List<UserDTO> result = userService.getAllUsers();

        assertEquals(userDAOList.size(), result.size());
        for (int i = 0; i < userDAOList.size(); i++) {
            UserDAO dao = userDAOList.get(i);
            UserDTO dto = result.get(i);
            assertEquals(dao.getUserId(), dto.getUserId());
            assertEquals(dao.getName(), dto.getName());
            assertEquals(dao.getSurname(), dto.getSurname());
            assertEquals(dao.getUsername(), dto.getUsername());
            assertEquals(dao.getEmail(), dto.getEmail());
            assertEquals(dao.getIsActive(), dto.getIsActive());
            assertEquals(dao.getUserCreated(), dto.getUserCreated());
        }
    }

    @Test
    public void testFindUserById_UserExists() {
        Long userId = 1L;
        UserDAO userDAO = new UserDAO();
        userDAO.setUserId(userId);
        userDAO.setName("Ivans");
        userDAO.setSurname("Kuropatkins");
        userDAO.setUsername("IvansK");
        userDAO.setPassword("password");
        userDAO.setEmail("IvansKK@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userDAO));
        UserDTO result = userService.findUserById(userId);

        assertEquals(userDAO.getUserId(), result.getUserId());
        assertEquals(userDAO.getName(), result.getName());
        assertEquals(userDAO.getSurname(), result.getSurname());
        assertEquals(userDAO.getUsername(), result.getUsername());
        assertEquals(userDAO.getEmail(), result.getEmail());
        assertEquals(userDAO.getIsActive(), result.getIsActive());
        assertEquals(userDAO.getUserCreated(), result.getUserCreated());
    }

    @Test
    public void testFindUserById_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.findUserById(userId));
    }

    @Test
    public void testFindByUsernameOrEmail_UsernameFound() {
        String username = "IvansK";
        UserDAO userDAO = new UserDAO();
        userDAO.setUserId(1L);
        userDAO.setName("Ivans");
        userDAO.setSurname("Kuropatkins");
        userDAO.setUsername(username);
        userDAO.setPassword("password");
        userDAO.setEmail("IvansKK@example.com");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userDAO));
        Optional<UserDAO> result = userService.findByUsernameOrEmail(username, "test@example.com");
        assertEquals(Optional.of(userDAO), result);
    }

    @Test
    public void testFindByUsernameOrEmail_EmailFound() {
        String email = "IvansKK@example.com";
        UserDAO userDAO = new UserDAO();
        userDAO.setUserId(1L);
        userDAO.setName("Ivans");
        userDAO.setSurname("Kuropatkins");
        userDAO.setUsername("IvansK");
        userDAO.setPassword("password");
        userDAO.setEmail(email);

        when(userRepository.findByUsername("IvansK")).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userDAO));
        Optional<UserDAO> result = userService.findByUsernameOrEmail("IvansK", email);
        assertEquals(Optional.of(userDAO), result);
    }

    @Test
    public void testFindByUsernameOrEmail_NotFound() {
        String username = "IvansK";
        String email = "IvansKK@example.com";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<UserDAO> result = userService.findByUsernameOrEmail(username, email);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testRegisterUser_UserNotExists() {
        UserDAO userDAO = new UserDAO();
        userDAO.setUsername("newUser");
        userDAO.setEmail("newuser@example.com");
        userDAO.setPassword("somePassword2003");
        when(userRepository.findByUsernameOrEmail(userDAO.getUsername(), userDAO.getEmail())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> userService.registerUser(userDAO));
        verify(userRepository, times(1)).save(userDAO);
    }

    @Test
    public void testDeactivateUser_UserExists() {
        Long userId = 1L;
        UserDAO userDAO = new UserDAO();
        userDAO.setUserId(userId);
        userDAO.setIsActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userDAO));
        when(userRepository.save(userDAO)).thenReturn(userDAO);
        UserDAO result = userService.deactivateUser(userId);
        assertFalse(result.getIsActive());
    }

    @Test
    public void testDeactivateUser_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.deactivateUser(userId));
    }

    @Test
    public void testGetAllActiveUsers() {
        UserDAO user1 = new UserDAO();
        user1.setUserId(1L);
        user1.setIsActive(true);
        UserDAO user2 = new UserDAO();
        user2.setUserId(2L);
        user2.setIsActive(true);
        List<UserDAO> activeUsersDAO = new ArrayList<>();
        activeUsersDAO.add(user1);
        activeUsersDAO.add(user2);

        when(userRepository.findActiveUsers()).thenReturn(activeUsersDAO);
        List<UserDTO> result = userService.getAllActiveUsers();

        assertEquals(activeUsersDAO.size(), result.size());
        for (int i = 0; i < activeUsersDAO.size(); i++) {
            assertEquals(activeUsersDAO.get(i).getUserId(), result.get(i).getUserId());
            assertEquals(activeUsersDAO.get(i).getName(), result.get(i).getName());
            assertEquals(activeUsersDAO.get(i).getSurname(), result.get(i).getSurname());
            assertEquals(activeUsersDAO.get(i).getUsername(), result.get(i).getUsername());
            assertEquals(activeUsersDAO.get(i).getEmail(), result.get(i).getEmail());
            assertEquals(activeUsersDAO.get(i).getIsActive(), result.get(i).getIsActive());
            assertEquals(activeUsersDAO.get(i).getUserCreated(), result.get(i).getUserCreated());
        }
    }
}
package com.billbasher.services;

import com.billbasher.dto.UserDTO;
import com.billbasher.exception.UserAlreadyExistsException;
import com.billbasher.pswrdhashing.PasswordEncoderUtil;
import com.billbasher.repository.UserRep;
import com.billbasher.model.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRep userRepository;

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public UserDAO updateUserById(Long id, UserDAO user) {
        try {
            Optional<UserDAO> userDAO = userRepository.findById(id);
            if (userDAO.isEmpty()) {
                throw new NoSuchElementException("User not found with id: " + id);
            }
            Optional<UserDAO> existingUserByUsername = userRepository.findByUsername(user.getUsername());
            Optional<UserDAO> existingUserByEmail = userRepository.findByEmail(user.getEmail());
            boolean usernameIsChanged = !userDAO.get().getUsername().equals(user.getUsername());
            boolean emailIsChanged = !userDAO.get().getEmail().equals(user.getEmail());
            if(
               usernameIsChanged && existingUserByUsername.isPresent() ||
               emailIsChanged && existingUserByEmail.isPresent()
            ) {
                throw new UserAlreadyExistsException("Username or email already exists");
            }
            user.setPassword(PasswordEncoderUtil.encodePassword(user.getPassword()));
            return userRepository.save(user);
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UserDTO> getAllUsers() {
        List<UserDAO> userDAOList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserDAO userDAO : userDAOList) {
            userDTOList.add(UserDTO.mapUserDAOToDTO(userDAO));
        }
        return userDTOList;
    }

    public UserDTO findUserById(Long id) {
        Optional<UserDAO> userDAO = userRepository.findById(id);
        if (userDAO.isPresent()) {
            return UserDTO.mapUserDAOToDTO(userDAO.get());
        }
        throw new NoSuchElementException("User not found with id: " + id);
    }

    public Optional<UserDAO> findByUsernameOrEmail(String username, String email) {
        Optional<UserDAO> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isPresent()) {
            return userByUsername;
        }
        return userRepository.findByEmail(email);
    }

    public void registerUser(UserDAO userDAO) {
        try {
            userDAO.setPassword(PasswordEncoderUtil.encodePassword(userDAO.getPassword()));
            Optional<UserDAO> existingUser = userRepository.findByUsernameOrEmail(userDAO.getUsername(), userDAO.getEmail());
            if (existingUser.isPresent()) {
                throw new UserAlreadyExistsException("Username or email already exists");
            }
            userRepository.save(userDAO);
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserDAO deactivateUser(Long userId) {
        Optional<UserDAO> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserDAO user = userOptional.get();
            user.setIsActive(false);
            return userRepository.save(user);
        } else {
            throw new NoSuchElementException("User not found with id: " + userId);
        }
    }

    public List<UserDTO> getAllActiveUsers() {
        List<UserDAO> activeUsersDAO = userRepository.findActiveUsers();
        List<UserDTO> activeUsersDTO = new ArrayList<>();
        for (UserDAO userDAO : activeUsersDAO) {
            activeUsersDTO.add(UserDTO.mapUserDAOToDTO(userDAO));
        }
        return activeUsersDTO;
    }
}
package com.billbasher.dto;

import com.billbasher.model.UserDAO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {
    private Long userId;
    private String name;
    private String surname;
    private String username;
    private String email;
    private Boolean isActive;
    private LocalDateTime userCreated = LocalDateTime.now();

    public static UserDTO mapUserDAOToDTO(UserDAO userDAO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userDAO.getUserId());
        userDTO.setName(userDAO.getName());
        userDTO.setSurname(userDAO.getSurname());
        userDTO.setUsername(userDAO.getUsername());
        userDTO.setEmail(userDAO.getEmail());
        userDTO.setIsActive(userDAO.getIsActive());
        userDTO.setUserCreated(userDAO.getUserCreated());
        return userDTO;
    }
}
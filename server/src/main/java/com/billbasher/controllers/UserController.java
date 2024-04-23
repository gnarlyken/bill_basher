package com.billbasher.controllers;

import com.billbasher.dto.UserDTO;
import com.billbasher.exception.UserAlreadyExistsException;
import com.billbasher.model.UserDAO;
import com.billbasher.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("/api/v1/users/{id}")
  public ResponseEntity<UserDTO> findUserById(@PathVariable("id") Long id) {
    UserDTO userDTO = userService.findUserById(id);
    return ResponseEntity.ok(userDTO);
  }

  @PutMapping("/api/v1/users/{id}")
  public ResponseEntity<Object> updateUserById(@PathVariable("id") Long id, @Valid @RequestBody UserDAO user) {
    try {
      userService.updateUserById(id, user);
      return new ResponseEntity<>((Object) user, HttpStatus.OK);
    } catch (UserAlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @DeleteMapping("/api/v1/users/{id}")
  public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
    userService.deleteUserById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/api/v1/users")
  public ResponseEntity<Object> getAllUsers() {
    List<UserDTO> users = userService.getAllUsers();
    if (users.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>((Object) users, HttpStatus.OK);
  }

  @PutMapping("/api/v1/users/deactivate/{id}")
  public ResponseEntity<UserDTO> deactivateUserById(@PathVariable("id") Long id) {
    try {
      UserDAO deactivatedUser = userService.deactivateUser(id);
      UserDTO deactivatedUserDTO = UserDTO.mapUserDAOToDTO(deactivatedUser);
      return ResponseEntity.ok(deactivatedUserDTO);
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/api/v1/users/getAllActiveUsers")
  public ResponseEntity<List<UserDTO> > getAllActiveUsers() {
    List<UserDTO> activeUsers = userService.getAllActiveUsers();
    if (activeUsers.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(activeUsers);
  }
}
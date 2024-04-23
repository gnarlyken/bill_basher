package com.billbasher.pswrdhashing;

import com.billbasher.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    private UserDTO user;
    private String token;

    public AuthResponse(UserDTO user, String token) {
        this.user = user;
        this.token = token;
    }
}
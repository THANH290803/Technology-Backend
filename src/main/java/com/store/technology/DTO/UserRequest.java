package com.store.technology.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private Long roleId;
}

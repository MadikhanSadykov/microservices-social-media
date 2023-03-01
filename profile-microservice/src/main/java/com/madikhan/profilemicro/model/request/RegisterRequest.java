package com.madikhan.profilemicro.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @NotNull(message = "Username cannot be empty")
    @Size(min = 4, message = "Username must not be less than 4 characters")
    private String username;

    @NotNull(message = "Email cannot be empty")
    @Email
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be less greater than 8 characters")
    private String password;

    @NotNull(message = "First Name cannot be empty")
    @Size(min = 2, message = "First Name must not be less than 2 characters")
    private String firstName;

    @NotNull(message = "First Name cannot be empty")
    @Size(min = 2, message = "First Name must not be less than 2 characters")
    private String lastName;

}

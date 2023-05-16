package com.madikhan.profilemicro.model.request;

import lombok.Data;

@Data
public class ProfileUpdateRequest {

    private String uuid;
    private String firstName;
    private String lastName;
    private String username;
    private String bio;
    private String location;
    private String gender;
    private Integer age;

}

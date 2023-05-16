package com.madikhan.profilemicro.model.response;

import lombok.Data;

@Data
public class SaveProfileResponse {

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String uuid;
    private String location;
    private String gender;
    private Integer age;

}

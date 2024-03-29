package com.madikhan.profilemicro.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.madikhan.profilemicro.model.entity.Interest;
import com.madikhan.profilemicro.model.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"interests", "friends", "requestFromMe", "requestToMe"})
@ToString(exclude = {"interests", "friends", "requestFromMe", "requestToMe"})
public class ProfileDTO implements Serializable {

    private static final long serialVersionUID = -2572028659694425971L;

    private Long id;

    private String uuid;

    @NotNull(message = "Username cannot be empty")
    @Size(min = 4, message = "Username must not be less than 4 characters")
    private String username;

    @NotNull(message = "Email cannot be empty")
    @Email
    private String email;

    @NotNull(message = "First Name cannot be empty")
    @Size(min = 2, message = "First Name must not be less than 2 characters")
    private String firstName;

    @NotNull(message = "First Name cannot be empty")
    @Size(min = 2, message = "First Name must not be less than 2 characters")
    private String lastName;

    private String bio;

    private String location;

    private String gender;

    private Integer age;

    private Set<Interest> interests = new HashSet<>();

    @JsonIgnore
    private Set<Profile> friends = new HashSet<>();

    @JsonIgnore
    private Set<Profile> requestFromMe = new HashSet<>();

    @JsonIgnore
    private Set<Profile> requestToMe = new HashSet<>();

}

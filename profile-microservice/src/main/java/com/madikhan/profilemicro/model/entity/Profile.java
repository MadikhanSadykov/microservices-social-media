package com.madikhan.profilemicro.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"interests"})
@ToString(exclude = {"interests"})
@Table(name = "profile")
public class Profile implements Serializable, UserDetails {

    private static final long serialVersionUID = 5837238503465653050L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, unique=true)
    private String uuid;

    @NotNull(message = "Username cannot be empty")
    @Size(min = 4, message = "Username must not be less than 4 characters")
    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @NotNull(message = "Email cannot be empty")
    @Email
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Column(nullable = false, length = 2048)
    private String password;

    @NotNull(message = "First Name cannot be empty")
    @Size(min = 2, message = "First Name must not be less than 2 characters")
    @Column(nullable = false)
    private String firstName;

    @NotNull(message = "First Name cannot be empty")
    @Size(min = 2, message = "First Name must not be less than 2 characters")
    @Column(nullable = false)
    private String lastName;

    @Column(length = 512)
    private String bio;

    @ManyToMany
    @JoinTable(
            name = "profile_interest",
            joinColumns = { @JoinColumn(name = "profile_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "interest_id", referencedColumnName = "id") }
    )
    private Set<Interest> interests = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

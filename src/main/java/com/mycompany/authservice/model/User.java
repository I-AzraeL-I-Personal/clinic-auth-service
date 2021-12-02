package com.mycompany.authservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.authservice.security.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull(message = "uuid cannot be null")
    @Column(nullable = false, unique = true, updatable = false)
    private UUID userUUID;

    @Column(nullable = false, unique = true)
    @Email(message = "must be a well-formed email address")
    @NotNull(message = "email cannot be null")
    @Size(min = 3, max = 200, message = "email length out of range")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "password must have at least {min} characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    @NotNull(message = "role cannot be null")
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isEnabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.withPrefix()));
    }

    @Override
    public String getUsername() {
        return email;
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
        return isEnabled;
    }
}

package com.mash.loginservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "bot_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    UUID id;
    String username;
    String password;
    @Transient
    String passwordConfirm;

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    //TODO:
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //TODO:
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //TODO:
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //TODO:
    @Override
    public boolean isEnabled() {
        return true;
    }

    //TODO:
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

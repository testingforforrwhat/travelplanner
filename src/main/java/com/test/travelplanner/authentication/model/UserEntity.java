package com.test.travelplanner.authentication.model;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * The @Entity annotation indicates that this class is an entity class,
 * and during project startup, a table will be automatically generated based on this class.
 */
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password_hash;
    @Enumerated(EnumType.STRING)
    private UserRole role;


    public UserEntity() {
    }


    public UserEntity(Long id, String username, String password_hash, UserRole role) {
        this.id = id;
        this.username = username;
        this.password_hash = password_hash;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public String getPassword() {
        return password_hash;
    }


    @Override
    public String getUsername() {
        return username;
    }


    public Long getId() {
        return id;
    }


    public UserRole getRole() {
        return role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password_hash, that.password_hash) && role == that.role;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, username, password_hash, role);
    }


    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password_hash + '\'' +
                ", role=" + role +
                '}';
    }
}

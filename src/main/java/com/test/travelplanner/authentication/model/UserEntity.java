package com.test.travelplanner.authentication.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.OffsetDateTime;
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
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password_hash;
    @Getter
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Setter
    @Getter
    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Setter
    @Getter
    @Size(max = 255)
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Setter
    @Getter
    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @Setter
    @Getter
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Setter
    @Getter
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


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

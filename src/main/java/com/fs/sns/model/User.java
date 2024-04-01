package com.fs.sns.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fs.sns.model.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity userEntity) {
        return new User(
            userEntity.getId(),
            userEntity.getUserName(),
            userEntity.getPassword(),
            userEntity.getRole(),
            userEntity.getRegisteredAt(),
            userEntity.getUpdatedAt(),
            userEntity.getDeletedAt()
        );
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() { // 계정이 만료되지 않았는지 확인하는 메서드
        return this.deletedAt == null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() { // 계정이 잠기지 않았는지 확인하는 메서드
        return this.deletedAt == null;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() { // 자격이 만료되지 않았는지 확인하는 메서드
        return this.deletedAt == null;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() { // 계정이 활성화되었는지 확인하는 메서드
        return this.deletedAt == null;
    }
}

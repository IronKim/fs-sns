package com.fs.sns.model;

import com.fs.sns.model.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class User implements UserDetails {

    private Integer id;
    private String userName;
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    public String getUsername() { // 사용자 이름을 반환하는 메서드
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() { // 계정이 만료되지 않았는지 확인하는 메서드
        return this.deletedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정이 잠기지 않았는지 확인하는 메서드
        return this.deletedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 자격이 만료되지 않았는지 확인하는 메서드
        return this.deletedAt == null;
    }

    @Override
    public boolean isEnabled() { // 계정이 활성화되었는지 확인하는 메서드
        return this.deletedAt == null;
    }
}

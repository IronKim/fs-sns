package com.fs.sns.controller.reponse;

import com.fs.sns.model.User;
import com.fs.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Integer id;
    private String userName;
    private UserRole role;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
            user.getId(),
            user.getUserName(),
            user.getUserRole()
        );
    }
}

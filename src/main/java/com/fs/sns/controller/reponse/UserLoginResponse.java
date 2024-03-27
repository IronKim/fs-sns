package com.fs.sns.controller.reponse;

import com.fs.sns.model.User;
import com.fs.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String token;
}

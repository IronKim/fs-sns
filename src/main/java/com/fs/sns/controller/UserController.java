package com.fs.sns.controller;

import com.fs.sns.controller.response.AlarmResponse;
import com.fs.sns.controller.response.Response;
import com.fs.sns.controller.response.UserJoinResponse;
import com.fs.sns.controller.response.UserLoginResponse;
import com.fs.sns.controller.request.UserJoinRequest;
import com.fs.sns.exception.ErrorCode;
import com.fs.sns.exception.SnsApplicationException;
import com.fs.sns.model.User;
import com.fs.sns.service.AlarmService;
import com.fs.sns.service.UserService;
import com.fs.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AlarmService alarmService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserJoinRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
    }

    @GetMapping("/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        return alarmService.connectAlarm(user.getId());
    }


}

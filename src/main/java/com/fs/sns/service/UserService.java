package com.fs.sns.service;

import com.fs.sns.exception.ErrorCode;
import com.fs.sns.exception.SnsApplicationException;
import com.fs.sns.model.Alarm;
import com.fs.sns.model.Entity.UserEntity;
import com.fs.sns.model.User;
import com.fs.sns.repository.AlarmEntityRepository;
import com.fs.sns.repository.UserCacheRepository;
import com.fs.sns.repository.UserEntityRepository;
import com.fs.sns.util.JwtTokenUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository userCacheRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private long expiredTimeMs;

    public User loadUserByUserName(String userName) {
        return userCacheRepository.getUser(userName).orElseGet(() ->
                userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)))
                );
    }

    @Transactional
    public User join(String userName, String password) {
        // 회원가입하려는 userName으로 회원가입된 user가 있는지
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        // 회원가입 진행 = user를 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {
        // 회원가입 여부 체크
        User user = loadUserByUserName(userName);
        userCacheRepository.setUser(user);

        // 비밀번호 체크
        if(!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        return JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
    }

    public Page<Alarm> alarmList(Integer userId, Pageable pageable) {
        return alarmEntityRepository.findAllByUserId(userId, pageable).map(Alarm::fromEntity);

    }
}

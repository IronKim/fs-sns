package com.fs.sns.service;

import com.fs.sns.exception.ErrorCode;
import com.fs.sns.exception.SnsApplicationException;
import com.fs.sns.model.Entity.PostEntity;
import com.fs.sns.model.Entity.UserEntity;
import com.fs.sns.repository.PostEntityRepository;
import com.fs.sns.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                 new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }
}

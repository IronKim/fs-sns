package com.fs.sns.repository;

import com.fs.sns.model.Entity.PostEntity;
import com.fs.sns.model.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Integer>{

    Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);
}

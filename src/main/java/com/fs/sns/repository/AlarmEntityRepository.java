package com.fs.sns.repository;

import com.fs.sns.model.Entity.AlarmEntity;
import com.fs.sns.model.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer>{
    Page<AlarmEntity> findAllByUser(UserEntity user, Pageable pageable);
}

package com.fs.sns.repository;

import com.fs.sns.model.Entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Integer>{
}

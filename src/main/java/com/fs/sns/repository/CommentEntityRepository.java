package com.fs.sns.repository;

import com.fs.sns.model.Entity.CommentEntity;
import com.fs.sns.model.Entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer>{
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE CommentEntity entity SET entity.deletedAt = current_timestamp where entity.post = :post")
    void deleteAllByPost(@Param("post")PostEntity post);
}

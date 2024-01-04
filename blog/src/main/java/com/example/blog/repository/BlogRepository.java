package com.example.blog.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    Blog findBySeq(long seq);
    Blog findByTitle(String title);
    Blog findByContent(String content);
    Blog findByCategory(String category);
    Blog findByWriteDate(LocalDateTime writeDate);
    Blog findBySearchCount(long searchCount);
    List<Blog> findAllByOrderBySeqDesc();
    List<Blog> findAllByOrderBySearchCountDesc();
    Long countBy();
}

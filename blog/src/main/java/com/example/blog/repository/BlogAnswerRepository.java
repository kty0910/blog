package com.example.blog.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.model.BlogAnswer;

@Repository
public interface BlogAnswerRepository extends JpaRepository<BlogAnswer,Long> {
    BlogAnswer findById(long id);
    List<BlogAnswer> findBySeq(long seq);
    BlogAnswer findByAnswer(String answer);
    BlogAnswer findByWriter(String writer);
    BlogAnswer findByAnswerDate(LocalDateTime answerDate);
}

package com.example.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.model.BlogUser;

@Repository
public interface UserRepository extends JpaRepository<BlogUser,Long> {
    BlogUser findBySeq(long seq);
    List<BlogUser> findById(String id);
    BlogUser findByIdAndPw(String id,String pw);
    List<BlogUser> findByPwAndId(String pw,String id);
    BlogUser findByName(String name);
}

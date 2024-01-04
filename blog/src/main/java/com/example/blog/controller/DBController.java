package com.example.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.model.Blog;
import com.example.blog.repository.BlogRepository;

@RestController
public class DBController {
    @Autowired
    BlogRepository boardRepository;

    @GetMapping("/test")
    public List<Blog> test() {
        return boardRepository.findAll();
    }
}

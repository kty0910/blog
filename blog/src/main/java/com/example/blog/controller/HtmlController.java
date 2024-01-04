package com.example.blog.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blog.model.Blog;
import com.example.blog.model.BlogAnswer;
import com.example.blog.model.BlogUser;
import com.example.blog.repository.BlogAnswerRepository;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HtmlController {
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    BlogAnswerRepository blogAnswerRepository;
    @Autowired
    UserRepository userRepository;

    // @GetMapping("/")
    // public String homeLogin(
    //     Model model,
    //     HttpSession session
    // ) {
    //     List<Blog> blogs = blogRepository.findAllByOrderBySeqDesc();
    //     model.addAttribute("blogs", blogs);

    //     List<Blog> top = blogRepository.findAllByOrderBySearchCountDesc();
    //     model.addAttribute("top", top);

    //     session.invalidate();

    //     return "html/homeLogin";
    // }
    @GetMapping("/")
    public String home(Model model) {
        List<Blog> blogs = blogRepository.findAllByOrderBySeqDesc();
        model.addAttribute("blogs", blogs);

        List<Blog> top = blogRepository.findAllByOrderBySearchCountDesc();
        model.addAttribute("top", top);

        return "html/home";
    }

    @PostMapping("/")
    public String home(
        @RequestParam("id") String id,
        @RequestParam("pw") String pw,
        HttpSession session
    ) {
        BlogUser user;
        user = userRepository.findByIdAndPw(id, pw);
        // int count = userRepository.findByPwAndId(pw, id).size();

        // if (count<1) {
        //     return "html/homeLogin";
        // }

        session.setAttribute("user", user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/";
    }

    @GetMapping("/write")
    public String write(Model model) {
        List<Blog> categories = blogRepository.findAll();
        model.addAttribute("categories", categories);

        return "html/write";
    }

    @PostMapping("/write")
    public String writeSave(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("category") String category
    ) {
        LocalDateTime localdate = LocalDateTime.now();

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setCategory(category);
        blog.setWriteDate(localdate);
        blogRepository.save(blog);

        int last = blogRepository.findAll().size()-1;
        Blog save = blogRepository.findAll().get(last);
        long seq = save.getSeq();

        return "redirect:/board?seq="+seq;
    }

    @GetMapping("/board")
    public String board(
        Model model,
        @RequestParam("seq") long seq
    ) {
        Blog board = blogRepository.findBySeq(seq);
        model.addAttribute("board", board);

        List<BlogAnswer> answers = blogAnswerRepository.findBySeq(seq);
        model.addAttribute("answers", answers);

        for (int i=0; i<blogRepository.count(); i++) {
            if (blogRepository.findAll().get(i).getSeq() == seq) {
                long cnt = blogRepository.findAll().get(i).getSearchCount();
                cnt = cnt+1;
                Blog blog = blogRepository.findAll().get(i);
                blog.setSearchCount(cnt);
                blogRepository.save(blog);
            }
            
        }

        return "html/board";
    }

    @PostMapping("/board")
    public String boardAnswer(
        Model model,
        @RequestParam("seq") long seq,
        @RequestParam("answer") String answer,
        @RequestParam("writer") String writer
    ) {
        Blog board = blogRepository.findBySeq(seq);
        model.addAttribute("board", board);

        BlogAnswer blogAnswer = new BlogAnswer();
        blogAnswer.setSeq(seq);
        blogAnswer.setAnswer(answer);
        blogAnswer.setWriter(writer);
        blogAnswer.setAnswerDate(LocalDateTime.now());
        blogAnswerRepository.save(blogAnswer);

        List<BlogAnswer> answers = blogAnswerRepository.findBySeq(seq);
        model.addAttribute("answers", answers);

        return "redirect:/board?seq="+seq;
    }

    @GetMapping("/board/update")
    public String boardUpdate(
        Model model,
        @RequestParam("seq") long seq
    ) {
        Blog board = blogRepository.findBySeq(seq);
        model.addAttribute("board", board);

        return "html/update";
    }

    @PostMapping("/board/update")
    public String boardUpdatePost(
        @RequestParam("seq") long seq,
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("category") String category
    ) {
        Blog update = blogRepository.findBySeq(seq);
        update.setTitle(title);
        update.setContent(content);
        update.setCategory(category);
        blogRepository.save(update);

        return "redirect:/board?seq="+seq;
    }

    @GetMapping("/board/delete/idadmin/pw1234")
    public String boardDelete(@RequestParam("seq") long seq) {
        blogAnswerRepository.deleteAll(blogAnswerRepository.findBySeq(seq));
        blogRepository.delete(blogRepository.findBySeq(seq));;

        return "redirect:/";
    }

    @GetMapping("/boardList")
    public String boardList(
        Model model,
        @RequestParam(defaultValue = "1") int page
    ) {
        List<Blog> blogs = blogRepository.findAllByOrderBySeqDesc();
        model.addAttribute("blogs", blogs);

        long cnt = blogRepository.countBy();
        model.addAttribute("cnt", cnt);

        int startPage = (page-1)/10*10+1;
        int endPage = (int)Math.ceil(cnt/6.0);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("page", page);

        return "html/boardList";
    }

    @GetMapping("/account")
    public String account() {
        return "html/account";
    }

    @PostMapping("/account")
    public String accountSet(
        @RequestParam("id") String id,
        @RequestParam("pw") String pw,
        @RequestParam("name") String name,
        HttpSession session
    ) {
        BlogUser user = new BlogUser();
        user.setId(id);
        user.setPw(pw);
        user.setName(name);
        session.setAttribute("account", user);
        int count = userRepository.findById(id).size();

        if (count>0) {
            return "redirect:/account";
        }

        session.invalidate();
        userRepository.save(user);

        return "redirect:/";
    }
}

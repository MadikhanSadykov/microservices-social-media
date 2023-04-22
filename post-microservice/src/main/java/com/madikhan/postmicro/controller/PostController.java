package com.madikhan.postmicro.controller;

import com.madikhan.postmicro.model.Post;
import com.madikhan.postmicro.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    public List<Post> getAllProfilePosts() {
        return null;
    }

}

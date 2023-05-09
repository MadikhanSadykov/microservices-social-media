package com.madikhan.postmicro.controller;

import com.madikhan.postmicro.model.Post;
import com.madikhan.postmicro.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api("Post controller")
public class PostController {

//    private final PostService postService;


    @GetMapping
    @ApiOperation("List all posts")
    public List<Post> getAllPosts() {
        return null;
    }

    @GetMapping("/{username}")
    @ApiOperation("List all posts By Username")
    public List<Post> getAllPostsByUsername(@PathVariable(name = "username") String username) {
        return null;
    }

    @GetMapping("/{id}")
    @ApiOperation("List post by id")
    public Post getPostById(@PathVariable(name = "id") Long id) {
        return null;
    }

    @PutMapping()
    @ApiOperation("Update post")
    public Post updatePost(@RequestBody Post post) {
        return null;
    }

}

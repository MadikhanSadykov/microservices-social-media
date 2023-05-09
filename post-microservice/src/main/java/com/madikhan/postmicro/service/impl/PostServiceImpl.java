//package com.madikhan.postmicro.service.impl;
//
//import com.madikhan.postmicro.model.Post;
//import com.madikhan.postmicro.repository.PostRepository;
//import com.madikhan.postmicro.service.PostService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.persistence.EntityNotFoundException;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class PostServiceImpl implements PostService {
//
//    private final PostRepository postRepository;
//
//    @Override
//    public Post save(Post post) {
//        return postRepository.save(post);
//    }
//
//    @Override
//    public Optional<Post> listById(Long id) {
//        return postRepository.findById(id);
//    }
//
//    @Override
//    public Optional<List<Post>> listByUUID(String uuid) {
//        return postRepository.findPostsByUuid(uuid);
//    }
//
//    @Override
//    public List<Post> listAll() {
//        return postRepository.findAll();
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        Optional<Post> optionalPost = listById(id);
//        if (optionalPost.isPresent()) {
//            postRepository.delete(optionalPost.get());
//        } else {
//            throw new EntityNotFoundException("Post not found");
//        }
//    }
//
//    @Override
//    public void delete(Post post) {
//
//    }
//}

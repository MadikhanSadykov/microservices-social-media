package com.madikhan.postmicro.service;

import com.madikhan.postmicro.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {

    Post save(Post post);

    Optional<Post> listById(Long id);

    Optional<List<Post>> listByUUID(String uuid);

    List<Post> listAll();

    void deleteById(Long id);

    void delete(Post post);

}

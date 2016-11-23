package com.spr.controller;

import java.util.UUID;

import com.spr.model.PostContent;
import com.spr.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * REST controller for the blog microservice.
 *
 * @author Matt Sicker
 */
@RestController
@CrossOrigin("http://localhost:8000")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BlogController {

    private final BlogService service;

    @GetMapping("/api/blog/{id}")
    public Mono<PostContent> getPost(@PathVariable final UUID id) {
        return service.getPost(id);
    }

    @PostMapping("/api/blog")
    public Mono<UUID> addPost(@RequestBody Mono<PostContent> content) {
        return service.addPost(content);
    }

    @PutMapping("/api/blog/{id}")
    public Mono<Void> updatePost(@PathVariable final UUID id, @RequestBody final Mono<PostContent> content) {
        return service.updatePost(id, content);
    }
}

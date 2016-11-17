package com.spr.service;

import java.util.UUID;

import com.spr.dao.BlogPost;
import com.spr.dao.BlogRepository;
import com.spr.model.PostContent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Service to manage blog posts asynchronously via reactive stream APIs.
 *
 * @author Matt Sicker
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired)) // may not be necessary anymore in new versions of spring?
public class BlogService {

    private final BlogRepository repository;

    public Mono<PostContent> getPost(final UUID id) {
        return Mono.defer(() -> Mono.justOrEmpty(repository.findOne(id)))
            .subscribeOn(Schedulers.elastic())
            .map(post -> new PostContent(post.getTitle(), post.getAuthor(), post.getBody()));
    }

    public Mono<UUID> addPost(final Mono<PostContent> contentMono) {
        return contentMono
            .map(content -> new BlogPost(UUID.randomUUID(), content.getTitle(), content.getAuthor(), content.getBody()))
            .publishOn(Schedulers.parallel())
            .doOnNext(repository::save)
            .map(BlogPost::getId);
    }

    public Mono<Void> updatePost(final UUID id, final Mono<PostContent> contentMono) {
        return contentMono
            .map(content -> new BlogPost(id, content.getTitle(), content.getAuthor(), content.getBody()))
            .publishOn(Schedulers.parallel())
            .doOnNext(repository::save)
            .then();
    }
}

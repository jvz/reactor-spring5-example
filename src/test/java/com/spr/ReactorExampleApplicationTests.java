package com.spr;

import java.io.IOException;
import java.util.UUID;

import com.spr.controller.BlogController;
import com.spr.model.PostContent;
import com.spr.setup.CassandraSetup;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactorExampleApplicationTests {

    @Autowired
    private BlogController controller;

    @BeforeClass
    public static void setUpClass() throws IOException {
        CassandraSetup.init();
    }

    @Test
    public void sanityTests() throws Exception {
        final Mono<PostContent> nonexistent = controller.getPost(UUID.randomUUID());
        assertThat(nonexistent.hasElement().block()).isFalse();
        final UUID id = controller.addPost(
            Mono.just(newPostContent())).block();
        final Mono<PostContent> contentMono = controller.getPost(id);
        assertThat(contentMono.block()).isEqualTo(newPostContent());
        controller.updatePost(id, Mono.just(newPostContent().withBody("Other body"))).block();
        final Mono<PostContent> updatedMono = controller.getPost(id);
        assertThat(updatedMono.block()).isEqualTo(newPostContent().withBody("Other body"));
    }

    private static PostContent newPostContent() {
        return PostContent.builder().title("Title").author("Author").body("Body").build();
    }
}

package com.spr.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * REST API data format for blog posts.
 *
 * @author Matt Sicker
 */
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class PostContent {
    @NonNull
    String title;
    @NonNull
    String author;
    @NonNull
    String body;
}

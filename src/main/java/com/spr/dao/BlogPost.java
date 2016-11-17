package com.spr.dao;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * A blog post entity for storage and retrieval in Cassandra.
 *
 * @author Matt Sicker
 */
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Table
public class BlogPost {
    @PrimaryKey
    UUID id;
    String title;
    String author;
    String body;
}

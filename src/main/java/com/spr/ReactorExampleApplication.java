package com.spr;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.service.EmbeddedCassandraService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ReactorExampleApplication {

    public static void main(String[] args) throws Exception {
        setupCassandraDirectories();
        startEmbeddedCassandra();
        setupDatabase();
        SpringApplication.run(ReactorExampleApplication.class, args);
    }

    /**
     * Creates the base storage directories required for running a Cassandra instance.
     */
    private static void setupCassandraDirectories() throws IOException {
        final Path root = Files.createTempDirectory("cassandra");
        final Path config = root.resolve("cassandra.yaml");
        Files.copy(ReactorExampleApplication.class.getResourceAsStream("/cassandra.yaml"), config);
        System.setProperty("cassandra.config", "file:" + config.toString());
        System.setProperty("cassandra.storagedir", root.toString());
        System.setProperty("cassandra-foreground", "true");
        Stream.of(DatabaseDescriptor.getAllDataFileLocations())
            .map(root::resolve)
            .forEach(ReactorExampleApplication::mkdir);
    }

    private static void mkdir(final Path path) {
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            log.error("Error creating directory", e);
        }
    }

    /**
     * Creates and backgrounds an instance of Cassandra.
     */
    private static void startEmbeddedCassandra() {
        final Thread thread = new Thread(new Cassandra());
        thread.setDaemon(true);
        thread.start();
    }

    private static class Cassandra implements Runnable {
        private final EmbeddedCassandraService cassandra = new EmbeddedCassandraService();

        @Override
        public void run() {
            try {
                cassandra.start();
            } catch (final IOException e) {
                log.error("Could not start Cassandra", e);
            }
        }
    }

    /**
     * Creates the keyspace and table used in this microservice.
     */
    private static void setupDatabase() {
        final Cluster cluster = new Cluster.Builder().addContactPoints(InetAddress.getLoopbackAddress()).build();
        try (final Session session = cluster.connect()) {
            session.execute("CREATE KEYSPACE blogs WITH REPLICATION = " +
                "{ 'class' : 'SimpleStrategy', 'replication_factor' : 16 };");
            session.execute("CREATE TABLE blogs.blogpost (" +
                "id uuid PRIMARY KEY," +
                "title text," +
                "author text," +
                "body text" +
                ");");
        }
    }
}

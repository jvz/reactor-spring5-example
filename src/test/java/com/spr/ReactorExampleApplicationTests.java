package com.spr;

import java.io.IOException;

import com.spr.setup.CassandraSetup;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactorExampleApplicationTests {

    @BeforeClass
    public static void setUpClass() throws IOException {
        CassandraSetup.init();
    }

    @Test
    public void contextLoads() {
    }

}

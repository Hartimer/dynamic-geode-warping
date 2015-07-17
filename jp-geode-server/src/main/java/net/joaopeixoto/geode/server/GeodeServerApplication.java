package net.joaopeixoto.geode.server;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeodeServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(GeodeServerApplication.class, args);
        System.out.println("Press any key to exit");
        System.in.read();
    }
}

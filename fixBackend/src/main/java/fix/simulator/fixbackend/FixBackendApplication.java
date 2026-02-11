package fix.simulator.fixbackend;

import fix.simulator.fixbackend.config.DotenvInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FixBackendApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FixBackendApplication.class)
                .initializers(new DotenvInitializer())
                .run(args);
    }

}

package itss.group22.bookexchangeeasy.config;
import itss.group22.bookexchangeeasy.entity.Role;
import itss.group22.bookexchangeeasy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

@Configuration
public class Initializer {
    @Autowired
    private RoleRepository roleRepository;


    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
//            if (roleRepository.count() == 0) {
//                roleRepository.saveAll(Stream.of("ADMIN", "BOOK_EXCHANGER", "BOOKSTORE")
//                        .map(name -> new Role(null, name)).toList());
//            }
        };
    }

    @Bean
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS) // Or AFTER_TEST_CLASS if needed
    public Object sqlExecution() {
        return null; // Required by Spring, but the method body doesn't need specific logic
    }
}

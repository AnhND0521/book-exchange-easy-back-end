package itss.group22.bookexchangeeasy.config;

import itss.group22.bookexchangeeasy.entity.Role;
import itss.group22.bookexchangeeasy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Initializer {
    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            roleRepository.saveAll(List.of(
                    new Role(null, "ADMIN"),
                    new Role(null, "BOOK_OWNER"),
                    new Role(null, "BOOK_BORROWER"),
                    new Role(null, "BOOKSTORE_OWNER")
            ));
        };
    }
}

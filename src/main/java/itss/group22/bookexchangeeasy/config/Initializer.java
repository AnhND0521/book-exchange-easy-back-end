package itss.group22.bookexchangeeasy.config;

import itss.group22.bookexchangeeasy.entity.Role;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.repository.RoleRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

@Configuration
public class Initializer {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            if (roleRepository.count() == 0) {
                roleRepository.saveAll(Stream.of("ADMIN", "BOOK_OWNER", "BOOK_BORROWER", "BOOKSTORE_OWNER")
                        .map(name -> new Role(null, name)).toList());
            }
        };
    }
}

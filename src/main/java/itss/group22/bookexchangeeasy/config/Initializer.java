package itss.group22.bookexchangeeasy.config;
import itss.group22.bookexchangeeasy.entity.Role;
import itss.group22.bookexchangeeasy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.stream.Stream;

@Configuration
public class Initializer {
    @Autowired
    private RoleRepository roleRepository;


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

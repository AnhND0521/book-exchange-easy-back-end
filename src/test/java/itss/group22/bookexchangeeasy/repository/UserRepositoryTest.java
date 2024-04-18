package itss.group22.bookexchangeeasy.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void givenUser_whenFindByCorrectEmail_returnFoundUser() {
        userRepository.save(User.builder().email("test@gmail.com").name("Test").gender(Gender.MALE).build());

        var result = userRepository.findByEmail("test@gmail.com");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test");
    }

    @Test
    void givenUser_whenFindByIncorrectEmail_returnEmpty() {
        userRepository.save(User.builder().email("test@gmail.com").name("Test").gender(Gender.MALE).build());

        var result = userRepository.findByEmail("test@email.com");

        assertThat(result).isEmpty();
    }
}

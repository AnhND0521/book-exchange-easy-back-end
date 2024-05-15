package itss.group22.bookexchangeeasy.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

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

    @Test
    void givenUserList_whenFindByEmailOrName_returnUsersWithMatchingEmailOrName() {
        userRepository.saveAll(List.of(
                User.builder().email("thisistestemail@gmail.com").name("This is a user").build(),
                User.builder().email("anothermail@gmail.com").name("Another user for test").build(),
                User.builder().email("amailthatdoesntmatch@gmail.com").name("A user that doesn't match").build()
        ));

        String keyword = "Test";
        var result = userRepository.findByEmailOrName(keyword, PageRequest.of(0, 50));

        assertThat(result.size()).isEqualTo(2);
        result.forEach(user ->
                assertThat(user.getEmail().toLowerCase().contains(keyword.toLowerCase())
                        || user.getName().toLowerCase().contains(keyword.toLowerCase()))
                        .isTrue()
        );
    }
}

package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.ChatConversation;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.utils.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ChatConversationRepositoryTest {
    @Autowired
    private ChatConversationRepository chatConversationRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(LongStream.range(1, 5)
                .mapToObj(i -> User.builder().id(i).name("User " + i).build())
                .toList()
        );

        chatConversationRepository.saveAll(LongStream.range(1, 21)
                .mapToObj(i -> ChatConversation.builder()
                        .id(i)
                        .user1(userRepository.findById(i % 4 + 1).get())
                        .user2(userRepository.findById((i + 1) % 4 + 1).get())
                        .lastMessageTime(RandomUtils.randomPastTime())
                        .build())
                .toList()
        );
    }

    @Test
    void givenConversations_whenFindByUser_returnConversationsOfThatUser() {
        Page<ChatConversation> page = chatConversationRepository.findByUser(1L, PageRequest.of(0, 5));
        List<ChatConversation> list = page.toList();

        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getUser1().getId() == 1L || list.get(i).getUser2().getId() == 1L).isTrue();
            if (i < list.size() - 1) {
                assertThat(list.get(i).getLastMessageTime()).isAfterOrEqualTo(list.get(i + 1).getLastMessageTime());
            }
        }
    }
}

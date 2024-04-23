package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Transaction;
import itss.group22.bookexchangeeasy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(List.of(
                User.builder().id(1L).name("User 1").build(),
                User.builder().id(2L).name("User 2").build(),
                User.builder().id(3L).name("User 3").build()
        ));

        long minEpoch = LocalDateTime.now().minusYears(1).toEpochSecond(ZoneOffset.UTC) * 1000;  // 1 year in the past
        long maxEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000;  // Current time
        transactionRepository.saveAll(IntStream.range(0, 30).mapToObj(i -> {
            LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(new Random().nextLong(minEpoch, maxEpoch)), ZoneOffset.UTC);
            long ownerId = i % 3 + 1;
            long borrowerId = (i + 1) % 3 + 1;
            return Transaction.builder()
                    .id(UUID.randomUUID().toString())
                    .timestamp(timestamp)
                    .owner(userRepository.findById(ownerId).orElse(null))
                    .borrower(userRepository.findById(borrowerId).orElse(null))
                    .build();
        }).toList());
    }

    @Test
    void givenTransactionList_whenFindAllByOrderByTimestampDesc_thenReturnTransactionsInDescendingTimeOrder() {
        Page<Transaction> transactions = transactionRepository.findAllByOrderByTimestampDesc(PageRequest.of(0, 20));

        assertThat(transactions.getSize()).isEqualTo(20);
        assertThat(transactions.getTotalElements()).isEqualTo(30);
        assertThat(transactions.getTotalPages()).isEqualTo(2);

        List<Transaction> list = transactions.stream().toList();
        for (int i = 0; i < list.size() - 1; i++) {
            assertThat(list.get(i).getTimestamp()).isAfterOrEqualTo(list.get(i + 1).getTimestamp());
        }
    }

    @Test
    void givenTransactionList_whenFindByUserOrderByTimestampDesc_thenReturnTransactionsInDescendingTimeOrderOfThatUser() {
        Page<Transaction> transactions = transactionRepository.findByUserOrderByTimestampDesc(1L, PageRequest.of(0, 20));

        assertThat(transactions.getSize()).isEqualTo(20);
        assertThat(transactions.getTotalElements()).isEqualTo(20);
        assertThat(transactions.getTotalPages()).isEqualTo(1);

        List<Transaction> list = transactions.stream().toList();
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOwner().getId().equals(1L) || list.get(i).getBorrower().getId().equals(1L)).isTrue();
            if (i < list.size() - 1)
                assertThat(list.get(i).getTimestamp()).isAfterOrEqualTo(list.get(i + 1).getTimestamp());
        }
    }
}

package itss.group22.bookexchangeeasy.config;

import itss.group22.bookexchangeeasy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class Initializer {
    private final DataSource dataSource;
    private final AddressUnitRepository addressUnitRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            if (addressUnitRepository.count() == 0) {
                importSql("data/address_unit.sql");
            }
            if (contactInfoRepository.count() == 0) {
                importSql("data/contact_info.sql");
            }
            if (userRepository.count() == 0) {
                importSql("data/user_info.sql", "data/role.sql", "data/users_roles.sql");
            }
            if (bookRepository.count() == 0) {
                importSql("data/book.sql", "data/money_item.sql", "data/exchange_request.sql");
            }
        };
    }

    private void importSql(String... sqlFilePaths) {
        try {
            for (String path : sqlFilePaths)
                ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource(path));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

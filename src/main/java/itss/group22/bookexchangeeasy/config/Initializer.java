package itss.group22.bookexchangeeasy.config;

import itss.group22.bookexchangeeasy.repository.AddressUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;

@Configuration
public class Initializer {
    @Autowired
    private AddressUnitRepository addressUnitRepository;

    @Autowired
    private DataSource dataSource;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            if (addressUnitRepository.count() == 0)
                ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("data.sql"));
        };
    }
}

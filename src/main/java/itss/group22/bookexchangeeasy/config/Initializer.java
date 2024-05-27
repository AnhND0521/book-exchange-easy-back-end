package itss.group22.bookexchangeeasy.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itss.group22.bookexchangeeasy.entity.*;
import itss.group22.bookexchangeeasy.enums.*;
import itss.group22.bookexchangeeasy.repository.*;
import itss.group22.bookexchangeeasy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Initializer {
    private final DataSource dataSource;
    private final AddressUnitRepository addressUnitRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ExchangeOfferRepository exchangeOfferRepository;
    private final TransactionRepository transactionRepository;
    private final MoneyItemRepository moneyItemRepository;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final TransactionService transactionService;
    private final TaskExecutor taskExecutor;
    private Random random = new Random();

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            if (addressUnitRepository.count() == 0) {
                importSql("data/address_unit.sql");
            }
//            if (contactInfoRepository.count() == 0) {
//                importSql("data/contact_info.sql");itss.group22.bookexchangeeasy.enums.ExchangeItemType
//            }
            if (roleRepository.count() == 0) {
                importSql("data/role.sql");
            }
            if (userRepository.count() == 0) {
//                importSql("data/user_info.sql", "data/role.sql", "data/users_roles.sql");
                log.info("Generating users...");
                generateUsers(20);
                log.info("Done generating users");
            }
            if (categoryRepository.count() == 0) {
                generateCategories();
            }
            if (bookRepository.count() == 0) {
//                importSql("data/book.sql", "data/money_item.sql", "data/exchange_request.sql");
                log.info("Generating books...");
                generateBooks(100);
                log.info("Done generating books");
            }
            if (exchangeOfferRepository.count() == 0) {
                log.info("Generating offers...");
                generateExchangeOffers(50);
                log.info("Done generating offers");
            }
            if (transactionRepository.count() == 0) {
                log.info("Generating transactions...");
                generateTransactions(30);
                log.info("Done generating transactions");
            }
            log.info("Done preparing data");
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

    @Async
    private void generateUsers(int number) {
        // Admin
        userRepository.save(User.builder()
                .email("admin@bee.com")
                .password(passwordEncoder.encode("admin"))
                .name("Admin")
                .gender(Gender.OTHER)
                .birthDate(LocalDate.now())
                .isVerified(true)
                .isLocked(false)
                .roles(Set.of(roleRepository.findByName("ADMIN").get()))
                .created(randomPastTime(3))
                .build());

        CountDownLatch latch = new CountDownLatch(number);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("https://randomuser.me/api/?results=" + number, String.class);
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            for (var node : root.get("results"))
                taskExecutor.execute(() -> {
                    String email = node.get("email").asText();
                    String password = email.substring(0, email.indexOf("@"));
                    String name = node.get("name").get("first").asText() + " " + node.get("name").get("last").asText();
                    Gender gender = node.get("gender").asText().matches("male|female")
                            ? Gender.valueOf(node.get("gender").asText().toUpperCase())
                            : Gender.OTHER;
                    LocalDate birthDate = LocalDate.parse(node.get("dob").get("date").asText().substring(0, 10));

                    String phoneNumber = node.get("phone").asText().replaceAll("\\D", "");
                    var communes = addressUnitRepository.findByTypeOrderByNameAsc(3);
                    var commune = communes.get(random.nextInt(communes.size()));
                    var district = addressUnitRepository.findById(commune.getParentId()).get();
                    var province = addressUnitRepository.findById(district.getParentId()).get();
                    String detailedAddress = node.get("location").get("street").get("number").asInt() + " " + node.get("location").get("street").get("name").asText();

                    var contactInfo = ContactInfo.builder()
                            .phoneNumber(phoneNumber)
                            .province(province)
                            .district(district)
                            .commune(commune)
                            .detailedAddress(detailedAddress)
                            .build();
                    contactInfo = contactInfoRepository.save(contactInfo);

                    String pictureUrl = node.get("picture").get("large").asText();
                    Set<Role> roles = Set.of(random.nextInt(10) == 0
                            ? roleRepository.findByName("BOOKSTORE").get()
                            : roleRepository.findByName("BOOK_EXCHANGER").get());

                    User user = User.builder()
                            .email(email)
                            .password(passwordEncoder.encode(password))
                            .name(name)
                            .gender(gender)
                            .birthDate(birthDate)
                            .contactInfo(contactInfo)
                            .isVerified(true)
                            .isLocked(false)
                            .pictureUrl(pictureUrl)
                            .roles(roles)
                            .created(randomPastTime(3))
                            .build();
                    userRepository.save(user);
                    log.info("Generated user %d/%d: %s".formatted(number - latch.getCount() + 1, number, name));
                    latch.countDown();
                });

            latch.await();
        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateCategories() {
        List<String> categories = List.of("Romance", "Fantasy", "Horror", "History", "Classics", "Biography", "Mystery", "Science fiction", "Adventure", "Thriller", "Short story", "Historical fiction", "Children's literature", "Young adult");
        categoryRepository.saveAll(categories.stream().map(name -> Category.builder().name(name).build()).toList());
    }

    @Async
    private void generateBooks(int number) {
        String rootUrl = "https://openlibrary.org";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(rootUrl + "/search.json?q=anata+no&limit=" + number, String.class);
            JsonNode root = null;
            root = new ObjectMapper().readTree(response.getBody());

            var allCategories = categoryRepository.findAll();
            var allUsers = userRepository.findAll();

//            int count = 0;
            CountDownLatch latch = new CountDownLatch(number);
            for (var node : root.get("docs"))
                taskExecutor.execute(() -> {
                    String title = node.get("title").asText();
                    String author = node.get("author_name") == null ? null : node.get("author_name").get(0).asText();
                    String publisher = node.get("publisher") == null ? null : node.get("publisher").get(random.nextInt(0, node.get("publisher").size())).asText();
                    Integer publishYear = node.get("publish_year") == null ? null : node.get("publish_year").get(random.nextInt(0, node.get("publish_year").size())).asInt();
                    String language = node.get("language") == null ? null : node.get("language").get(random.nextInt(0, node.get("language").size())).asText();
                    Integer pages = node.get("number_of_pages_median") == null ? null : node.get("number_of_pages_median").asInt();
                    String layout = List.of("Softcover", "Hardcover").get(random.nextInt(2));
                    String description = node.get("first_sentence") == null ? null : node.get("first_sentence").get(random.nextInt(node.get("first_sentence").size())).asText();

                    // get cover image
                    String imagePath = null;
                    try {
                        String bookUrl = rootUrl + node.get("seed").get(0).asText();
                        Document doc = Jsoup.connect(bookUrl).userAgent("Jsoup client").get();
                        var imageNode = doc.selectFirst("div[class~=cover] img");
                        if (imageNode != null) {
                            imagePath = imageNode.attr("src");
                            if (imagePath.startsWith("//")) imagePath = "https:" + imagePath;
                            if (imagePath.endsWith("avatar_book.png")) imagePath = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    List<Category> categories = IntStream.range(0, random.nextInt(1, 6))
                            .mapToObj(allCategories::get)
                            .collect(Collectors.toSet())
                            .stream().toList();

                    BookStatus status = BookStatus.AVAILABLE;
                    User owner = allUsers.get(random.nextInt(allUsers.size()));
                    List<User> concernUser = null;
                    while (concernUser.size() < 10) {
                        int randomIndex = random.nextInt(allUsers.size());
                        User randomUser = allUsers.get(randomIndex);

                        if (!concernUser.contains(randomUser)) {
                            concernUser.add(randomUser);
                        }
                    }

                    var created = randomPastTime(2);

                    bookRepository.save(new Book(null, title, author, publisher, publishYear,
                            language, null, null, pages, layout, description,
                            imagePath, categories, status, owner, created,concernUser));
                    log.info("Generated book %d/%d: %s".formatted(number - latch.getCount() + 1, number, title));
                    latch.countDown();
//                count++;
//                if (count % 10 == 0) log.info(count + "/" + number);
                });

            latch.await();
        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateExchangeOffers(int number) {
        var allUsers = userRepository.findAll();

        exchangeOfferRepository.saveAll(IntStream.range(0, number).mapToObj(i -> {
            User owner;
            List<Book> ownerBooks;
            do {
                owner = allUsers.get(random.nextInt(allUsers.size()));
            } while ((ownerBooks = bookRepository.findByOwnerIdAndStatus(owner.getId(), BookStatus.AVAILABLE)).isEmpty());

            User borrower;
            do {
                borrower = allUsers.get(random.nextInt(allUsers.size()));
            } while (borrower.getId().equals(owner.getId()));

            Book targetBook = ownerBooks.get(random.nextInt(ownerBooks.size()));
            List<Book> borrowerBooks;
            ExchangeItemType exchangeItemType = (borrowerBooks = bookRepository.findByOwnerIdAndStatus(borrower.getId(), BookStatus.AVAILABLE)).isEmpty() ? ExchangeItemType.MONEY : ExchangeItemType.BOOK;
            Book bookItem = null;
            MoneyItem moneyItem = null;

            if (exchangeItemType.equals(ExchangeItemType.BOOK)) {
                bookItem = borrowerBooks.get(random.nextInt(borrowerBooks.size()));
            } else {
                moneyItem = MoneyItem.builder().amount((double) random.nextInt(200000)).unit("VND").build();
                moneyItemRepository.save(moneyItem);
            }
            ExchangeOfferStatus status = ExchangeOfferStatus.PENDING;

            return ExchangeOffer.builder()
                    .owner(owner)
                    .borrower(borrower)
                    .targetBook(targetBook)
                    .exchangeItemType(exchangeItemType)
                    .bookItem(bookItem)
                    .moneyItem(moneyItem)
                    .status(status)
                    .timestamp(randomPastTime(2))
                    .build();
        }).toList());
    }

    private void generateTransactions(int number) {
        var offers = exchangeOfferRepository.findByStatus(ExchangeOfferStatus.PENDING);
        int count = 0;
        while (count < number && offers.size() > 0) {
            var chosenOffer = offers.get(random.nextInt(offers.size()));
            transactionService.acceptOffer(chosenOffer.getTargetBook().getId(), chosenOffer.getId());
            count++;
            offers = exchangeOfferRepository.findByStatus(ExchangeOfferStatus.PENDING);
        }
        transactionRepository.findAll().forEach(t -> {
            t.setTimestamp(randomPastTime(2));
            t.setStatus(TransactionStatus.values()[random.nextInt(TransactionStatus.values().length)]);
            transactionRepository.save(t);
        });
    }

    private LocalDateTime randomPastTime(int monthsBefore) {
        long minEpoch = LocalDateTime.now().minusMonths(monthsBefore).toEpochSecond(ZoneOffset.UTC) * 1000;
        long maxEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000;  // Current time
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(random.nextLong(minEpoch, maxEpoch)), ZoneOffset.UTC);
    }
}

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    private final StoreEventRepository eventRepository;
    private final PostRepository postRepository;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final TransactionService transactionService;
    private final TaskExecutor taskExecutor;
    private Random random = new Random();

    @Value("${app.user.default-picture-url}")
    private String defaultPictureUrl;

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
                generateUsers(50);
                log.info("Done generating users");
            }
            if (bookRepository.count() == 0) {
//                importSql("data/book.sql", "data/money_item.sql", "data/exchange_request.sql");
                log.info("Generating books...");
                generateBooks(20);
                log.info("Done generating books");
            }
            if (exchangeOfferRepository.count() == 0) {
                log.info("Generating offers...");
                generateExchangeOffers(100);
                log.info("Done generating offers");
            }
            if (transactionRepository.count() == 0) {
                log.info("Generating transactions...");
                generateTransactions(30);
                log.info("Done generating transactions");
            }
            if (eventRepository.count() == 0) {
                log.info("Generating events...");
                generateEvents(15);
                log.info("Done generating events");
            }
            if (postRepository.count() == 0) {
                log.info("Generating posts...");
                generatePosts(50);
                log.info("Done generating posts");
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
                .pictureUrl(defaultPictureUrl)
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

    @Transactional
    private void generateBooks(int numberPerCategory) {
        String baseUrl = "https://openlibrary.org";
        String coverBaseUrl = "https://covers.openlibrary.org/b/id/";
        List<String> subjects = List.of(
                "Fantasy",
                "Historical Fiction",
                "Horror",
                "Humor",
                "Literature",
                "Magic",
                "Mystery and detective stories",
                "Plays",
                "Poetry",
                "Romance",
                "Science Fiction",
                "Short Stories",
                "Thriller",
                "Young Adult",
                "Biology",
                "Chemistry",
                "Mathematics",
                "Physics",
                "Programming",
                "Management",
                "Entrepreneurship",
                "Business Economics",
                "Business Success",
                "Finance",
                "Ancient Civilization",
                "Archaeology",
                "Anthropology",
                "World War II",
                "Social Life and Customs",
                "Anthropology",
                "Religion",
                "Political Science",
                "Psychology"
        );

        List<User> users = userRepository.findAll();

        int latchCount = numberPerCategory * subjects.size();
        CountDownLatch latch = new CountDownLatch(latchCount);

        for (String subject : subjects) {
            log.info("Generating category: {}", subject);
            Category category = categoryRepository.findByName(subject).orElse(null);
            if (Objects.isNull(category)) {
                category = Category.builder().name(subject).build();
                categoryRepository.save(category);
            }
            final Category thisCategory = category;
            try {
                String url = baseUrl + "/subjects/" + subject.toLowerCase().replaceAll("\\s+", "_") + ".json?limit=" + numberPerCategory;
                log.info("Fetching url: {}", url);
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                JsonNode root = new ObjectMapper().readTree(response.getBody());

                var works = root.get("works");

                for (var work : works)
                    taskExecutor.execute(() -> {
                        try {
                            Book book;
                            if (bookRepository.findByTitle(work.get("title").asText()).isPresent()) {
                                return;
                            }

                            book = new Book();

                            // Title
                            book.setTitle(work.get("title").asText());

                            // Author
                            var authors = work.get("authors");
                            StringBuilder authorNames = new StringBuilder();
                            for (var author : authors) {
                                if (!authorNames.isEmpty())
                                    authorNames.append(", ");
                                authorNames.append(author.get("name").asText());
                            }
                            book.setAuthor(authorNames.toString());

                            String apiUrl = baseUrl + work.get("key").asText() + ".json";
                            log.info("Fetching url: {}", apiUrl);
                            ResponseEntity<String> workResponse = restTemplate.getForEntity(apiUrl, String.class);
                            JsonNode workRoot = new ObjectMapper().readTree(workResponse.getBody());

                            // Description
                            if (Objects.nonNull(workRoot.get("description")) && !workRoot.get("description").isNull()) {
                                String description = workRoot.get("description").asText();
                                if (description.length() > 1000)
                                    description = description.substring(0, 997) + "...";
                                book.setDescription(description);
                            }

                            // Layout
                            if (random.nextInt(2) == 1)
                                book.setLayout(List.of("Softcover", "Hardcover").get(random.nextInt(2)));

                            // Image path
                            if (!work.get("cover_id").isNull()) {
                                book.setImagePath(coverBaseUrl + work.get("cover_id").asText() + "-L.jpg");
                            }

                            // Category
                            book.setCategories(List.of(thisCategory));

                            // Status
                            book.setStatus(BookStatus.AVAILABLE);

                            // Owner
                            book.setOwner(users.get(random.nextInt(users.size())));

                            // Created
                            book.setCreated(randomPastTime(3));

                            // Concerned users
                            List<User> concernedUsers = new ArrayList<>();
                            int countConcernedUsers = random.nextInt(5);
                            while (countConcernedUsers-- > 0) {
                                concernedUsers.add(users.get(random.nextInt(users.size())));
                            }
                            book.setConcernedUsers(concernedUsers);

                            String editionKey = null;
                            if (!work.get("cover_edition_key").isNull()) {
                                editionKey = work.get("cover_edition_key").asText();
                            } else if (!work.get("lending_edition").isNull()) {
                                editionKey = work.get("lending_edition").asText();
                            } else if (!work.get("availability").isNull() && !work.get("availability").get("openlibrary_edition").isNull()) {
                                editionKey = work.get("availability").get("openlibrary_edition").asText();
                            } else {
                                bookRepository.save(book);

                                latch.countDown();
                                log.info(
                                        "Generated book #{}: '{}'",
                                        latchCount - latch.getCount() + 1,
                                        book.getTitle()
                                );

                                return;
                            }

                            apiUrl = baseUrl + "/books/" + editionKey + ".json";
                            log.info("Fetching url: {}", apiUrl);
                            ResponseEntity<String> editionResponse = restTemplate.getForEntity(apiUrl, String.class);
                            JsonNode editionRoot = new ObjectMapper().readTree(editionResponse.getBody());

                            // Publisher
                            if (Objects.nonNull(editionRoot.get("publishers")) && !editionRoot.get("publishers").isNull() && !editionRoot.get("publishers").isEmpty()) {
                                book.setPublisher(editionRoot.get("publishers").get(0).asText());
                            }

                            // Publish year
                            if (Objects.nonNull(editionRoot.get("publish_date")) && !editionRoot.get("publish_date").isNull()) {
                                String regex = "\\d{4}";

                                Pattern pattern = Pattern.compile(regex);
                                Matcher matcher = pattern.matcher(editionRoot.get("publish_date").asText());

                                while (matcher.find()) {
                                    String year = matcher.group();
                                    book.setPublishYear(Integer.valueOf(year));
                                }
                            }

                            // Language
                            if (Objects.nonNull(editionRoot.get("languages")) && !editionRoot.get("languages").isNull() && !editionRoot.get("languages").isEmpty()) {
                                String languageKey = editionRoot.get("languages").get(0).get("key").asText();
                                String language = languageKey.substring(languageKey.lastIndexOf('/') + 1);
                                book.setLanguage(language);
                            }

                            // Number of pages
                            if (Objects.nonNull(editionRoot.get("number_of_pages")) && !editionRoot.get("number_of_pages").isNull()) {
                                book.setPages(editionRoot.get("number_of_pages").asInt());
                            }

                            bookRepository.save(book);

                            latch.countDown();
                            log.info(
                                    "Generated book #{}: '{}'",
                                    latchCount - latch.getCount() + 1,
                                    book.getTitle()
                            );

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
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

    private void generateEvents(int number) {
        String baseUrl = "https://baconipsum.com/api/?format=text";
        List<User> users = userRepository.findAll();
        CountDownLatch latch = new CountDownLatch(number);
        for (int i = 0; i < number; i++)
//            taskExecutor.execute(() ->
        {
            String type = random.nextInt(2) == 0 ? "all-meat" : "meat-and-filler";

            String randomSentence = restTemplate.getForEntity(baseUrl + "&type=%s&sentences=%d".formatted(type, 1), String.class).getBody();
            assert randomSentence != null;
            List<String> words = Arrays.stream(randomSentence.split("\\s+")).toList();
            String name = String.join(" ", words.subList(0, random.nextInt(2, 5)));

            String description = restTemplate.getForEntity(baseUrl + "&type=%s&paras=%d".formatted(type, 1), String.class).getBody();

            LocalDateTime from = randomTime(LocalDateTime.now().minusMonths(3), LocalDateTime.now().plusMonths(3));
            LocalDateTime to = randomTime(from, LocalDateTime.now().plusMonths(3));

            User owner = users.get(random.nextInt(users.size()));

            Set<User> concernedUsers = new HashSet<>();
            int countConcernedUsers = random.nextInt(users.size() / 2);
            while (countConcernedUsers-- > 0) {
                concernedUsers.add(users.get(random.nextInt(users.size())));
            }

            LocalDateTime created = randomPastTime(3);

            String imagePath = "https://picsum.photos/%d/%d".formatted(800 + random.nextInt(10) - 5, 600 + random.nextInt(10) - 5);

            StoreEvent event = StoreEvent.builder()
                    .name(name)
                    .description(description)
                    .startTime(from)
                    .endTime(to)
                    .imagePath(imagePath)
                    .owner(owner)
                    .concernedUsers(concernedUsers)
                    .created(created)
                    .build();

            eventRepository.save(event);
            latch.countDown();
            log.info("Generated event {}/{}: {}", number - latch.getCount(), number, name);
        }
//            );

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePosts(int number) {
        String baseUrl = "https://baconipsum.com/api/?format=text";
        List<User> users = userRepository.findAll();
        List<StoreEvent> events = eventRepository.findAll();
        CountDownLatch latch = new CountDownLatch(number);
        for (int i = 0; i < number; i++)
//            taskExecutor.execute(() ->
        {
            String type = random.nextInt(2) == 0 ? "all-meat" : "meat-and-filler";

            String randomSentence = restTemplate.getForEntity(baseUrl + "&type=%s&sentences=%d".formatted(type, 1), String.class).getBody();
            assert randomSentence != null;
            List<String> words = Arrays.stream(randomSentence.split("\\s+")).toList();
            String title = String.join(" ", words.subList(0, random.nextInt(2, Math.min(8, words.size()))));

            String content = restTemplate.getForEntity(baseUrl + "&type=%s&paras=%d".formatted(type, random.nextInt(5)), String.class).getBody();
            if (content.length() > 2000) content = content.substring(0, 1997) + "...";

            User user = users.get(random.nextInt(users.size()));

            Set<User> likedUsers = new HashSet<>();
            int countLikedUsers = random.nextInt(users.size() * 2 / 3);
            while (countLikedUsers-- > 0) {
                likedUsers.add(users.get(random.nextInt(users.size())));
            }

            LocalDateTime created = randomPastTime(3);
            LocalDateTime lastUpdated = randomTime(created, LocalDateTime.now());

            String imagePath = "https://picsum.photos/%d/%d".formatted(800 + random.nextInt(10) - 5, 600 + random.nextInt(10) - 5);

            StoreEvent event = random.nextInt(2) == 0 ? null : events.get(random.nextInt(events.size()));

            Post post = Post.builder()
                    .title(title)
                    .content(content)
                    .imagePath(imagePath)
                    .user(user)
                    .created(created)
                    .lastUpdated(lastUpdated)
                    .event(event)
                    .likedUsers(likedUsers)
                    .build();
            postRepository.save(post);
            latch.countDown();
            log.info("Generated post {}/{}: {}", number - latch.getCount(), number, title);
        }
//        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDateTime randomPastTime(int monthsBefore) {
        return randomTime(LocalDateTime.now().minusMonths(monthsBefore), LocalDateTime.now());
    }

    private LocalDateTime randomTime(LocalDateTime from, LocalDateTime to) {
        long minEpoch = from.toEpochSecond(ZoneOffset.UTC) * 1000;
        long maxEpoch = to.toEpochSecond(ZoneOffset.UTC) * 1000;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(random.nextLong(minEpoch, maxEpoch)), ZoneOffset.UTC);
    }
}

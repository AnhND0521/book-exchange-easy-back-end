package itss.group22.bookexchangeeasy.controller;

import itss.group22.bookexchangeeasy.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

}

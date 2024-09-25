package itss.group22.bookexchangeeasy.controller;

import itss.group22.bookexchangeeasy.dto.book.CategoryDTO;
import itss.group22.bookexchangeeasy.dto.book.GetCategoriesRequest;
import itss.group22.bookexchangeeasy.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getCategories(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false) int page,
            @RequestParam(name = "size", required = false) int size
    ) {
        var request = GetCategoriesRequest.builder()
                .name(name)
                .pageable(PageRequest.of(page, size))
                .build();
        return ResponseEntity.ok(categoryService.getCategories(request));
    }
}

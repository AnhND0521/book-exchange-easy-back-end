package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.book.CategoryDTO;
import itss.group22.bookexchangeeasy.dto.book.GetCategoriesRequest;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Page<CategoryDTO> getCategories(GetCategoriesRequest request);
}

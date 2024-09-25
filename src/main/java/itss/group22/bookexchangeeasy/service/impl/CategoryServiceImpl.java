package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.book.CategoryDTO;
import itss.group22.bookexchangeeasy.dto.book.GetCategoriesRequest;
import itss.group22.bookexchangeeasy.entity.Category;
import itss.group22.bookexchangeeasy.repository.CategoryRepository;
import itss.group22.bookexchangeeasy.repository.specification.QueryOperator;
import itss.group22.bookexchangeeasy.repository.specification.SpecificationBuilder;
import itss.group22.bookexchangeeasy.repository.specification.SpecificationFilter;
import itss.group22.bookexchangeeasy.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Page<CategoryDTO> getCategories(GetCategoriesRequest request) {
        List<SpecificationFilter> filters = new ArrayList<>();
        if (Objects.nonNull(request.getName())) {
            filters.add(SpecificationFilter.builder()
                    .field("name")
                    .operator(QueryOperator.LIKE)
                    .value(request.getName())
                    .build());
        }
        SpecificationBuilder<Category> specificationBuilder = new SpecificationBuilder<>();
        Specification<Category> specification = specificationBuilder.combineAndSpecifications(filters);
        return categoryRepository.findAll(specification, request.getPageable())
                .map(c -> CategoryDTO.builder()
                        .id(c.getId())
                        .categoryName(c.getName())
                        .build());
    }
}

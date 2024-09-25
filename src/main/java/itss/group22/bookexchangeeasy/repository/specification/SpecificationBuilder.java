package itss.group22.bookexchangeeasy.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SpecificationBuilder<T> {
    public Specification<T> createSpecification(SpecificationFilter input) {
        return switch (input.getOperator()) {
            case EQUALS -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(input.getField()), input.getValue());
            case NOT_EQUALS -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.notEqual(root.get(input.getField()), input.getValue());
            case GREATER_THAN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.gt(root.get(input.getField()), (Number) input.getValue());
            case LESS_THAN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lt(root.get(input.getField()), (Number) input.getValue());
            case LIKE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(input.getField().toLowerCase()), "%" + input.getValue().toString().toLowerCase() + "%");
            case IN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.in(root.get(input.getField())).value(input.getValues());
            default -> throw new RuntimeException("Operation not supported yet");
        };
    }

    public Specification<T> combineAndSpecifications(List<SpecificationFilter> filters) {
        if (filters.isEmpty())
            return null;

        Specification<T> specification = createSpecification(filters.get(0));
        for (int i = 1; i < filters.size(); i++) {
            specification = specification.and(createSpecification(filters.get(i)));
        }
        return specification;
    }

    public Specification<T> combineOrSpecifications(List<SpecificationFilter> filters) {
        if (filters.isEmpty())
            return null;

        Specification<T> specification = createSpecification(filters.get(0));
        for (int i = 1; i < filters.size(); i++) {
            specification = specification.or(createSpecification(filters.get(i)));
        }
        return specification;
    }
}

package exercise.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import exercise.dto.ProductParamsDTO;
import exercise.model.Product;


import java.util.ArrayList;
import java.util.List;

// BEGIN
@Component
public class ProductSpecification {

    public static Specification<Product> getByParams(ProductParamsDTO params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), params.getCategoryId()));
            }

            if (params.getPriceLt() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("price"), params.getPriceLt()));
            }

            if (params.getPriceGt() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("price"), params.getPriceGt()));
            }

            if (params.getRatingGt() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("rating"), params.getRatingGt()));
            }

            if (params.getTitleCont() != null && !params.getTitleCont().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + params.getTitleCont().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
// END

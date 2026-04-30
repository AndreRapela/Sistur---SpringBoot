package br.gov.noronha.sistur.repository.specification;

import br.gov.noronha.sistur.modules.tourism.model.TouristPoint;
import org.springframework.data.jpa.domain.Specification;

public class TouristPointSpecifications {

    public static Specification<TouristPoint> hasCategory(String category) {
        return (root, query, cb) -> (category == null || category.isBlank() || category.equalsIgnoreCase("Todos"))
            ? cb.conjunction()
            : cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<TouristPoint> searchByNameOrDescription(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + term.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("description")), pattern),
                cb.like(cb.lower(root.get("location")), pattern)
            );
        };
    }
}
package br.gov.noronha.sistur.repository.specification;

import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import org.springframework.data.jpa.domain.Specification;

public class EstablishmentSpecifications {

    public static Specification<Establishment> hasType(EstablishmentType type) {
        return (root, query, cb) -> type == null ? cb.conjunction() : cb.equal(root.get("type"), type);
    }

    public static Specification<Establishment> hasCategory(String category) {
        return (root, query, cb) -> (category == null || category.equalsIgnoreCase("Todos")) 
            ? cb.conjunction() 
            : cb.like(cb.lower(root.get("foodType")), "%" + category.toLowerCase() + "%");
    }

    public static Specification<Establishment> searchByNameOrDescription(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isEmpty()) return cb.conjunction();
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }
}

package net.codejava.repository.criteria;

import java.util.function.Consumer;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteriaConsumer implements Consumer<SearchCriteria> {
    private CriteriaBuilder builder;
    private Predicate predicate;
    private Root root;

    @Override
    public void accept(SearchCriteria param) {
        if (param.getOperation().equals(">")) {
            predicate = builder.and(
                    predicate,
                    builder.greaterThanOrEqualTo(
                            root.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equals("<")) {
            predicate = builder.and(
                    predicate,
                    builder.lessThanOrEqualTo(
                            root.get(param.getKey()), param.getValue().toString()));
        } else {
            if (root.get(param.getKey()).getJavaType() == String.class)
                predicate = builder.and(
                        predicate,
                        builder.like(
                                root.get(param.getKey()), "%" + param.getValue().toString() + "%"));
            else
                predicate = builder.and(
                        predicate,
                        builder.equal(root.get(param.getKey()), param.getValue().toString()));
        }
    }
}

package net.codejava.repository.criteria;

import lombok.Builder;
import net.codejava.domain.dto.meta.SortingDTO;

@Builder
public record PageCriteria<T>(
        Long totalItems, Integer totalPages, Integer currentPage, Integer pageSize, SortingDTO sorting, T item) {}

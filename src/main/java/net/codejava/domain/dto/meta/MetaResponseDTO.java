package net.codejava.domain.dto.meta;

import lombok.Builder;

@Builder
public record MetaResponseDTO(
        Integer totalItems, Integer totalPages, Integer currentPage, Integer pageSize, SortingDTO sorting) {}

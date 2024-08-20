package net.codejava.domain.dto.meta;

import java.util.Objects;

import org.springframework.web.bind.annotation.RequestParam;

import net.codejava.constant.MetaConstant;

public record MetaRequestDTO(
        @RequestParam(name = "currentPage", required = false) Integer currentPage,
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "sortField", required = false) String sortField,
        @RequestParam(name = "sortDir", required = false) String sortDir,
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "search", required = false, defaultValue = "") String... search) {
    public Integer currentPage() {
        return Objects.requireNonNullElse(this.currentPage, MetaConstant.Pagination.DEFAULT_CURRENT_PAGE);
    }

    public Integer pageSize() {
        return Objects.requireNonNullElse(this.pageSize, MetaConstant.Pagination.DEFAULT_PAGE_SIZE);
    }

    public String sortField() {
        return Objects.requireNonNullElse(this.sortField, MetaConstant.Sorting.DEFAULT_FIELD);
    }

    public String sortDir() {
        return Objects.requireNonNullElse(this.sortDir, MetaConstant.Sorting.DEFAULT_DIRECTION);
    }
}

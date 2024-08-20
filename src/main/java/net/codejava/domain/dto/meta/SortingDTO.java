package net.codejava.domain.dto.meta;

import java.util.Objects;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Builder;
import net.codejava.constant.MetaConstant;

@Builder
public record SortingDTO(
        @RequestParam(name = "sortField", required = false) String sortField,
        @RequestParam(name = "sortDir", required = false) String sortDir) {
    public String sortField() {
        return Objects.requireNonNullElse(this.sortField, MetaConstant.Sorting.DEFAULT_FIELD);
    }

    public String sortDir() {
        return Objects.requireNonNullElse(this.sortDir, MetaConstant.Sorting.DEFAULT_DIRECTION);
    }
}

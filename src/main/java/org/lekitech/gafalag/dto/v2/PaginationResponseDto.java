package org.lekitech.gafalag.dto.v2;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponseDto<T, E> {
    private int totalItems;
    private int totalPages;
    private int pageSize;
    private int currentPage;
    private List<E> items;

    public PaginationResponseDto(Page<T> page, List<E> elements) {
        this.totalItems = (int) page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.currentPage = page.getNumber();
        this.items = elements;
    }
}

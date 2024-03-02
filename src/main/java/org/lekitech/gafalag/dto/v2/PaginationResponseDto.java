package org.lekitech.gafalag.dto.v2;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This class represents a pagination response containing a list of DTO objects and pagination information.
 * The generic type <E> represents an Entity object retrieved from the repository (database),
 * while the generic type <D> represents a DTO object that is returned to the frontend via controllers.
 * <p>
 * - totalItems:  Represents the total number of items available for pagination.
 * - totalPages:  Indicates the total number of pages based on the pagination logic.
 * - pageSize:    Specifies the number of items displayed per page.
 * - currentPage: Indicates the current page being displayed.
 * - items:       Contains the list of DTO objects to be displayed on the current page.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponseDto<E, D> {
    private int totalItems;
    private int totalPages;
    private int pageSize;
    private int currentPage;
    private List<D> items;

    public PaginationResponseDto(Page<E> page, List<D> itemsDto) {
        this.totalItems = (int) page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.currentPage = page.getNumber();
        this.items = itemsDto;
    }
}

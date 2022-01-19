package org.lekitech.gafalag.dto;

import java.util.List;

public record PaginatedResult <T> (
        long totalItems,
        long totalPages,
        int currentPage,
        int pageSize,
        List<T> items
) { }

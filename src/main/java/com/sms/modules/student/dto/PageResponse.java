package com.sms.modules.student.dto;

import java.util.List;

import org.springframework.data.domain.Page;

/** Stable page response shape used by the Angular client. */
public record PageResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int size,
        int number) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber());
    }
}

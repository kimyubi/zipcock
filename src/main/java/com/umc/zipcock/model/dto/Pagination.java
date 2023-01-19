package com.umc.zipcock.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Pagination {

    // 전체 페이지 수
    private Integer totalPages;

    // 전체 데이터 수
    private Long totalElements;

    // 현재 페이지
    private Integer currentPage;

    // 현재 페이지에 나올 데이터 수
    private Integer currentElements;

    public Pagination(Page<?> page){
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.currentElements = page.getNumberOfElements();
    }

}

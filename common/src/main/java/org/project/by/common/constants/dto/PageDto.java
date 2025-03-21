package org.project.by.common.constants.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageDto<T> {

    private List<T> content;

    private int totalPages;

    private long totalElements;

    private int pageNumber;

    private int pageSize;

    public PageDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
    }

    public static <T> PageDto<T> empty() {
        return new PageDto<>(Page.empty());
    }

    public boolean isEmpty() {
        return this.content == null || this.content.isEmpty();
    }

}

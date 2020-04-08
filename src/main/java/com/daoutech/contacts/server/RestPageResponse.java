package com.daoutech.contacts.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class RestPageResponse<T> {

    private List<T> content;
    private Long totalElements;
    private int size;
    private int number;
    private List<Sort.Order> orders;

    public RestPageResponse(Page<T> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.number = page.getNumber();
        this.orders = page.getSort().toList();
    }
}

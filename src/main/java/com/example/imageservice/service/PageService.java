package com.example.imageservice.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PageService<T> {
    Page<T> getPageableResult(Pageable pageable, List<T> result);
}

package com.example.imageservice.service.impl;

import com.example.imageservice.service.PageService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl<T> implements PageService<T> {
    @Override
    public Page<T> getPageableResult(Pageable pageable, List<T> result) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), result.size());
        return new PageImpl<>(result.subList(start, end),
                pageable, result.size());
    }
}

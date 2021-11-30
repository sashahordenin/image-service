package com.example.imageservice.repository;

import com.example.imageservice.model.Image;
import java.util.List;
import java.util.Map;

public interface CustomImageRepository {
    List<Image> findAll(Map<String, List<Object>> params);
}

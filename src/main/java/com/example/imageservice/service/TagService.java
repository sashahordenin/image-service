package com.example.imageservice.service;

import com.example.imageservice.model.Tag;
import java.util.List;

public interface TagService {
    Tag add(Tag tag);

    Tag get(Long id);

    List<Tag> getAll();

    Tag update(Tag tag);

    void delete(Long id);

    Tag findByName(String name);
}

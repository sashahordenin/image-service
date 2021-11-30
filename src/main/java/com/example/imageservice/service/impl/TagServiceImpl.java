package com.example.imageservice.service.impl;

import com.example.imageservice.exception.DataProcessingException;
import com.example.imageservice.model.Tag;
import com.example.imageservice.repository.TagRepository;
import com.example.imageservice.service.TagService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag add(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag get(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new DataProcessingException(
                        "Can't find tag by id: "
                                + id));
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag update(Tag tag) {
        tagRepository.findById(tag.getId()).orElseThrow(
                () -> new DataProcessingException("Can't find tag by id: "
                        + tag.getId()));
        return tagRepository.save(tag);
    }

    @Override
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name).orElseThrow(
                () -> new DataProcessingException("Can't find tag by name: "
                        + name));
    }

}

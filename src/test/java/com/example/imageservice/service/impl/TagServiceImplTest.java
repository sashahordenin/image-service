package com.example.imageservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.imageservice.exception.DataProcessingException;
import com.example.imageservice.model.Tag;
import com.example.imageservice.repository.TagRepository;
import com.example.imageservice.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class TagServiceImplTest {
    private TagService tagService;
    private TagRepository tagRepository;
    private Tag goodTag;
    private Tag nullTag;
    private Long goodId;
    private Long nullId;
    private List<Tag> goodList;
    private Tag pandaTag;
    private List<Tag> emptyList;

    @BeforeEach
    void setUp() {
        tagRepository = mock(TagRepository.class);
        tagService = new TagServiceImpl(tagRepository);
        goodTag = new Tag();
        goodTag.setId(1L);
        goodTag.setName("animal");
        nullTag = null;
        goodId = 1L;
        nullId = null;
        goodList = new ArrayList<>();
        goodList.add(goodTag);
        pandaTag = new Tag();
        pandaTag.setId(2L);
        pandaTag.setName("panda");
        goodList.add(pandaTag);
        emptyList = new ArrayList<>();
    }

    @Test
    void add_Ok() {
        when(tagRepository.save(goodTag)).thenReturn(goodTag);
        Tag actual = tagService.add(goodTag);
        Assertions.assertNotNull(actual);
        assertEquals(goodTag, actual);
    }

    @Test
    void add_null_NotOk() {
        when(tagRepository.save(nullTag)).thenThrow(IllegalArgumentException.class);
        try {
            tagService.add(nullTag);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected to receive IllegalArgumentException");
    }

    @Test
    void get_Ok() {
        when(tagRepository.findById(goodId)).thenReturn(Optional.of(goodTag));
        Tag actual = tagService.get(goodId);
        Assertions.assertNotNull(actual);
        assertEquals(goodTag, actual);
    }

    @Test
    void get_notExist_NotOk() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(goodTag));
        when(tagRepository.findById(2L)).thenThrow(DataProcessingException.class);
        try {
            Tag actual = tagService.get(2L);
            Assertions.assertNotNull(actual);
            assertEquals(goodTag, actual);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void get_null_NotOk() {
        when(tagRepository.findById(nullId)).thenThrow(IllegalArgumentException.class);
        try {
            tagService.get(nullId);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected to receive IllegalArgumentException");
    }

    @Test
    void getAll_Ok() {
        when(tagRepository.findAll()).thenReturn(goodList);
        List<Tag> actual = tagService.getAll();
        Assertions.assertNotNull(actual);
        assertEquals(goodList.size(), actual.size());
    }

    @Test
    void getAll_notFound_Ok() {
        when(tagRepository.findAll()).thenReturn(emptyList);
        List<Tag> actual = tagService.getAll();
        Assertions.assertNotNull(actual);
        assertEquals(emptyList.size(), actual.size());
    }

    @Test
    void update_Ok() {
        when(tagRepository.findById(pandaTag.getId())).thenReturn(Optional.of(pandaTag));
        when(tagRepository.save(pandaTag)).thenReturn(pandaTag);
        Tag actual = tagService.update(pandaTag);
        Assertions.assertNotNull(actual);
        assertEquals(pandaTag.getName(), actual.getName());
    }

    @Test
    void update_notExist_NotOk() {
        when(tagRepository.findById(pandaTag.getId())).thenThrow(DataProcessingException.class);
        try {
            Tag actual = tagService.update(pandaTag);
            Assertions.assertNotNull(actual);
            assertEquals(pandaTag, actual);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void delete_Ok() {
        doNothing().doThrow(new IllegalArgumentException()).when(tagRepository).deleteById(pandaTag.getId());
        tagService.delete(pandaTag.getId());
        try {
            tagService.delete(pandaTag.getId());
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected to receive IllegalArgumentException");
    }

    @Test
    void findByName_Ok() {
        when(tagRepository.findByName(pandaTag.getName())).thenReturn(Optional.of(pandaTag));
        Tag actual = tagService.findByName(pandaTag.getName());
        Assertions.assertNotNull(actual);
        assertEquals(pandaTag.getName(), actual.getName());
    }

    @Test
    void findByName_notExist_notOk() {
        when(tagRepository.findByName(pandaTag.getName())).thenThrow(DataProcessingException.class);
        try {
            Tag actual = tagService.findByName(pandaTag.getName());
            Assertions.assertNotNull(actual);
            assertEquals(pandaTag.getName(), actual.getName());
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}

package com.example.imageservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.imageservice.exception.DataProcessingException;
import com.example.imageservice.model.Account;
import com.example.imageservice.model.Image;
import com.example.imageservice.repository.CustomImageRepository;
import com.example.imageservice.repository.CustomImageRepositoryImpl;
import com.example.imageservice.repository.ImageRepository;
import com.example.imageservice.service.ImageService;
import java.security.AccessControlException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


class ImageServiceImplTest {
    private ImageRepository imageRepository;
    private CustomImageRepository customImageRepository;
    private ImageService imageService;
    private Image goodImage;
    private Image gardenImage;
    private Image buildingImage;
    private Account admin;
    private Account user;
    private List<Image> emptyList;
    private Map<String, List<Object>> params;
    private Image nullImage;
    private Long nullId;

    @BeforeEach
    void setUp() {
        imageRepository = Mockito.mock(ImageRepository.class);
        customImageRepository = Mockito.mock(CustomImageRepositoryImpl.class);
        imageService = new ImageServiceImpl(imageRepository, customImageRepository);
        goodImage = new Image();
        goodImage.setId(1L);
        goodImage.setName("photo.jpg");
        goodImage.setContentType("image/jpeg");
        goodImage.setImageSize(44593459);
        admin = new Account();
        admin.setId(1L);
        admin.setRole("ADMIN");
        user = new Account();
        user.setId(2L);
        user.setRole("USER");
        emptyList = new ArrayList<>();
        gardenImage = new Image();
        gardenImage.setId(2L);
        gardenImage.setName("photo2.jpg");
        gardenImage.setImageSize(445934554);
        buildingImage = new Image();
        buildingImage.setId(3L);
        buildingImage.setName("photo3.jpg");
        buildingImage.setImageSize(5346453);
        goodImage.setContentType("image/jpeg");
        params = new HashMap<>();
        params.put("name", List.of("photo.jpg", "photo2.jpg", "photo3.jpg"));
        nullImage = null;
        nullId = null;
    }

    @Test
    void add_Ok() {
        when(imageRepository.save(goodImage)).thenReturn(goodImage);
        Image actual = imageService.add(goodImage, admin);
        Assertions.assertNotNull(actual);
        assertEquals(goodImage, actual);
    }

    @Test
    void add_nullImage_NotOk() {
        when(imageRepository.save(nullImage)).thenThrow(NullPointerException.class);
        try {
            imageService.add(nullImage, admin);
        } catch (NullPointerException e) {
            return;
        }
        Assertions.fail("Expected to receive NullPointerException");
    }

    @Test
    void add_nullAccount_Ok() {
        when(imageRepository.save(goodImage)).thenReturn(goodImage);
        Image actual = imageService.add(goodImage, admin);
        Assertions.assertNotNull(actual);
        assertEquals(goodImage, actual);
    }

    @Test
    void get_Ok() {
        when(imageRepository.findById(goodImage.getId())).thenReturn(Optional.of(goodImage));
        Image actual = imageService.get(goodImage.getId());
        Assertions.assertNotNull(actual);
        assertEquals(goodImage, actual);
    }

    @Test
    void get_notExist_NotOk() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(goodImage));
        when(imageRepository.findById(2L)).thenThrow(DataProcessingException.class);
        try {
            Image actual = imageService.get(2L);
            Assertions.assertNotNull(actual);
            assertEquals(goodImage, actual);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void get_null_NotOk() {
        when(imageRepository.findById(nullId)).thenThrow(IllegalArgumentException.class);
        try {
            imageService.get(null);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected to receive IllegalArgumentException");
    }

    @Test
    void getAll_Ok() {
        when(imageRepository.findAll()).thenReturn(List.of(goodImage));
        List<Image> actual = imageService.getAll();
        Assertions.assertNotNull(actual);
        assertEquals(List.of(goodImage), actual);
    }

    @Test
    void getAll_notFound_Ok() {
        when(imageRepository.findAll()).thenReturn(emptyList);
        List<Image> actual = imageService.getAll();
        Assertions.assertNotNull(actual);
        assertEquals(emptyList.size(), actual.size());
    }

    @Test
    void update_Ok() {
        when(imageRepository.findById(goodImage.getId())).thenReturn(Optional.of(goodImage));
        when(imageRepository.save(goodImage)).thenReturn(goodImage);
        Image actual = imageService.update(goodImage, admin);
        Assertions.assertNotNull(actual);
        assertEquals(goodImage.getName(), actual.getName());
    }

    @Test
    void update_notExist_NotOk() {
        when(imageRepository.findById(goodImage.getId())).thenThrow(DataProcessingException.class);
        try {
            Image actual = imageService.update(goodImage, admin);
            Assertions.assertNotNull(actual);
            assertEquals(goodImage, actual);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void delete_accountOwner_Ok() {
        goodImage.setAccount(admin);
        when(imageRepository.findById(goodImage.getId())).thenReturn(Optional.of(goodImage));
        doNothing().doThrow(new IllegalArgumentException()).when(imageRepository).deleteById(goodImage.getId());
        imageService.delete(goodImage.getId(), admin);
        try {
            imageService.delete(goodImage.getId(), admin);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected to receive IllegalArgumentException");
    }

    @Test
    void delete_accountNotOwner_NotOk() {
        goodImage.setAccount(user);
        when(imageRepository.findById(goodImage.getId())).thenReturn(Optional.of(goodImage));
        doNothing().doThrow(new AccessControlException("Access on delete has only owner"))
                .when(imageRepository).deleteById(goodImage.getId());
        try {
            imageService.delete(goodImage.getId(), admin);
        } catch (AccessControlException e) {
            return;
        }
        Assertions.fail("Expected to receive AccessControlException");
    }

    @Test
    void delete_imageNotExist_NotOk() {
        when(imageRepository.findById(goodImage.getId())).thenThrow(DataProcessingException.class);
        try {
            imageService.delete(goodImage.getId(), admin);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findAll_byNames_Ok() {
        List<Image> expected = List.of(goodImage, gardenImage, buildingImage);
        when(customImageRepository.findAll(params)).thenReturn(expected);
        List<Image> actual = imageService.findAll(params);
        Assertions.assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void findAll_byNamesAndId_Ok() {
        params.put("id", List.of(1L));
        List<Image> expected = List.of(goodImage);
        when(customImageRepository.findAll(params)).thenReturn(expected);
        List<Image> actual = imageService.findAll(params);
        Assertions.assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void findAll_emptyParamsMap_Ok() {
        params.remove("name");
        when(customImageRepository.findAll(params)).thenThrow(DataProcessingException.class);
        try {
            List<Image> actual = imageService.findAll(params);
            Assertions.assertNotNull(actual);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}

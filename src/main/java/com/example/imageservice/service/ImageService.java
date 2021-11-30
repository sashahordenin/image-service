package com.example.imageservice.service;

import com.example.imageservice.model.Account;
import com.example.imageservice.model.Image;
import java.util.List;
import java.util.Map;

public interface ImageService {
    Image add(Image image, Account account);

    Image get(Long id);

    List<Image> getAll();

    Image update(Image image, Account currentAccount);

    void delete(Long id, Account currentAccount);

    List<Image> findAll(Map<String, List<Object>> params);
}

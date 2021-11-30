package com.example.imageservice.service.impl;

import com.example.imageservice.exception.DataProcessingException;
import com.example.imageservice.model.Account;
import com.example.imageservice.model.Image;
import com.example.imageservice.repository.CustomImageRepository;
import com.example.imageservice.repository.ImageRepository;
import com.example.imageservice.service.ImageService;
import java.security.AccessControlException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final CustomImageRepository customImageRepository;

    public ImageServiceImpl(ImageRepository imageRepository,
                            @Qualifier("customImageRepositoryImpl")
                                    CustomImageRepository customImageRepository) {
        this.imageRepository = imageRepository;
        this.customImageRepository = customImageRepository;
    }

    @Override
    public Image add(Image image, Account account) {
        image.setAccount(account);
        image.setLink(getLink(image));
        return imageRepository.save(image);
    }

    @Override
    public Image get(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new DataProcessingException(
                        "Can't find image by id: "
                        + id));
    }

    @Override
    public List<Image> getAll() {
        return imageRepository.findAll();
    }

    @Override
    public Image update(Image currentImage, Account currentAccount) {
        Image image = imageRepository.findById(currentImage.getId()).orElseThrow(
                () -> new DataProcessingException("Can't find image by id: "
                + currentImage.getId()));
        currentImage.setAccount(currentAccount);
        currentImage.setLink(getLink(currentImage));
        if (currentImage.getTags() == null) {
            currentImage.setTags(image.getTags());
        }
        currentImage.setCreationDate(image.getCreationDate());
        if (image.getAccount().equals(currentAccount)) {
            return imageRepository.save(currentImage);
        }
        throw new AccessControlException("Access on update has only owner");
    }

    @Override
    public void delete(Long id, Account currentAccount) {
        Account account = imageRepository.findById(id)
                .orElseThrow(() -> new DataProcessingException("Can't find image by id: "
                + id)).getAccount();
        if (account.getId().equals(currentAccount.getId())
                || account.getRole().equals("ADMIN")) {
            imageRepository.deleteById(id);
            return;
        }
        throw new AccessControlException("Access on delete has only owner");
    }

    @Override
    public List<Image> findAll(Map<String, List<Object>> params) {
        return customImageRepository.findAll(params);
    }

    private String getLink(Image image) {
        return String.format("http://localhost:8080/images/%s", image.getName());
    }
}

package com.example.imageservice.controller;

import com.example.imageservice.dto.request.ImageRequestDto;
import com.example.imageservice.dto.response.ImageResponseDto;
import com.example.imageservice.model.Account;
import com.example.imageservice.model.Image;
import com.example.imageservice.service.AccountService;
import com.example.imageservice.service.ImageService;
import com.example.imageservice.service.PageService;
import com.example.imageservice.service.mapper.ImageMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    private final ImageMapper imageMapper;
    private final AccountService accountService;
    private final PageService<ImageResponseDto> pageService;

    public ImageController(ImageService imageService,
                           ImageMapper imageMapper,
                           AccountService accountService,
                           PageService<ImageResponseDto> pageService) {
        this.imageService = imageService;
        this.imageMapper = imageMapper;
        this.accountService = accountService;
        this.pageService = pageService;
    }

    @PostMapping
    public Page<ImageResponseDto> addAll(Pageable pageable,
                                         @RequestPart("image") List<MultipartFile> images,
                                         Authentication auth) {
        return pageService.getPageableResult(pageable, images.stream()
                .map(i -> imageMapper
                        .mapToDto(imageService.add(imageMapper.mapUploadedToModel(i),
                                getAccountInformation(auth))))
                .collect(Collectors.toList()));
    }

    @GetMapping
    public Page<ImageResponseDto> getAll(Pageable pageable) {
        return pageService.getPageableResult(pageable, imageService.getAll()
                .stream()
                .map(imageMapper::mapToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ImageResponseDto get(@PathVariable Long id) {
        return imageMapper.mapToDto(imageService.get(id));
    }

    @PutMapping("/{id}")
    public ImageResponseDto update(@PathVariable Long id,
                                    @RequestBody ImageRequestDto imageRequestDto,
                                   Authentication auth) {
        Image image = imageMapper.mapToModel(imageRequestDto);
        image.setId(id);
        return imageMapper.mapToDto(imageService.update(image, getAccountInformation(auth)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        imageService.delete(id, getAccountInformation(auth));
    }

    @GetMapping("/search")
    public Page<ImageResponseDto> search(Pageable pageable,
            @RequestParam Map<String, String> params) {
        Map<String, List<Object>> map = imageMapper.mapToMap(params);
        List<ImageResponseDto> result = imageService.findAll(map)
                .stream()
                .map(imageMapper::mapToDto)
                .collect(Collectors.toList());
        return pageService.getPageableResult(pageable, result);
    }

    private Account getAccountInformation(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return accountService.findByEmail(userDetails.getUsername());
    }
}


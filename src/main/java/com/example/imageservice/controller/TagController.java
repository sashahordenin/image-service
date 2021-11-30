package com.example.imageservice.controller;

import com.example.imageservice.dto.request.TagRequestDto;
import com.example.imageservice.dto.response.ImageResponseDto;
import com.example.imageservice.dto.response.TagResponseDto;
import com.example.imageservice.model.Account;
import com.example.imageservice.model.Image;
import com.example.imageservice.model.Tag;
import com.example.imageservice.service.AccountService;
import com.example.imageservice.service.ImageService;
import com.example.imageservice.service.PageService;
import com.example.imageservice.service.TagService;
import com.example.imageservice.service.mapper.ImageMapper;
import com.example.imageservice.service.mapper.TagMapper;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;
    private final ImageService imageService;
    private final ImageMapper imageMapper;
    private final AccountService accountService;
    private final PageService<TagResponseDto> pageService;

    public TagController(TagService tagService, TagMapper tagMapper,
                         ImageService imageService,
                         ImageMapper imageMapper,
                         AccountService accountService,
                         PageService<TagResponseDto> pageService) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
        this.imageService = imageService;
        this.imageMapper = imageMapper;
        this.accountService = accountService;
        this.pageService = pageService;
    }

    @PostMapping
    public TagResponseDto add(@RequestBody TagRequestDto tagRequestDto) {
        return tagMapper.mapToDto(tagService
                .add(tagMapper.mapToModel(tagRequestDto)));
    }

    @GetMapping
    public Page<TagResponseDto> getAll(Pageable pageable) {
        return pageService.getPageableResult(pageable, tagService.getAll()
                .stream()
                .map(tagMapper::mapToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public TagResponseDto get(@PathVariable Long id) {
        return tagMapper.mapToDto(tagService.get(id));
    }

    @PutMapping("/{id}")
    public TagResponseDto update(@PathVariable Long id,
                                     @RequestBody TagRequestDto tagRequestDto) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(tagRequestDto.getName());
        return tagMapper.mapToDto(tagService.update(tag));
    }

    @PutMapping("/add/by-image/{id}")
    public ImageResponseDto addTag(@PathVariable Long id,
                                   @RequestBody TagRequestDto tagRequestDto,
                                   Authentication auth) {
        Image image = imageService.get(id);
        image.getTags().add(tagService.add(tagMapper.mapToModel(tagRequestDto)));
        return imageMapper.mapToDto(imageService.update(image, getAccountInformation(auth)));
    }

    @PutMapping("/delete/by-image/{id}")
    public ImageResponseDto deleteTag(@PathVariable Long id,
                                   @RequestBody TagRequestDto tagRequestDto,
                                   Authentication auth) {
        Image image = imageService.get(id);
        image.getTags().remove(tagService.findByName(tagRequestDto.getName()));
        image.setTags(image.getTags());
        return imageMapper.mapToDto(imageService.update(image, getAccountInformation(auth)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }

    private Account getAccountInformation(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return accountService.findByEmail(userDetails.getUsername());
    }
}

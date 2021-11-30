package com.example.imageservice.service.mapper;

import com.example.imageservice.dto.request.ImageRequestDto;
import com.example.imageservice.dto.response.ImageResponseDto;
import com.example.imageservice.model.Image;
import com.example.imageservice.model.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageMapper implements RequestDtoMapper<ImageRequestDto, Image>,
        ResponseDtoMapper<ImageResponseDto, Image> {
    @Override
    public Image mapToModel(ImageRequestDto dto) {
        Image image = new Image();
        image.setName(dto.getName());
        image.setContentType(dto.getContentType());
        image.setImageSize(dto.getImageSize());
        return image;
    }

    @Override
    public ImageResponseDto mapToDto(Image image) {
        ImageResponseDto imageResponseDto = new ImageResponseDto();
        imageResponseDto.setId(image.getId());
        imageResponseDto.setName(image.getName());
        imageResponseDto.setContentType(image.getContentType());
        imageResponseDto.setImageSize(image.getImageSize());
        imageResponseDto.setLink(image.getLink());
        imageResponseDto.setCreationDateTime(image.getCreationDate().toString());
        imageResponseDto.setUpdateDateTime(image.getUpdateDate().toString());
        if (image.getTags() != null) {
            imageResponseDto.setTags(image.getTags()
                    .stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet()));
        }
        return imageResponseDto;
    }

    public Image mapUploadedToModel(MultipartFile multipartFile) {
        Image image = new Image();
        image.setName(multipartFile.getOriginalFilename());
        image.setContentType(multipartFile.getContentType());
        image.setImageSize(multipartFile.getSize());
        return image;
    }

    public Map<String, List<Object>> mapToMap(Map<String,String> params) {
        Map<String, List<Object>> diffTypesMap = new HashMap<>();
        params.remove("page");
        params.remove("size");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String[] stringsValues = entry.getValue().split(",");
            List<Object> values = new ArrayList<>();
            for (String value : stringsValues) {
                if (key.equals("id") || key.equals("imageSize")) {
                    values.add(Long.parseLong(value));
                } else {
                    values.add(value);
                }
            }
            diffTypesMap.put(key, values);
        }
        return diffTypesMap;
    }
}

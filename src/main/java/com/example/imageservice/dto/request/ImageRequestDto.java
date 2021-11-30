package com.example.imageservice.dto.request;

import lombok.Data;

@Data
public class ImageRequestDto {
    private String name;
    private String contentType;
    private long imageSize;
}

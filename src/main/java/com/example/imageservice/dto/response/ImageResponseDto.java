package com.example.imageservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ImageResponseDto {
    private Long id;
    private String name;
    private String contentType;
    private long imageSize;
    private String link;
    private Set<String> tags;
    private String creationDateTime;
    private String updateDateTime;
}

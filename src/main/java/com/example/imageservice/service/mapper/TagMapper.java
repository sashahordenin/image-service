package com.example.imageservice.service.mapper;

import com.example.imageservice.dto.request.TagRequestDto;
import com.example.imageservice.dto.response.TagResponseDto;
import com.example.imageservice.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper implements ResponseDtoMapper<TagResponseDto, Tag>,
        RequestDtoMapper<TagRequestDto, Tag> {
    @Override
    public Tag mapToModel(TagRequestDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());
        return tag;
    }

    @Override
    public TagResponseDto mapToDto(Tag tag) {
        TagResponseDto tagResponseDto = new TagResponseDto();
        tagResponseDto.setId(tag.getId());
        tagResponseDto.setName(tag.getName());
        return tagResponseDto;
    }
}

package com.example.imageservice.service.mapper;

public interface RequestDtoMapper<D, T> {
    T mapToModel(D dto);
}

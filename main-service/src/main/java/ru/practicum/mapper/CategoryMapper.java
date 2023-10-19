package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends Mappable<Category, CategoryDto> {

    @Override
    Category toEntity(CategoryDto dto);

    @Override
    CategoryDto toDto(Category entity);
}

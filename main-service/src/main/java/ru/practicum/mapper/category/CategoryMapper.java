package ru.practicum.mapper.category;

import org.mapstruct.Mapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.mapper.Mappable;
import ru.practicum.model.category.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends Mappable<Category, CategoryDto> {

    @Override
    Category toEntity(CategoryDto dto);

    @Override
    CategoryDto toDto(Category entity);
}

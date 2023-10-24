package ru.practicum.service.category;

import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryPublicService {

    List<CategoryDto> findAllCategories(Integer from, Integer size);

    CategoryDto findCategoryById(Integer catId);

}

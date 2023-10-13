package ru.practicum.service.category;

import ru.practicum.dto.category.CategoryDto;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategoryById(Integer id);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer id);

}

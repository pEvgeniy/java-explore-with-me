package ru.practicum.service.category;

import ru.practicum.dto.category.CategoryDto;

public interface CategoryAdminService {

    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategoryById(Integer id);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer id);

}

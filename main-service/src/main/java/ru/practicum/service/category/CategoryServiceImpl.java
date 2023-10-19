package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        log.info("service. create category with body = {}", categoryDto);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Integer id) {
        categoryRepository.deleteById(id);
        log.info("service. delete category with id = {}", id);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id = %s not found", id)));
        category.setName(categoryDto.getName());
        log.info("service. delete category with body = {}", id);
        return categoryMapper.toDto(category);
    }
}

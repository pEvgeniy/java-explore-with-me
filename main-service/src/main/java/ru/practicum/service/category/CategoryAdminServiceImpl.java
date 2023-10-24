package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.exception.EntityConflictException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        log.info("service. create category with body = {}", category);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Integer id) {
        if (eventRepository.existsEventByCategoryId(id)) {
            throw new EntityConflictException(String.format("Some events depends on category with id = %s", id));
        }
        categoryRepository.deleteById(id);
        log.info("service. delete category with id = {}", id);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id = %s not found", id)));
        if (!categoryDto.getName().equals(category.getName()) &&
                categoryRepository.existsByName(categoryDto.getName())) {
            throw new EntityConflictException(String.format("Category with name = %s already exists", categoryDto.getName()));
        }
        category.setName(categoryDto.getName());
        log.info("service. delete category with body = {}", id);
        return categoryMapper.toDto(category);
    }
}

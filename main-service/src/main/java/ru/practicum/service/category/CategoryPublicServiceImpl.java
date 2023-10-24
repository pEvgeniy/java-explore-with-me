package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryPublicServiceImpl implements CategoryPublicService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAllCategories(Integer from, Integer size) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(from / size, size));
        log.info("service. found categories = {}", categories);
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findCategoryById(Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id = %s not found", catId)));
        log.info("service. found category = {}", category);
        return categoryMapper.toDto(category);
    }
}

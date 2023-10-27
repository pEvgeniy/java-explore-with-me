package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.compilation.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationsPublicServiceImpl implements CompilationsPublicService {

    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = pinned == null ?
                compilationRepository.findAll(PageRequest.of(from / size, size)).getContent() :
                compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));
        log.info("service. found compilations = {}", compilations);
        return compilations.stream()
                .map(compilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto findCompilationById(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Compilation with id = %s not found", compId)));
        log.info("service. compilation found = {}", compilation);
        return compilationMapper.toDto(compilation);
    }
}

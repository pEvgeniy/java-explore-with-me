package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationsPublicService {

    List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto findCompilationById(Integer compId);

}

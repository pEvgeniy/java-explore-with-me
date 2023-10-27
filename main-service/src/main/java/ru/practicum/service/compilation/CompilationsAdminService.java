package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequestDto;

public interface CompilationsAdminService {

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilationById(Integer compId);

    CompilationDto updateCompilationById(Integer compId,
                                         UpdateCompilationRequestDto updateCompilationRequestDto);

}

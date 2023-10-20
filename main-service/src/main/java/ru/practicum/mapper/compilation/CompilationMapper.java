package ru.practicum.mapper.compilation;

import org.mapstruct.Mapper;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.mapper.Mappable;
import ru.practicum.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper extends Mappable<Compilation, CompilationDto> {
    @Override
    Compilation toEntity(CompilationDto dto);

    @Override
    CompilationDto toDto(Compilation entity);
}

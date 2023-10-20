package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequestDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.compilation.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationsAdminServiceImpl implements CompilationsAdminService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .events(findEventsFromDto(compilationDto.getEvents()))
                .build();
        compilationRepository.save(compilation);
        log.info("service. created compilation = {}", compilation);
        return compilationMapper.toDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilationById(Integer compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Compilation with compId = %s not found", compId)));
        compilationRepository.deleteById(compId);
        log.info("service. deleted compilation with id = {}", compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationById(Integer compId, UpdateCompilationRequestDto updateRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Compilation with compId = %s not found", compId)));
        if (updateRequest.getPinned() != null) {
            compilation.setPinned(updateRequest.getPinned());
        }
        if (updateRequest.getTitle() != null) {
            compilation.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getEvents() != null) {
            compilation.setEvents(findEventsFromDto(updateRequest.getEvents()));
        }
        log.info("service. updated compilation with id = {}", compId);
        return compilationMapper.toDto(compilation);
    }

    private List<Event> findEventsFromDto(List<Integer> eventIds) {
        return eventIds.stream()
                .map(eventId -> eventRepository.findById(eventId)
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId))))
                .collect(Collectors.toList());
    }
}

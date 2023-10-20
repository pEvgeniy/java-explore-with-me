package ru.practicum.controller.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequestDto;
import ru.practicum.service.compilation.CompilationsAdminService;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationsAdminController {

    private final CompilationsAdminService compilationsService;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody NewCompilationDto compilationDto) {
        log.info("controller. post. /admin/compilations. create compilation with body = {}", compilationDto);
        return compilationsService.createCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable Integer compId) {
        log.info("controller. delete. /admin/compilations/{}. delete compilation", compId);
        compilationsService.deleteCompilationById(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilationById(@PathVariable Integer compId,
                                                @RequestBody UpdateCompilationRequestDto updateCompilationRequestDto) {
        log.info("controller. patch. /admin/compilations/{}. update compilation", compId);
        return compilationsService.updateCompilationById(compId, updateCompilationRequestDto);
    }


}

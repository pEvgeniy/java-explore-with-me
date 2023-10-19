package ru.practicum.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.dto.category.CategoryDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewEventDto {

    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotBlank
    private String description;

    @Future
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid;

    @PositiveOrZero
    private Integer participantsLimit;

    private Boolean requestModeration = true;

    @NotBlank
    private String title;

}

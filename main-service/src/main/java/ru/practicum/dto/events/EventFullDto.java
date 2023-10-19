package ru.practicum.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventFullDto {

    @PositiveOrZero
    private Integer id;

    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    @PositiveOrZero
    private Integer confirmedRequests;

    @PastOrPresent
    private LocalDateTime createdAt;

    @NotBlank
    private String description;

    @Future
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid;

    @PositiveOrZero
    private Integer participantsLimit;

    @PastOrPresent
    private LocalDateTime publishedAt;

    private Boolean requestModeration = true;

    private EventState state = EventState.PENDING;

    @NotBlank
    private String title;

    @PositiveOrZero
    private Integer views;

    public enum EventState {
        PENDING, PUBLISHED, CANCELED
    }

}

package ru.practicum.ewm.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @PositiveOrZero
    private Integer hits;
}

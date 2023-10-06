package ru.practicum.ewm.dto.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.ewm.dto.stats.EndpointHitDto.TimeFormatter.DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private String ip;

    @NotNull
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime timestamp = LocalDateTime.now();

    public static class TimeFormatter {
        public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    }
}

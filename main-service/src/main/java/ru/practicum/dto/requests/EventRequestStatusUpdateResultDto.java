package ru.practicum.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventRequestStatusUpdateResultDto {

    @NotNull
    private List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();

    @NotNull
    private List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

}

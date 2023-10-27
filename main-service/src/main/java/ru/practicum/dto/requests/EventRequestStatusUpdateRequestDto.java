package ru.practicum.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventRequestStatusUpdateRequestDto {

    private List<Integer> requestIds;

    private RequestUpdateStatus status;

    public enum RequestUpdateStatus {
        CONFIRMED, REJECTED
    }

}

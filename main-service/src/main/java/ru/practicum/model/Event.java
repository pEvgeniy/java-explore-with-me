package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.dto.events.EventFullDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "annotation")
    private String annotation;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id")
    private Category category;

    @PositiveOrZero
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @PastOrPresent
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotBlank
    @Column(name = "description")
    private String description;

    @Future
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "initiator_id")
    private User initiator;

    @NotNull
    @OneToOne
    @Column(name = "location_id")
    private Location location;

    @NotNull
    @Column(name = "paid")
    private Boolean paid;

    @PositiveOrZero
    @Column(name = "participant_limit")
    private Integer participantsLimit;

    @PastOrPresent
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    private EventFullDto.EventState state;

    @NotBlank
    @Column(name = "title")
    private String title;

    @PositiveOrZero
    private Integer views;

}

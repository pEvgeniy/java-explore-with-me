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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

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
    @JoinColumn(name = "category_id")
    private Category category;

    @PositiveOrZero
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @PastOrPresent
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @NotBlank
    @Column(name = "description")
    private String description;

    @Future
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @NotNull
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @NotNull
    @Column(name = "paid")
    private Boolean paid;

    @PositiveOrZero
    @Column(name = "participant_limit")
    private Integer participantLimit;

    @PastOrPresent
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventFullDto.EventState state;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @PositiveOrZero
    @Transient
    private Integer views;

}

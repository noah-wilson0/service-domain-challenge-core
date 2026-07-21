package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity;

import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.SeatStatus;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.TrainScheduleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "train_schedule_seat",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_schedule_seat",
                columnNames = {
                        "train_schedule_id",
                        "seat_id"
                }
        )
)
public class TrainScheduleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "train_schedule_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_schedule_id", nullable = false)
    private TrainSchedule trainSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;

    private TrainScheduleSeat(
            TrainSchedule trainSchedule,
            Seat seat
    ) {
        this.trainSchedule = trainSchedule;
        this.seat = seat;
        this.status = SeatStatus.AVAILABLE;
    }

    public static TrainScheduleSeat create(
            TrainSchedule trainSchedule,
            Seat seat
    ) {
        if (trainSchedule.getTrain().getId() != null &&
                !Objects.equals(trainSchedule.getTrain().getId(),
                seat.getCar().getTrain().getId()
        )) {
            throw new IllegalArgumentException(
                    "운행 열차에 속하지 않은 좌석입니다."
            );
        }
        return new TrainScheduleSeat(trainSchedule, seat);
    }

    public void reserve() {
        if (!isBookable()) {
            throw new IllegalStateException(
                    "현재 예매할 수 없는 운행 좌석입니다."
            );
        }

        status = SeatStatus.BOOKED;
    }
    public boolean isBookable() {
        return trainSchedule.getTrain().isInService()
                && trainSchedule.isBookable()
                && status == SeatStatus.AVAILABLE;
    }

    @Override
    public boolean equals(Object obj) {
        TrainScheduleSeat other = (TrainScheduleSeat) obj;
        return this.trainSchedule.equals(other.trainSchedule) &&
                this.seat.equals(other.seat) &&
                this.status == other.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                trainSchedule,
                seat,
                status
        );
    }
}

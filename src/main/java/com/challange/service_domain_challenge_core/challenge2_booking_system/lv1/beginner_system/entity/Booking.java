package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity;

import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.BookingStatus;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "booking",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_booking",
                        columnNames = {
                                "train_schedule_seat_id"
                        }
                )
        }
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Column(name = "uuid",nullable = false)
    private String user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_schedule_seat_id", nullable = false)
    private TrainScheduleSeat scheduleSeat;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status",nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Booking(
            String userId,
            TrainScheduleSeat scheduleSeat,
            BookingStatus status
    ) {
        this.user = userId;
        this.scheduleSeat = scheduleSeat;
        this.status = status;
        this.createdAt = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public static Booking create(String user, TrainScheduleSeat scheduleSeat, BookingStatus bookingStatus) {
        return new Booking(user, scheduleSeat, bookingStatus);
    }


    @Override
    public boolean equals(Object object) {
        Booking other = (Booking) object;
        return this.user.equals(other.getUser()) &&
                this.scheduleSeat.equals(other.getScheduleSeat()) &&
                this.status.equals(other.getStatus());
    }
    @Override
    public int hashCode() {
        return Objects.hash(
                user,
                scheduleSeat,
                status
        );
    }
}

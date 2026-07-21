package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "seat",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_seat_train_car_number",
                        columnNames = {
                                "car_id",
                                "seat_number"
                        }
                )
        }
)
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    private int seatNumber;

    private Seat(Car car, int seatNumber) {
        this.car = car;
        this.seatNumber = seatNumber;
    }

    public static Seat create(Car car, int seatNumber) {
        return new Seat(car, seatNumber);
    }

    @Override
    public boolean equals(Object obj) {
        Seat other = (Seat) obj;
        return this.car.equals(other.car) &&
                this.seatNumber == other.seatNumber;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

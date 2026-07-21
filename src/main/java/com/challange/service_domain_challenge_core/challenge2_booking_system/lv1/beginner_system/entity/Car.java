package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"train_id", "car_number"}
        )
)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(name = "car_number", nullable = false)
    private int number;

    @OneToMany(mappedBy = "car", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    private List<Seat> seats = new ArrayList<>();

    private Car(Train train, int number) {
        this.train = train;
        this.number = number;
    }
    public static Car create(Train train, int number) {
        return new Car(train, number);
    }

    public Seat addSeat(int seatNumber) {
        Seat seat = Seat.create(this, seatNumber);
        seats.add(seat);
        return seat;
    }
}

package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity;

import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.TrainStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "train_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "train", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    private List<Car> cars = new ArrayList<>();

    @OneToMany(mappedBy = "train", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    private List<TrainSchedule> trainSchedules = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "train_status", nullable = false)
    private TrainStatus status;
    private Train(String name) {
        this.name = name;
        this.status=TrainStatus.IN_SERVICE;
    }

    public static Train create(String name) {
        return new Train(name);
    }

    public Car addCar(int carNumber) {
        Car car = Car.create(this, carNumber);
        cars.add(car);
        return car;
    }

    public TrainSchedule addSchedule(
            LocalDate operatingDate,
            LocalTime departureTime,
            LocalTime arrivalTime,
            String departureStation,
            String arrivalStation
    ) {
        TrainSchedule schedule =
                TrainSchedule.create(
                        this,
                        operatingDate,
                        departureTime,
                        arrivalTime,
                        departureStation,
                        arrivalStation
                );

        trainSchedules.add(schedule);
        return schedule;
    }

    public void stopService() {
        if (status == TrainStatus.OUT_OF_SERVICE) {
            throw new IllegalStateException(
                    "이미 운행 중단된 기차입니다."
            );
        }

        this.status = TrainStatus.OUT_OF_SERVICE;
    }

    public void resumeService() {
        if (status == TrainStatus.IN_SERVICE) {
            throw new IllegalStateException(
                    "이미 운행 중인 기차입니다."
            );
        }

        this.status = TrainStatus.IN_SERVICE;
    }

    public boolean isInService() {
        return status == TrainStatus.IN_SERVICE;
    }



    @Override
    public boolean equals(Object obj) {
        Train other = (Train) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

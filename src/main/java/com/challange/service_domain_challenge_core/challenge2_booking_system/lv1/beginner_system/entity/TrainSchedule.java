package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity;

import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.SeatStatus;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.TrainScheduleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "train_schedule",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_schedule_train_date_time",
                columnNames = {
                        "train_id",
                        "operating_date",
                        "departure_time"
                }
        )
)
public class TrainSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "train_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(nullable = false)
    private LocalDate operatingDate;      // 운행 날짜

    @Column(nullable = false)
    private LocalTime departureTime;      // 출발 시간
    @Column(nullable = false)
    private LocalTime arrivalTime;      // 도착 시간

    @Column(nullable = false)
    private String departureStation;      // 출발역

    @Column(nullable = false)
    private String arrivalStation;        // 도착역

    @Enumerated(EnumType.STRING)
    @Column(name = "train_schedule_status", nullable = false)
    private TrainScheduleStatus status;

    private TrainSchedule(Train train, LocalDate operatingDate, LocalTime departureTime, LocalTime arrivalTime, String departureStation, String arrivalStation, TrainScheduleStatus status) {
        this.train = train;
        this.operatingDate = operatingDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.status = status;
    }

    public static TrainSchedule create(Train train, LocalDate operatingDate, LocalTime departureTime, LocalTime arrivalTime, String departureStation, String arrivalStation) {
        return new TrainSchedule(train, operatingDate, departureTime, arrivalTime, departureStation, arrivalStation, TrainScheduleStatus.SCHEDULED);
    }

    public static TrainSchedule create(Train train, LocalDate operatingDate, LocalTime departureTime, LocalTime arrivalTime, String departureStation, String arrivalStation, TrainScheduleStatus status) {
        return new TrainSchedule(train, operatingDate, departureTime, arrivalTime, departureStation, arrivalStation, status);
    }

    public void cancel() {
        if (status == TrainScheduleStatus.COMPLETED) {
            throw new IllegalStateException(
                    "완료된 운행은 취소할 수 없습니다."
            );
        }

        if (status == TrainScheduleStatus.CANCELED) {
            throw new IllegalStateException(
                    "이미 취소된 운행입니다."
            );
        }

        status = TrainScheduleStatus.CANCELED;
    }

    public void complete() {
        if (status == TrainScheduleStatus.CANCELED) {
            throw new IllegalStateException(
                    "취소된 운행은 완료할 수 없습니다."
            );
        }

        status = TrainScheduleStatus.COMPLETED;
    }

    /**
     * Delay 적용시 같은 스케쥴의 다른 departureTime 및 arrivalTime 을 가진 컬럼 을 추가해서
     * 원본 시간 - 업데이트 시간 으로 딜레이 시간 구할 수 있도록 유도 하기
     */
    public void delay() {
        if (status != TrainScheduleStatus.SCHEDULED) {
            throw new IllegalStateException(
                    "운행 예정 상태만 지연할 수 있습니다."
            );
        }

        status = TrainScheduleStatus.DELAYED;
    }

    public boolean isBookable() {
        return status == TrainScheduleStatus.SCHEDULED
                || status == TrainScheduleStatus.DELAYED;
    }


    @Override
    public boolean equals(Object obj) {
        TrainSchedule other = (TrainSchedule) obj;

        return this.train.equals(other.train) &&
                this.operatingDate.equals(other.operatingDate) &&
                this.departureTime.equals(other.departureTime) &&
                this.arrivalTime.equals(other.arrivalTime) &&
                this.departureStation.equals(other.departureStation) &&
                this.arrivalStation.equals(other.arrivalStation);

    }

    @Override
    public int hashCode() {
        return Objects.hash(
                train,
                operatingDate,
                departureTime,
                arrivalTime,
                departureStation,
                arrivalStation
        );
    }
}

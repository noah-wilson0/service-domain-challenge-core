package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.repository;

import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.BookingStatus;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.SeatStatus;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.TrainScheduleStatus;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.e.TrainStatus;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**  Lv1-M1-좌석 및 예매 기본 데이터 구성
 * 구현할 미션 종류
 * 포함: 필수 필드, 식별자, 상태값, 저장 구조, 테스트 fixture
 *
 * 미션 성공 시나리오  Given 빈 저장소, When S-001·U-001 또는 REQ-001·EVT-001 데이터를 생성하면, Then 동일 식별자로 다시 조회할 수 있다.
 * 미션 실패 시나리오  Given 이미 존재하는 식별자 또는 필수값 누락, When 데이터를 생성하면, Then 중복·검증 예외가 발생하고 기존 데이터는 유지된다.
 */
@SpringBootTest
@DisplayName("Lv1-M1-좌석 및 예매 기본 데이터 구성")
@Transactional
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private TrainScheduleRepository trainScheduleRepository;
    @Autowired
    private TrainScheduleSeatRepository trainScheduleSeatRepository;

    @Autowired
    private EntityManager em;

    private Train train;
    private TrainSchedule schedule;
    private TrainScheduleSeat bookingScheduleSeat;
    String uuid = "U-001";

    @BeforeEach
    void setUp() {
        train = Train.create("무궁화호");

        for (int i = 1; i <= 4; i++) {
            Car car = train.addCar(i);
            for (int j = 1; j <= 10; j++) {
                car.addSeat(j);
            }
        }
        schedule = train.addSchedule(
                LocalDate.of(2026, 7, 21),
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                "서울",
                "부산"
        );
        trainRepository.saveAndFlush(train);

        List<TrainScheduleSeat> scheduleSeats =
                new ArrayList<>();

        for (Car car : train.getCars()) {
            for (Seat seat : car.getSeats()) {
                TrainScheduleSeat scheduleSeat =
                        TrainScheduleSeat.create(
                                this.schedule,
                                seat
                        );

                scheduleSeats.add(scheduleSeat);

                if (car.getNumber() == 3
                        && seat.getSeatNumber() == 7) {
                    bookingScheduleSeat =
                            scheduleSeat;
                }
            }
        }

        trainScheduleSeatRepository
                .saveAllAndFlush(scheduleSeats);
    }

    @Test
    @DisplayName("예매를 저장하면 동일한 ID로 운행 일정과 좌석 정보를 조회할 수 있다.")
    void saveAndFindBooking() {
        //given
        bookingScheduleSeat.reserve();

        Booking booking = Booking.create(uuid, bookingScheduleSeat, BookingStatus.CONFIRMED);

        //when
        Booking saved = bookingRepository.saveAndFlush(booking);

        Long bookingId = saved.getId();
        Long scheduleSeatId =
                saved.getScheduleSeat().getId();

        em.clear();
        Booking result =
                bookingRepository.findById(bookingId)
                        .orElseThrow();
        //then
        assertAll(
                () -> assertEquals(
                        bookingId,
                        result.getId()
                ),
                () -> assertEquals(
                        uuid,
                        result.getUser()
                ),
                () -> assertEquals(
                        BookingStatus.CONFIRMED,
                        result.getStatus()
                ),
                () -> assertEquals(
                        scheduleSeatId,
                        result.getScheduleSeat().getId()
                )
        );

    }

    @Test
    @DisplayName("기본 기차 데이터와 일정별 좌석이 모두 저장된다")
    void saveBasicFixture() {

        assertAll(
                () -> assertEquals(
                        TrainStatus.IN_SERVICE,
                        train.getStatus()
                ),
                () -> assertEquals(
                        TrainScheduleStatus.SCHEDULED,
                        schedule.getStatus()
                ),
                () -> assertEquals(
                        SeatStatus.AVAILABLE,
                        bookingScheduleSeat.getStatus()
                )

        );


        assertAll(
                () -> assertEquals(1, trainRepository.count()),
                () -> assertEquals(4, carRepository.count()),
                () -> assertEquals(40, seatRepository.count()),
                () -> assertEquals(
                        1,
                        trainScheduleRepository.count()
                ),
                () -> assertEquals(
                        40,
                        trainScheduleSeatRepository.count()
                )
        );

    }

    @Test
    @DisplayName("같은 운행 일정에는 동일한 좌석을 중복 등록할 수 없다")
    void rejectDuplicateScheduleSeat() {
        // given
        TrainScheduleSeat duplicate = TrainScheduleSeat.create(
                schedule,
                bookingScheduleSeat.getSeat()
        );

        // when & then
        assertThrows(
                DataIntegrityViolationException.class,
                () -> trainScheduleSeatRepository.saveAndFlush(duplicate)
        );
    }

    @Test
    @DisplayName("동일한 운행 좌석에는 예매를 중복 저장할 수 없다")
    void rejectDuplicateBooking() {
        // given
        bookingScheduleSeat.reserve();

        Booking first = Booking.create(
                "U-001",
                bookingScheduleSeat,
                BookingStatus.CONFIRMED
        );
        Booking duplicate = Booking.create(
                "U-002",
                bookingScheduleSeat,
                BookingStatus.CONFIRMED
        );

        bookingRepository.saveAndFlush(first);

        // when & then
        assertThrows(
                DataIntegrityViolationException.class,
                () -> bookingRepository.saveAndFlush(duplicate)
        );
    }

    /**
     * TODO Booking.create() 예외 처리 하기 @Valid 쓰기
     */
    @Test
    @DisplayName("사용자 ID가 없으면 예매를 생성할 수 없다")
    void rejectBookingWithoutUserId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Booking.create(
                        null,
                        bookingScheduleSeat,
                        BookingStatus.CONFIRMED
                )
        );
    }

    /**
     * TODO Booking.create 예외 처리하기
     * @param userId
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("사용자 ID가 비어 있으면 예매를 생성할 수 없다")
    void rejectBlankUserId(String userId) {
        assertThrows(
                IllegalArgumentException.class,
                () -> Booking.create(
                        userId,
                        bookingScheduleSeat,
                        BookingStatus.CONFIRMED
                )
        );
    }

    /**
     * TODO Booking.create() 예외 처리 하기  @Valid 쓰기
     */
    @Test
    @DisplayName("운행 좌석이 없으면 예매를 생성할 수 없다")
    void rejectBookingWithoutScheduleSeat() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Booking.create(
                        "U-001",
                        null,
                        BookingStatus.CONFIRMED
                )
        );
    }

    @Test
    @DisplayName("운행 일정과 다른 기차의 좌석은 등록할 수 없다")
    void rejectSeatFromDifferentTrain() {
        // given
        Train otherTrain = Train.create("새마을호");
        Seat seatOfOtherTrain = otherTrain.addCar(1).addSeat(1);
        trainRepository.saveAndFlush(otherTrain);

        // when & then
        assertThrows(
                IllegalArgumentException.class,
                () -> TrainScheduleSeat.create(
                        schedule,
                        seatOfOtherTrain
                )
        );
    }




}

package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.controller;

import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity.Booking;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity.Seat;
import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity.Train;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LV1. 비기니어 수준 전체 예매 시스템 구현
 *  Mission 2.  좌석 및 예매 기본 데이터 구성 테스트
 */
@SpringBootTest
class BookingSystemControllerTest {
    /*@Autowired
    private BookingSystemController bookingSystemController;
    @Autowired
    private SeatRepository seatRepository;

    Train train;
    Seat seat;
    @BeforeEach
    void setUp() {
        train = Train.create(10);
        seat = Seat.builder(train.id, 50);
    }

    @DisplayName("Lv1-M2-SUCCESS")
    @Test
    void mission2_success() {
        //given
        String uuid= "test-1-1"; //test 예약어 - lv - mission
        Seat bookSeat=new Seat(2,29);
        BookingReqeust request = BookingReqeust.builder(uuid, train, bookSeat);

        //when
        ResponseEntity<BookingResponse> result =
                bookingSystemController.booking(request);

        Booking expected =  Booking.builder(uuid, train, bookSeat, "BOOKED");
        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> assertNotNull(result.getBody()),
                () -> assertEquals(uuid, result.getBody().userId()),
                () -> assertEquals(train, result.getBody().train()),
                () -> assertEquals(bookSeat, result.getBody().seat()),
                () -> assertEquals(BookingStatus.RESERVED, result.getBody().status())
        );
    }
    @DisplayName("Lv1-M2-FAIL")
    @Test
    void mission2_fail() {
        //given
        String uuid= "test-1-1"; //test 예약어 - lv - mission
        Seat bookSeat1=new Seat(2,29);
        //사용자 누락
        BookingRequest request1 = BookingRequest.builder(null, train, bookSeat1);

        //예약된 좌석
        Seat bookSeat2=new Seat(2,30);
        Seat reservedSeat =
                Seat.builder(train.id(), 29, SeatStatus.RESERVED);
        seatRepository.update(reservedSeat);

        BookingReqeust request2 = BookingReqeust.builder(uuid, train, bookSeat2);

        //when

        //then
        assertAll(
                () -> assertThrows(
                        BookingExcetpion.class,
                        () -> bookingSystemController.booking(request1)
                ),
                assertThrows(
                        BookingExcetpion.class,
                        () -> bookingSystemController.booking(request2)
                )

        );

    }*/

}
package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.repository;

import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

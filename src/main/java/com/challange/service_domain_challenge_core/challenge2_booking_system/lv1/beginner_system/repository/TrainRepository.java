package com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.repository;

import com.challange.service_domain_challenge_core.challenge2_booking_system.lv1.beginner_system.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

}

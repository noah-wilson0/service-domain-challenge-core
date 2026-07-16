package com.challange.service_domain_challenge_core.challenge1_bank_system.lv1_payment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Given : 시나리오 진행에 필요한 값을 설정, 테스트의 상태를 설정  [준비]
 * When : 시나리오 진행 필요조건 명시, 테스트하고자 하는 행동  [실행]
 * Then : 시나리오를 완료했을 때 보장해야 하는 결과를 명시, 예상되는 변화 설명  [검증]
 */

/**
 * Lv1. 기본 결제
 * success goals: 성공 조건: 잔액이 충분하면 성공 / 잔액 부족 시 실패 (잔액 유지) / 결제 금액은 0원보다 커야 함
 * 요구 성능
 * 시나리오 목록:
 * 1.잔액이_충분하면_결제에_성공한다,
 * 2.잔액이_부족하면_결제에_실패한다,
 * 3.결제금액이_0원이면_결제에_실패한다,
 * 4.결제금액이_음수이면_결제에_실패한다,
 * 5.결제_실패시_잔액은_유지된다
 *
 *
 */
class BackBasicPaymentSystemTest {
    BackBasicPaymentSystem backBasicPaymentSystem;
    long initBalance; // 계좌 잔고
    long amount; //결제 금액
    long expectedBalance;    // 기대 잔액
    @BeforeEach
    void setUp() {
        initBalance = 100_000L;
    }

    /**
     * mission1. 일반 결제
     *
     * 목표: 계좌 잔액을 기준으로 결제 성공과 실패 검증
     *
     * 계좌 잔액 조회, 결제 금액 검증, 잔액 부족 검증, 결제 성공 시 잔액 차감
     *
     * 제약사항: 항상 결제 금액 > 0, 잔액 초과 결제 불가, 실패 시 잔액 유지
     *
     *
     *
     */
    @Test
    @DisplayName("결제 성공")
    void mission1_case1() {
        //입력 값: 초기 계좌 잔액, 결제 금액
        //결과 값: 차감 후 최종 잔액, 성공, 실패 여부
        //given
        amount = 100_000L;

        //when

        long result = backBasicPaymentSystem.pay(initBalance, amount);
        //then
        expectedBalance = initBalance - amount;
        assertEquals(expectedBalance, result);

    }

    @Test
    @DisplayName("결제 실패")
    void mission1_case2() {

    }
}
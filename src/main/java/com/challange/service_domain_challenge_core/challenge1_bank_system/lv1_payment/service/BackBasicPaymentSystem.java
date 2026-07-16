package com.challange.service_domain_challenge_core.challenge1_bank_system.lv1_payment.service;

/**
 * TODO DB 넣기 local memory로는 내가 생각한 챌린지를 진행 불가 >> 현재 단순 로직 구현이 될 뿐임
 * 잔액 검증 (amount > initBalance)
 *
 */
public class BackBasicPaymentSystem {
    public long pay(long initBalance, long amount) {


        return initBalance - amount;
    }
}

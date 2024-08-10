package com.loginwebservice.loginwebservice.Email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.loginwebservice.loginwebservice.Email.AuthCodeUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthCodeUtilsTest {

    @DisplayName("4자리 인증 코드를 생성한다.")
    @Test
    void createAuthCode(){
        assertThat(AuthCodeUtils.createAuthCode()).hasSize(AUTH_CODE_LENGTH);
    }

    @DisplayName("인증 코드 타입이 소문자일 경우 소문자를 생성한다.")
    @Test
    void convertByLowerAlphabet(){
        //given
        //when
        String result = AuthCodeUtils.convertBy(LOWER_ALPHABET_TYPE);
        //then
        assertThat(result).isBetween("a","z");
    }


    @DisplayName("인증 코드 타입이 대문자일 경우 소문자를 생성한다.")
    @Test
    void convertByUpperAlphabet(){
        //given
        //when
        String result = AuthCodeUtils.convertBy(UPPER_ALPHABET_TYPE);
        //then
        assertThat(result).isBetween("A","Z");
    }


    @DisplayName("인증 코드 타입이 숫자일 경우 숫자를 생성한다.")
    @Test
    void convertNumberType(){
        //given
        //when
        String result = AuthCodeUtils.convertBy(NUMBER_TYPE);
        //then
        assertThat(result).isBetween("0","9");
    }
}
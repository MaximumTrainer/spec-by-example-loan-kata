package org.kata.sbe.loan.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.kata.sbe.loan.library.GivenWhenThen;
import static org.kata.sbe.loan.library.GivenWhenThen.given;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HappyFlowTest {

    @Test
    public void testPassedValueisMatchedGivenWhen(){

        int passedIntValue = 22;
        String passedStringValue = "Test Value";

        given(passedIntValue).when("testing passed int value in 'when'",
            receivedValue -> { assertThat((int) receivedValue)
                .as("given passed int value should match when received value")
                .isEqualTo(passedIntValue); return true; });
        given(passedStringValue).when("testing passed string value in 'when'",
            receivedValue -> { assertThat(receivedValue)
                .as("given passed string value should match when received value")
                .isEqualTo(passedStringValue); return true; });

    }

    @Test
    public void testPassedValueIsMatchedWhenThen() throws ExecutionException, InterruptedException {
        String whenValue = "Test Value";

        given(null)
            .when(whenReceivedValue -> whenValue)
            .then("testing passed string value in 'then'",thenReceivedValue ->
            { assertThat(thenReceivedValue)
                .as("when passed value should match then received value")
                .isEqualTo(whenValue); });

    }

    @Test
    public void basicFlowTest(){
        given(1)
            .when("multiplying by 2", givenValue -> 2*givenValue)
            .then("value should be even", whenValue -> whenValue%2 == 0);
    }

    @Test
    public void basicMultiWhenFlowTest(){
        given(1)
                .when("multiplying by 2", givenValue -> 2*givenValue)
                .when("High squared", givenValue -> 2*givenValue)
                .then("value should be a square", whenValue -> whenValue == Math.pow(Math.sqrt(whenValue),2));
    }

    @Test
    public void multiTypeFlowTest(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DayOfWeek expectedDay = localDateTime.getDayOfWeek();

        given(localDateTime)
            .when("retrieving current day", LocalDateTime::getDayOfWeek)
            .then("days should match", day -> expectedDay == day);
    }
    @Test
    public void assertFlowTest(){
        Integer primeNumber = 17;
        given("a prime number", primeNumber)
            .when("finding dividers naively", number -> IntStream.rangeClosed(1, number)
                    .boxed().filter(value -> number%value == 0).collect(Collectors.toList()))
            .then(dividers -> {
                assertThat(dividers.size())
                    .as("should have two dividers")
                    .isEqualTo(2);
                assertThat((int) dividers.get(0))
                    .as("first divider should be 1")
                    .isEqualTo(1);
                assertThat(dividers.get(1))
                    .as("first divider should be %d", primeNumber)
                    .isEqualTo(primeNumber);
            });
    }


}

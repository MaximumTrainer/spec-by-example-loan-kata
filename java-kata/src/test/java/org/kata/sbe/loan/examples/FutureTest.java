package org.kata.sbe.loan.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.kata.sbe.loan.library.GivenWhenThen;

import static org.kata.sbe.loan.library.GivenWhenThen.given;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FutureTest {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    @Test
    public void basicGivenFutureFlowTest(){
            given(executor.submit(() -> 1))
                .when("multiplying by 2", givenValue -> 2*givenValue)
                .then("value should be even", whenValue -> whenValue%2 == 0);
    }
}

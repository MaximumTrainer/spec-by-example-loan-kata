package org.kata.sbe.loan.examples;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;

import org.kata.sbe.loan.library.GivenWhenThen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.kata.sbe.loan.library.GivenWhenThen.*;
import static org.kata.sbe.loan.library.GivenWhenThen.FAILED;
import static org.kata.sbe.loan.library.GivenWhenThen.WHEN_FUNCTION_FAILED;
import static org.kata.sbe.loan.library.GivenWhenThen.THEN_FUNCTION_FAILED;
import static org.kata.sbe.loan.library.GivenWhenThen.THEN_NOT_SATISFIED;


public class ExceptionsAreThrownTest {

    public static final String EVERYTHING_WILL_BE_FINE = "everything will be fine";
    public static final String BAD_THINGS_HAPPEN = "bad things happen";
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Test
    public void testExceptionIsThrownFromWhen(){
        assertThrows(RuntimeException.class,
                ()->{
                    GivenWhenThen.given(null)
                            .when(whenReceivedValue -> {throw new NullPointerException(BAD_THINGS_HAPPEN); });
        });
    }

    @Test
    public void testExceptionMessageFromWhen(){
        try{
            given(null)
                    .when(whenReceivedValue -> {throw new NullPointerException(BAD_THINGS_HAPPEN); });
        } catch (RuntimeException ex){
            assertThat(ex.getMessage())
                .as("Exception message should match.")
                .isEqualTo(WHEN_FUNCTION_FAILED);
        }

        try{
            given(null)
                    .when(EVERYTHING_WILL_BE_FINE, whenReceivedValue -> {throw new NullPointerException("bad things happen"); });
        } catch (RuntimeException ex){
            assertThat(ex.getMessage())
                .as("Exception message should match.")
                .isEqualTo(EVERYTHING_WILL_BE_FINE + FAILED);
        }

    }

    @Test
    public void testExceptionMessageFromThen(){
        try{
            given(null)
                    .when(value -> null)
                    .then((Consumer<Object>) whenReceivedValue -> { throw new NullPointerException(BAD_THINGS_HAPPEN); });

        } catch (RuntimeException ex){
            assertThat(ex.getMessage())
                .as("Exception message should match.")
                .isEqualTo(THEN_FUNCTION_FAILED);
        }

        try{
            given(null)
                    .when(value -> null)
                    .then(EVERYTHING_WILL_BE_FINE, (Consumer<Object>) whenReceivedValue -> { throw new NullPointerException(BAD_THINGS_HAPPEN); });
        } catch (RuntimeException ex){
            assertThat(ex.getMessage())
                .as("Exception message should match.")
                .isEqualTo(EVERYTHING_WILL_BE_FINE + FAILED);
        }

        try{
            given(null)
                    .when(value -> null)
                    .then((Predicate<Object>) whenReceivedValue -> { throw new NullPointerException(BAD_THINGS_HAPPEN); });

        } catch (RuntimeException ex){
            assertThat(ex.getMessage())
                .as("Exception message should match.")
                .isEqualTo(BAD_THINGS_HAPPEN);
        }

        try{
            given(null)
                    .when(value -> null)
                    .then(whenReceivedValue -> false);
        } catch (RuntimeException ex){
            assertThat(ex.getMessage())
                .as("Exception message should match.")
                .isEqualTo(THEN_NOT_SATISFIED);
        }

    }

    @Test
    public void testExceptionFromGivenFuture(){
        assertThrows(RuntimeException.class,
                ()->{
                    given(executor.submit(() ->{ throw new RuntimeException("future will fail"); }));
                });
    }

    @Test
    public void testExceptionMessageFromGivenFuture(){
        try{
            given(executor.submit(() ->{ throw new RuntimeException("future will fail"); }));
        } catch (RuntimeException ex){
            assertThat(ex.getMessage())
                .as("Exception message should match.")
                .isEqualTo(GivenWhenThen.FUTURE_FAILED);
        }
    }
}

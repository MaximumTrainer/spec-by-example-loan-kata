package org.kata.sbe.loan.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.kata.sbe.loan.domain.ApplicationResult;
import org.kata.sbe.loan.domain.DeclineReason;
import org.kata.sbe.loan.domain.LoanApplication;
import org.kata.sbe.loan.domain.LoanPreQualifier;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kata.sbe.loan.library.GivenWhenThen.given;
import static org.kata.sbe.loan.rules.LoanApplicationTestBuilder.aValidApplication;

class LoanPreQualifierTest {



    @Test
    @DisplayName("Should approve valid application using test builder")
    void shouldApproveValidApplication() {
        given(aValidApplication().build())
            .when(LoanPreQualifier.preQualify())
            .then((Consumer<ApplicationResult>) result -> assertThat(result.isApproved()).isTrue());
    }

    @Test
    @DisplayName("Should approve application meeting all criteria")
    void shouldApproveWhenAllCriteriaMet() {
        // Given
        LoanApplication application = aValidApplication()
                .withLoanToValueRatio(0.75)
                .withAnnualIncome(50000)
                .build();

        given(application)
            .when(LoanPreQualifier.preQualify())
            .then(result -> {
                assertThat(result.isApproved()).isTrue();
                assertThat(result.getDeclineReasons()).isEmpty();
            });
    }

    @Test
    @DisplayName("Should decline when credit score is too low")
    void shouldDeclineWhenCreditScoreInsufficient() {
        // Given
        LoanApplication application = aValidApplication()
                .withCreditScore(580)
                .build();

        given(application)
            .when(LoanPreQualifier.preQualify())
            .then(result -> {
                assertThat(result.isApproved()).isFalse();
                assertThat(result.getDeclineReasons())
                    .containsExactly(DeclineReason.INSUFFICIENT_CREDIT_SCORE);
            });
    }

    @Test
    @DisplayName("Should decline when debt-to-income ratio is too high")
    void shouldDeclineWhenDebtToIncomeTooHigh() {
        // Given
        LoanApplication application = aValidApplication()
                .withDebtToIncomeRatio(0.50) // Above 0.43 threshold
                .build();

        given(application)
            .when(LoanPreQualifier.preQualify())
            .then(result -> {
                assertThat(result.isApproved()).isFalse();
                assertThat(result.getDeclineReasons())
                    .containsExactly(DeclineReason.HIGH_DEBT_TO_INCOME_RATIO);
            });
    }

    @Test
    @DisplayName("Should decline when loan-to-value ratio is too high")
    void shouldDeclineWhenLoanToValueTooHigh() {
        // Given
        LoanApplication application = aValidApplication()
                .withLoanToValueRatio(0.90) // Above 0.80 threshold
                .build();

        given(application)
            .when(LoanPreQualifier.preQualify())
            .then(result -> {
                assertThat(result.isApproved()).isFalse();
                assertThat(result.getDeclineReasons())
                    .containsExactly(DeclineReason.HIGH_LOAN_TO_VALUE_RATIO);
            });
    }

    @Test
    @DisplayName("Should decline when income is insufficient")
    void shouldDeclineWhenIncomeInsufficient() {
        // Given
        LoanApplication application = aValidApplication()
                .withAnnualIncome(25000) // Below 30000 threshold
                .build();

        given(application)
            .when(LoanPreQualifier.preQualify())
            .then(result -> {
                assertThat(result.isApproved()).isFalse();
                assertThat(result.getDeclineReasons())
                    .containsExactly(DeclineReason.INSUFFICIENT_INCOME);
            });
    }

    @Test
    @DisplayName("Should decline with multiple reasons when multiple criteria fail")
    void shouldDeclineWithMultipleReasons() {
        // Given
        LoanApplication application = aValidApplication()
                .withCreditScore(550) // Too low
                .withDebtToIncomeRatio(0.50) // Too high
                .withLoanToValueRatio(0.85) // Too high
                .withAnnualIncome(25000) // Too low
                .build();

        given(application)
            .when(LoanPreQualifier.preQualify())
            .then(result -> {
                assertThat(result.isApproved()).isFalse();
                assertThat(result.getDeclineReasons())
                    .containsExactlyInAnyOrder(
                        DeclineReason.INSUFFICIENT_CREDIT_SCORE,
                        DeclineReason.HIGH_DEBT_TO_INCOME_RATIO,
                        DeclineReason.HIGH_LOAN_TO_VALUE_RATIO,
                        DeclineReason.INSUFFICIENT_INCOME
                    );
            });
    }

    @Test
    @DisplayName("Should approve when at exact threshold values")
    void shouldApproveAtThresholdValues() {
        // Given
        LoanApplication application = aValidApplication()
                .withCreditScore(600) // Minimum acceptable
                .withDebtToIncomeRatio(0.43) // Maximum acceptable
                .withLoanToValueRatio(0.80) // Maximum acceptable
                .withAnnualIncome(30000) // Minimum acceptable
                .build();

        given(application)
            .when(LoanPreQualifier.preQualify())
            .then(result -> {
                assertThat(result.isApproved()).isTrue();
                assertThat(result.getDeclineReasons()).isEmpty();
            });
    }

    @Test
    @DisplayName("Should calculate LTV automatically when loan amount and property value provided")
    void shouldCalculateLTVFromLoanAndPropertyValues() {
        // Given
        double loanAmount = 160000;
        double propertyValue = 200000;
        double expectedLTV = 0.80;

        LoanApplication application = LoanApplicationTestBuilder
                .anApplicationWithLoanAmount(loanAmount, propertyValue)
                .build();

        given(application)
            .when(LoanPreQualifier.preQualify())
            .then(result -> {
                assertThat(application.getLoanToValueRatio()).isEqualTo(expectedLTV);
                assertThat(result.isApproved()).isTrue();
            });
    }
}
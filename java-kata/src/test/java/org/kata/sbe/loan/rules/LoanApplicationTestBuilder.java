package org.kata.sbe.loan.rules;

import org.kata.sbe.loan.domain.LoanApplication;

class LoanApplicationTestBuilder {
    private int creditScore = 750;
    private double debtToIncomeRatio = 0.35;
    private double loanToValueRatio = 0.75;
    private double annualIncome = 50000;
    private Double loanAmount = null;
    private Double propertyValue = null;


    public static LoanApplicationTestBuilder aValidApplication() {
        return new LoanApplicationTestBuilder();
    }

    public static LoanApplicationTestBuilder anApplicationWithLoanAmount(double loanAmount, double propertyValue) {
        return new LoanApplicationTestBuilder()
                .withLoanAmount(loanAmount)
                .withPropertyValue(propertyValue);
    }

    public LoanApplicationTestBuilder withCreditScore(int creditScore) {
        this.creditScore = creditScore;
        return this;
    }

    public LoanApplicationTestBuilder withDebtToIncomeRatio(double ratio) {
        this.debtToIncomeRatio = ratio;
        return this;
    }

    public LoanApplicationTestBuilder withLoanToValueRatio(double ratio) {
        this.loanToValueRatio = ratio;
        return this;
    }

    public LoanApplicationTestBuilder withAnnualIncome(double income) {
        this.annualIncome = income;
        return this;
    }

    public LoanApplicationTestBuilder withLoanAmount(double amount) {
        this.loanAmount = amount;
        return this;
    }

    public LoanApplicationTestBuilder withPropertyValue(double value) {
        this.propertyValue = value;
        return this;
    }

    public LoanApplication build() {
        LoanApplication.Builder builder = LoanApplication.builder()
                .withCreditScore(creditScore)
                .withDebtToIncomeRatio(debtToIncomeRatio)
                .withAnnualIncome(annualIncome);

        if (loanAmount != null && propertyValue != null) {
            builder.withLoanAmount(loanAmount)
                   .withPropertyValue(propertyValue);
        } else {
            builder.withLoanToValueRatio(loanToValueRatio);
        }

        return builder.build();
    }
}

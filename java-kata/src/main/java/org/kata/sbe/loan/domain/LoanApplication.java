package org.kata.sbe.loan.domain;

public class LoanApplication {
    private final int creditScore;
    private final double debtToIncomeRatio;
    private final double loanToValueRatio;
    private final double annualIncome;
    private final double loanAmount;
    private final double propertyValue;

    private LoanApplication(Builder builder) {
        this.creditScore = builder.creditScore;
        this.debtToIncomeRatio = builder.debtToIncomeRatio;
        this.loanToValueRatio = builder.loanToValueRatio;
        this.annualIncome = builder.annualIncome;
        this.loanAmount = builder.loanAmount;
        this.propertyValue = builder.propertyValue;
    }

    // Getters
    public int getCreditScore() { return creditScore; }
    public double getDebtToIncomeRatio() { return debtToIncomeRatio; }
    public double getLoanToValueRatio() { return loanToValueRatio; }
    public double getAnnualIncome() { return annualIncome; }
    public double getLoanAmount() { return loanAmount; }
    public double getPropertyValue() { return propertyValue; }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int creditScore;
        private double debtToIncomeRatio;
        private double loanToValueRatio;
        private double annualIncome;
        private double loanAmount;
        private double propertyValue;

        public Builder withCreditScore(int creditScore) {
            this.creditScore = creditScore;
            return this;
        }

        public Builder withDebtToIncomeRatio(double debtToIncomeRatio) {
            this.debtToIncomeRatio = debtToIncomeRatio;
            return this;
        }

        public Builder withLoanToValueRatio(double loanToValueRatio) {
            this.loanToValueRatio = loanToValueRatio;
            return this;
        }

        public Builder withAnnualIncome(double annualIncome) {
            this.annualIncome = annualIncome;
            return this;
        }

        public Builder withLoanAmount(double loanAmount) {
            this.loanAmount = loanAmount;
            return this;
        }

        public Builder withPropertyValue(double propertyValue) {
            this.propertyValue = propertyValue;
            return this;
        }

        public LoanApplication build() {
            if (loanAmount > 0 && propertyValue > 0) {
                this.loanToValueRatio = loanAmount / propertyValue;
            }
            return new LoanApplication(this);
        }
    }
}
namespace Domain;

public class LoanApplication
{
    public int CreditScore { get; }
    public double DebtToIncomeRatio { get; }
    public double LoanToValueRatio { get; private set; }
    public double AnnualIncome { get; }
    public double LoanAmount { get; }
    public double PropertyValue { get; }

    private LoanApplication(Builder builder)
    {
        CreditScore = builder.CreditScore;
        DebtToIncomeRatio = builder.DebtToIncomeRatio;
        LoanToValueRatio = builder.LoanToValueRatio;
        AnnualIncome = builder.AnnualIncome;
        LoanAmount = builder.LoanAmount;
        PropertyValue = builder.PropertyValue;

        if (PropertyValue > 0 && LoanAmount > 0)
        {
            LoanToValueRatio = LoanAmount / PropertyValue;
        }
    }

    public static Builder CreateBuilder() => new();

    public class Builder
    {
        internal int CreditScore { get; private set; }
        internal double DebtToIncomeRatio { get; private set; }
        internal double LoanToValueRatio { get; private set; }
        internal double AnnualIncome { get; private set; }
        internal double LoanAmount { get; private set; }
        internal double PropertyValue { get; private set; }

        public Builder WithCreditScore(int creditScore)
        {
            CreditScore = creditScore;
            return this;
        }

        public Builder WithDebtToIncomeRatio(double ratio)
        {
            DebtToIncomeRatio = ratio;
            return this;
        }

        public Builder WithLoanToValueRatio(double ratio)
        {
            LoanToValueRatio = ratio;
            return this;
        }

        public Builder WithAnnualIncome(double income)
        {
            AnnualIncome = income;
            return this;
        }

        public Builder WithLoanAmount(double amount)
        {
            LoanAmount = amount;
            return this;
        }

        public Builder WithPropertyValue(double value)
        {
            PropertyValue = value;
            return this;
        }

        public LoanApplication Build() => new(this);
    }
}
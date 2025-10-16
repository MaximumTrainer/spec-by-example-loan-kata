using Domain;

namespace Tests;

internal class LoanApplicationTestBuilder
{
    private int _creditScore = 750;
    private double _debtToIncomeRatio = 0.35;
    private double _loanToValueRatio = 0.75;
    private double _annualIncome = 50000;
    private double? _loanAmount;
    private double? _propertyValue;

    public static LoanApplicationTestBuilder AValidApplication()
        => new();

    public static LoanApplicationTestBuilder AnApplicationWithLoanAmount(double loanAmount, double propertyValue)
        => new LoanApplicationTestBuilder()
            .WithLoanAmount(loanAmount)
            .WithPropertyValue(propertyValue);

    public LoanApplicationTestBuilder WithCreditScore(int creditScore)
    {
        _creditScore = creditScore;
        return this;
    }

    public LoanApplicationTestBuilder WithDebtToIncomeRatio(double ratio)
    {
        _debtToIncomeRatio = ratio;
        return this;
    }

    public LoanApplicationTestBuilder WithLoanToValueRatio(double ratio)
    {
        _loanToValueRatio = ratio;
        return this;
    }

    public LoanApplicationTestBuilder WithAnnualIncome(double income)
    {
        _annualIncome = income;
        return this;
    }

    public LoanApplicationTestBuilder WithLoanAmount(double amount)
    {
        _loanAmount = amount;
        return this;
    }

    public LoanApplicationTestBuilder WithPropertyValue(double value)
    {
        _propertyValue = value;
        return this;
    }

    public LoanApplication Build()
    {
        var builder = LoanApplication.CreateBuilder()
            .WithCreditScore(_creditScore)
            .WithDebtToIncomeRatio(_debtToIncomeRatio)
            .WithAnnualIncome(_annualIncome);

        if (_loanAmount.HasValue && _propertyValue.HasValue)
        {
            builder.WithLoanAmount(_loanAmount.Value)
                   .WithPropertyValue(_propertyValue.Value);
        }
        else
        {
            builder.WithLoanToValueRatio(_loanToValueRatio);
        }

        return builder.Build();
    }
}
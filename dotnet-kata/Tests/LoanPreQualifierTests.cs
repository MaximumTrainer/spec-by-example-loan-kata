using Domain;
using FluentAssertions;
using Xunit;
using static Tests.GivenWhenThen<Domain.LoanApplication>;
using static Tests.LoanApplicationTestBuilder;

namespace Tests;

public class LoanPreQualifierTests
{
    [Fact]
    public void ShouldApproveValidApplication()
    {
        Given(AValidApplication().Build())
            .When(LoanPreQualifier.PreQualify())
            .Then(result => result.IsApproved.Should().BeTrue());
    }

    [Fact]
    public void ShouldApproveWhenAllCriteriaMet()
    {
        Given(AValidApplication()
            .WithLoanToValueRatio(0.75)
            .WithAnnualIncome(50000)
            .Build())
            .When(LoanPreQualifier.PreQualify())
            .Then(result =>
            {
                result.IsApproved.Should().BeTrue();
                result.DeclineReasons.Should().BeEmpty();
            });
    }

    [Fact]
    public void ShouldDeclineWhenCreditScoreInsufficient()
    {
        Given(AValidApplication()
            .WithCreditScore(580)
            .Build())
            .When(LoanPreQualifier.PreQualify())
            .Then(result =>
            {
                result.IsApproved.Should().BeFalse();
                result.DeclineReasons.Should()
                    .ContainSingle()
                    .Which.Should().Be(DeclineReason.InsufficientCreditScore);
            });
    }

    [Fact]
    public void ShouldDeclineWhenDebtToIncomeTooHigh()
    {
        Given(AValidApplication()
            .WithDebtToIncomeRatio(0.50) // Above 0.43 threshold
            .Build())
            .When(LoanPreQualifier.PreQualify())
            .Then(result =>
            {
                result.IsApproved.Should().BeFalse();
                result.DeclineReasons.Should()
                    .ContainSingle()
                    .Which.Should().Be(DeclineReason.HighDebtToIncomeRatio);
            });
    }

    [Fact]
    public void ShouldDeclineWhenLoanToValueTooHigh()
    {
        Given(AValidApplication()
            .WithLoanToValueRatio(0.90) // Above 0.80 threshold
            .Build())
            .When(LoanPreQualifier.PreQualify())
            .Then(result =>
            {
                result.IsApproved.Should().BeFalse();
                result.DeclineReasons.Should()
                    .ContainSingle()
                    .Which.Should().Be(DeclineReason.HighLoanToValueRatio);
            });
    }

    [Fact]
    public void ShouldDeclineWhenIncomeInsufficient()
    {
        Given(AValidApplication()
            .WithAnnualIncome(25000) // Below 30000 threshold
            .Build())
            .When(LoanPreQualifier.PreQualify())
            .Then(result =>
            {
                result.IsApproved.Should().BeFalse();
                result.DeclineReasons.Should()
                    .ContainSingle()
                    .Which.Should().Be(DeclineReason.InsufficientIncome);
            });
    }

    [Fact]
    public void ShouldDeclineWithMultipleReasons()
    {
        Given(AValidApplication()
            .WithCreditScore(550) // Too low
            .WithDebtToIncomeRatio(0.50) // Too high
            .WithLoanToValueRatio(0.85) // Too high
            .WithAnnualIncome(25000) // Too low
            .Build())
            .When(LoanPreQualifier.PreQualify())
            .Then(result =>
            {
                result.IsApproved.Should().BeFalse();
                result.DeclineReasons.Should().BeEquivalentTo(new[]
                {
                    DeclineReason.InsufficientCreditScore,
                    DeclineReason.HighDebtToIncomeRatio,
                    DeclineReason.HighLoanToValueRatio,
                    DeclineReason.InsufficientIncome
                });
            });
    }

    [Fact]
    public void ShouldApproveAtThresholdValues()
    {
        Given(AValidApplication()
            .WithCreditScore(600) // Minimum acceptable
            .WithDebtToIncomeRatio(0.43) // Maximum acceptable
            .WithLoanToValueRatio(0.80) // Maximum acceptable
            .WithAnnualIncome(30000) // Minimum acceptable
            .Build())
            .When(LoanPreQualifier.PreQualify())
            .Then(result =>
            {
                result.IsApproved.Should().BeTrue();
                result.DeclineReasons.Should().BeEmpty();
            });
    }

    [Fact]
    public void ShouldCalculateLTVFromLoanAndPropertyValues()
    {
        const double loanAmount = 160000;
        const double propertyValue = 200000;
        const double expectedLTV = 0.80;

        var application = AnApplicationWithLoanAmount(loanAmount, propertyValue)
            .Build();

        Given(application)
            .When(LoanPreQualifier.PreQualify())
            .Then(result =>
            {
                application.LoanToValueRatio.Should().Be(expectedLTV);
                result.IsApproved.Should().BeTrue();
            });
    }
}
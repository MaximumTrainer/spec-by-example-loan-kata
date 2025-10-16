namespace Domain;

public class LoanPreQualifier
{
    private const int MinCreditScore = 600;
    private const double MaxDebtToIncome = 0.43;
    private const double MaxLoanToValue = 0.80;
    private const double MinAnnualIncome = 30000;

    public static Func<LoanApplication, ApplicationResult> PreQualify() =>
        application => EvaluateApplication(application);

    private static ApplicationResult EvaluateApplication(LoanApplication application)
    {
        var declineReasons = new List<DeclineReason>();

        CheckCreditScore(application, declineReasons);
        CheckDebtToIncome(application, declineReasons);
        CheckLoanToValue(application, declineReasons);
        CheckAnnualIncome(application, declineReasons);

        return declineReasons.Count == 0
            ? ApplicationResult.Approved()
            : ApplicationResult.Declined(declineReasons);
    }

    private static void CheckCreditScore(LoanApplication application, List<DeclineReason> declineReasons)
    {
        if (application.CreditScore < MinCreditScore)
        {
            declineReasons.Add(DeclineReason.InsufficientCreditScore);
        }
    }

    private static void CheckDebtToIncome(LoanApplication application, List<DeclineReason> declineReasons)
    {
        if (application.DebtToIncomeRatio > MaxDebtToIncome)
        {
            declineReasons.Add(DeclineReason.HighDebtToIncomeRatio);
        }
    }

    private static void CheckLoanToValue(LoanApplication application, List<DeclineReason> declineReasons)
    {
        if (application.LoanToValueRatio > MaxLoanToValue)
        {
            declineReasons.Add(DeclineReason.HighLoanToValueRatio);
        }
    }

    private static void CheckAnnualIncome(LoanApplication application, List<DeclineReason> declineReasons)
    {
        if (application.AnnualIncome < MinAnnualIncome)
        {
            declineReasons.Add(DeclineReason.InsufficientIncome);
        }
    }
}
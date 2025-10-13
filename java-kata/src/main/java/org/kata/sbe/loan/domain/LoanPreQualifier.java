package org.kata.sbe.loan.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LoanPreQualifier {
    private static final int MIN_CREDIT_SCORE = 600;
    private static final double MAX_DEBT_TO_INCOME = 0.43;
    private static final double MAX_LOAN_TO_VALUE = 0.80;
    private static final double MIN_ANNUAL_INCOME = 30000;

    public static Function<LoanApplication, ApplicationResult> preQualify() {
        return application -> evaluateApplication(application);
    }

    private static ApplicationResult evaluateApplication(LoanApplication application) {
        List<DeclineReason> declineReasons = new ArrayList<>();

        checkCreditScore(application, declineReasons);
        checkDebtToIncome(application, declineReasons);
        checkLoanToValue(application, declineReasons);
        checkAnnualIncome(application, declineReasons);

        return declineReasons.isEmpty() ?
                ApplicationResult.approved() :
                ApplicationResult.declined(declineReasons);
    }

    private static LoanApplication checkCreditScore(LoanApplication application, List<DeclineReason> declineReasons) {
        if (application.getCreditScore() < MIN_CREDIT_SCORE) {
            declineReasons.add(DeclineReason.INSUFFICIENT_CREDIT_SCORE);
        }
        return application;
    }

    private static LoanApplication checkDebtToIncome(LoanApplication application, List<DeclineReason> declineReasons) {
        if (application.getDebtToIncomeRatio() > MAX_DEBT_TO_INCOME) {
            declineReasons.add(DeclineReason.HIGH_DEBT_TO_INCOME_RATIO);
        }
        return application;
    }

    private static LoanApplication checkLoanToValue(LoanApplication application, List<DeclineReason> declineReasons) {
        if (application.getLoanToValueRatio() > MAX_LOAN_TO_VALUE) {
            declineReasons.add(DeclineReason.HIGH_LOAN_TO_VALUE_RATIO);
        }
        return application;
    }

    private static LoanApplication checkAnnualIncome(LoanApplication application, List<DeclineReason> declineReasons) {
        if (application.getAnnualIncome() < MIN_ANNUAL_INCOME) {
            declineReasons.add(DeclineReason.INSUFFICIENT_INCOME);
        }
        return application;
    }
}
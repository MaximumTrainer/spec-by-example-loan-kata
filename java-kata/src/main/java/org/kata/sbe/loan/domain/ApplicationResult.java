package org.kata.sbe.loan.domain;

import java.util.Collections;
import java.util.List;

public class ApplicationResult {
    private final ApplicationStatus status;
    private final List<DeclineReason> declineReasons;

    private ApplicationResult(ApplicationStatus status, List<DeclineReason> declineReasons) {
        this.status = status;
        this.declineReasons = Collections.unmodifiableList(declineReasons);
    }

    public static ApplicationResult approved() {
        return new ApplicationResult(ApplicationStatus.APPROVED, Collections.emptyList());
    }

    public static ApplicationResult declined(List<DeclineReason> reasons) {
        return new ApplicationResult(ApplicationStatus.DECLINED, reasons);
    }

    // Getters
    public ApplicationStatus getStatus() { return status; }
    public List<DeclineReason> getDeclineReasons() { return declineReasons; }
    public boolean isApproved() { return status == ApplicationStatus.APPROVED; }
}
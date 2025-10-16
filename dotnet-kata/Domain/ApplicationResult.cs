namespace Domain;

public class ApplicationResult
{
    public ApplicationStatus Status { get; }
    public IReadOnlyList<DeclineReason> DeclineReasons { get; }
    public bool IsApproved => Status == ApplicationStatus.Approved;

    private ApplicationResult(ApplicationStatus status, IReadOnlyList<DeclineReason> declineReasons)
    {
        Status = status;
        DeclineReasons = declineReasons;
    }

    public static ApplicationResult Approved() => 
        new(ApplicationStatus.Approved, Array.Empty<DeclineReason>());

    public static ApplicationResult Declined(IEnumerable<DeclineReason> reasons) => 
        new(ApplicationStatus.Declined, reasons.ToList().AsReadOnly());
}
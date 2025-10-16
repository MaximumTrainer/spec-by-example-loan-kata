namespace Tests;

public class GivenWhenThen<T>
{
    public const string WhenFunctionFailed = "When function failed.";
    public const string Failed = " - failed";
    public const string ThenNotSatisfied = "Then not satisfied.";
    public const string ThenFunctionFailed = "Then function failed.";
    private readonly T _received;

    private GivenWhenThen(T received)
    {
        _received = received;
    }

    public GivenWhenThen<F> When<F>(Func<T, F> whenFunction)
        => When(null, whenFunction);

    public GivenWhenThen<F> When<F>(string? message, Func<T, F> whenFunction)
    {
        try
        {
            return new GivenWhenThen<F>(whenFunction(_received));
        }
        catch (Exception ex)
        {
            throw new InvalidOperationException(message == null ? WhenFunctionFailed : message + Failed, ex);
        }
    }

    public void Then(Action<T> thenFunction)
        => Then(null, thenFunction);

    public void Then(string? message, Action<T> thenFunction)
    {
        try
        {
            thenFunction(_received);
        }
        catch (Exception ex)
        {
            throw new InvalidOperationException(message == null ? ThenFunctionFailed : message + Failed, ex);
        }
    }

    public void Then(Func<T, bool> thenFunction)
        => Then(null, thenFunction);

    public void Then(string? message, Func<T, bool> thenFunction)
    {
        try
        {
            if (!thenFunction(_received))
            {
                throw new InvalidOperationException(message ?? ThenNotSatisfied);
            }
        }
        catch (Exception ex) when (ex is not InvalidOperationException)
        {
            throw new InvalidOperationException(message == null ? ThenFunctionFailed : message + Failed, ex);
        }
    }

    public static GivenWhenThen<E> Given<E>(E receivedObj)
        => Given(null, receivedObj);

    public static GivenWhenThen<E> Given<E>(string? message, E receivedObj)
        => new(receivedObj);

    public static GivenWhenThen<E> Given<E>(Task<E> receivedObj)
        => Given(null, receivedObj);

    public static GivenWhenThen<E> Given<E>(string? message, Task<E> receivedObj)
        => new(receivedObj.Result);
}
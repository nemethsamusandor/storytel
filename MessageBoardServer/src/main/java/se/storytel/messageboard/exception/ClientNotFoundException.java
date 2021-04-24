package se.storytel.messageboard.exception;

/**
 * Throw this exception if client is not found
 *
 * @author Sandor Nemeth
 */
public class ClientNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = -5129074909978267391L;

    public ClientNotFoundException()
    {
        super();
    }

    public ClientNotFoundException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public ClientNotFoundException(final String message)
    {
        super(message);
    }

    public ClientNotFoundException(final Throwable cause)
    {
        super(cause);
    }
}

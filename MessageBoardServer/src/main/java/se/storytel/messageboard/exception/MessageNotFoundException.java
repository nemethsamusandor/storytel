package se.storytel.messageboard.exception;

/**
 * Throw this exception if message is not found
 *
 * @author Sandor Nemeth
 */
public class MessageNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 5225671366341799509L;

    public MessageNotFoundException()
    {
        super();
    }

    public MessageNotFoundException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public MessageNotFoundException(final String message)
    {
        super(message);
    }

    public MessageNotFoundException(final Throwable cause)
    {
        super(cause);
    }
}

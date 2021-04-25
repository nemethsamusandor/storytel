package se.storytel.messageboard.dto;

/**
 * Wrap error response for proper message format
 *
 * @author Sandor Nemeth
 */
public class ErrorResponseWrapper
{
    private final String message;
    private final String status;

    public ErrorResponseWrapper(String message, String status)
    {
        super();
        this.message = message;
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public String getStatus()
    {
        return status;
    }
}

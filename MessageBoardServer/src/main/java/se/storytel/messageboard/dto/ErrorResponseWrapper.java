package se.storytel.messageboard.dto;

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

package se.storytel.messageboard.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import se.storytel.messageboard.dto.ErrorResponseWrapper;
import se.storytel.messageboard.exception.ClientNotFoundException;
import se.storytel.messageboard.exception.MessageNotFoundException;

/**
 * Handle Rest Api Errors
 *
 * @author Sandor Nemeth
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    public RestExceptionHandler()
    {
        super();
    }

    @ExceptionHandler(ClientNotFoundException.class)
    protected ResponseEntity<Object> handleClientNotFound(Exception exception, WebRequest request)
    {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponseWrapper body = new ErrorResponseWrapper("Client not found", getStatusMessage(status));

        return handleExceptionInternal(exception, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    protected ResponseEntity<Object> handleMessageNotFound(Exception exception, WebRequest request)
    {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponseWrapper body = new ErrorResponseWrapper("Message not found", getStatusMessage(status));

        return handleExceptionInternal(exception, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({
        ConstraintViolationException.class,
        DataIntegrityViolationException.class
    })
    public ResponseEntity<Object> handleBadRequest(Exception exception, WebRequest request)
    {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseWrapper body = new ErrorResponseWrapper(exception.getLocalizedMessage(), getStatusMessage(status));

        return handleExceptionInternal(exception, body, new HttpHeaders(), status, request);
    }

    private String getStatusMessage(HttpStatus status)
    {
        return String.format("%s %s", status.value(), status.getReasonPhrase());
    }
}

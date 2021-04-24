package se.storytel.messageboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application
 *
 *  @author Sandor Nemeth
 */
@SpringBootApplication
public class MessageBoardApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MessageBoardApplication.class, args);
    }
}

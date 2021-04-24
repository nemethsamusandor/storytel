package se.storytel.messageboard.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Used for response type where the result is a list of {@link MessageDTO}
 *
 * @author Sandor Nemeth
 */
public class MessageListWrapper implements Serializable
{
    private static final long serialVersionUID = 2784869884557215362L;

    private List<MessageDTO> messages;

    public MessageListWrapper()
    {
        // deserialization
    }

    public MessageListWrapper(List<MessageDTO> messages)
    {
        super();
        this.messages = messages;
    }

    public List<MessageDTO> getMessages()
    {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages)
    {
        this.messages = messages;
    }
}

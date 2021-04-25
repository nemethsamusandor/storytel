package se.storytel.messageboard.dto;

import java.io.Serializable;

/**
 * Data transfer object to create a new message
 *
 * @author Sandor Nemeth
 */
public class SaveMessageDTO implements Serializable
{
    private static final long serialVersionUID = -7868559646424834180L;

    private Long id;
    private String text;
    private Long clientId;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }
}

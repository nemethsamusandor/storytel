package se.storytel.messageboard.dto;

import java.io.Serializable;

/**
 * Data transfer object for Message entity
 *
 * @author Sandor Nemeth
 */
public class MessageDTO implements Serializable
{
    private static final long serialVersionUID = 5615604622048511113L;

    private Long id;
    private String text;
    private ClientDTO client;

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

    public ClientDTO getClient()
    {
        return client;
    }

    public void setClient(ClientDTO client)
    {
        this.client = client;
    }
}

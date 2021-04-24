package se.storytel.messageboard.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * Message entity for messages
 *
 * @author Sandor Nemeth
 */
@Entity
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_sequence")
    @SequenceGenerator(name = "message_sequence", sequenceName = "message_sequence", allocationSize = 1)
    private Long id;

    private String text;

    @ManyToOne
    private Client client;

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

    public void setText(String message)
    {
        this.text = message;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }
}

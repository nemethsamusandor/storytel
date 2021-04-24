package se.storytel.messageboard.dto;

import java.io.Serializable;

import se.storytel.messageboard.entity.Client;

/**
 * Data transfer object for {@link Client} entity
 */
public class ClientDTO implements Serializable
{
    private static final long serialVersionUID = 3055813167592334806L;

    private Long id;
    private String name;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}

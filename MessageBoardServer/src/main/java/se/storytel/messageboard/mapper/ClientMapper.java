package se.storytel.messageboard.mapper;

import org.springframework.util.Assert;

import se.storytel.messageboard.dto.ClientDTO;
import se.storytel.messageboard.entity.Client;

/**
 * Mapping {@link Client} entity and {@link ClientDTO}
 *
 * @author Sandor Nemeth
 */
public class ClientMapper
{
    private ClientMapper()
    {
        // Hide public constructor
    }

    public static ClientDTO mapEntityToDTO(Client entity)
    {
        Assert.notNull(entity, "Client is null");

        ClientDTO dto = new ClientDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    public static Client mapDTOToEntity(ClientDTO dto)
    {
        Assert.notNull(dto, "ClientDTO is null");

        Client entity = new Client();

        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }
}

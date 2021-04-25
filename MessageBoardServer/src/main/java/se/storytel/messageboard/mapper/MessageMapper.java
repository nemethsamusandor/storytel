package se.storytel.messageboard.mapper;

import org.springframework.util.Assert;

import se.storytel.messageboard.dto.MessageDTO;
import se.storytel.messageboard.entity.Message;

/**
 * Mapping {@link Message} entity and {@link MessageDTO}
 *
 * @author Sandor Nemeth
 */
public class MessageMapper
{
    private MessageMapper()
    {
        // Hide constructor, because this class has only static methods
    }

    /**
     * Map {@link Message} entity to {@link MessageDTO}
     *
     * @param entity   entity
     * @return Mapped DTO
     */
    public static MessageDTO mapEntityToDTO(Message entity)
    {
        Assert.notNull(entity, "Entity must not be null");
        Assert.notNull(entity.getClient(), "Client id must not be null");

        MessageDTO dto = new MessageDTO();

        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setClient(ClientMapper.mapEntityToDTO(entity.getClient()));

        return dto;
    }

    /**
     * Map {@link MessageDTO} to {@link Message} entity
     *
     * @param dto   dto
     * @return Mapped entity
     */
    public static Message mapDTOToEntity(MessageDTO dto)
    {
        Assert.notNull(dto, "DTO must not be null");
        Assert.notNull(dto.getClient(), "Client id must not be null");

        Message entity = new Message();

        entity.setId(dto.getId());
        entity.setText(dto.getText());
        entity.setClient(ClientMapper.mapDTOToEntity(dto.getClient()));

        return entity;
    }
}

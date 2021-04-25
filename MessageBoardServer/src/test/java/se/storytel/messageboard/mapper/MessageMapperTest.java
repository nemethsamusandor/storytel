package se.storytel.messageboard.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import se.storytel.messageboard.dto.ClientDTO;
import se.storytel.messageboard.dto.MessageDTO;
import se.storytel.messageboard.entity.Client;
import se.storytel.messageboard.entity.Message;

/**
 * Unit test for {@link MessageMapper}
 *
 * @author Sandor Nemeth
 */
class MessageMapperTest
{
    private final String MESSAGE_TEXT = "Message_";

    private final String USERNAME = "username_";
    private final String PASSWORD = "pwd_";

    @Test
    void mapEntityToDTO()
    {
        Long messageId = 1L;
        Long clientId = 1L;

        Message entity = new Message();
        entity.setId(messageId);
        entity.setText(MESSAGE_TEXT + messageId);
        entity.setClient(getClient(clientId));

        MessageDTO dto = MessageMapper.mapEntityToDTO(entity);

        assertEquals(messageId, dto.getId());
        assertEquals(MESSAGE_TEXT + messageId, dto.getText());
        assertNotNull(dto.getClient());

        assertEquals(clientId, dto.getClient().getId());
        assertEquals(USERNAME + clientId, dto.getClient().getUsername());
        assertEquals(PASSWORD + clientId, dto.getClient().getPassword());
        assertTrue(dto.getClient().isEnabled());
    }

    @Test
    void mapDTOToEntity()
    {
        Long messageId = 1L;
        Long clientId = 1L;

        MessageDTO dto = new MessageDTO();
        dto.setText(MESSAGE_TEXT + messageId);
        dto.setId(messageId);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);
        clientDTO.setUsername(USERNAME + clientId);
        clientDTO.setPassword(PASSWORD + clientId);
        clientDTO.setEnabled(true);

        dto.setClient(clientDTO);

        Message entity = MessageMapper.mapDTOToEntity(dto);

        assertEquals(messageId, entity.getId());
        assertEquals(MESSAGE_TEXT + messageId, entity.getText());
        assertNotNull(entity.getClient());

        assertEquals(clientId, entity.getClient().getId());
        assertEquals(USERNAME + clientId, entity.getClient().getUsername());
        assertEquals(PASSWORD + clientId, entity.getClient().getPassword());
        assertTrue(entity.getClient().isEnabled());
    }

    @Test
    void mapEntityToDTOEntityIsNullTest()
    {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> MessageMapper.mapEntityToDTO(null));

        assertEquals("Entity must not be null", exception.getMessage());
    }

    @Test
    void mapEntityToDTOClientIdIsNullTest()
    {
        Exception exception =
            assertThrows(Exception.class, () -> MessageMapper.mapEntityToDTO(new Message()));

        assertEquals("Client id must not be null", exception.getMessage());
    }

    @Test
    void mapDTOToEntityDTOIsNullTest()
    {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> MessageMapper.mapDTOToEntity(null));

        assertEquals("DTO must not be null", exception.getMessage());
    }

    @Test
    void mapDTOToEntityClientIdIsNullTest()
    {
        Exception exception =
            assertThrows(Exception.class, () -> MessageMapper.mapDTOToEntity(new MessageDTO()));

        assertEquals("Client id must not be null", exception.getMessage());
    }

    private Client getClient(Long clientId)
    {
        Client client = new Client();
        client.setId(clientId);
        client.setUsername(USERNAME + clientId);
        client.setPassword(PASSWORD + clientId);
        client.setEnabled(true);

        return client;
    }
}

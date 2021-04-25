package se.storytel.messageboard.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import se.storytel.messageboard.dto.ClientDTO;
import se.storytel.messageboard.entity.Client;

/**
 * Unit test for {@link ClientMapper} class
 *
 * @author Sandor Nemeth
 */
class ClientMapperTest
{
    private static final String USERNAME = "username";
    private static final String PASSWORD = "pwd";
    private static final Long ID = 1L;

    @Test
    void mapEntityToDTO()
    {
        Client entity = new Client();
        entity.setId(ID);
        entity.setUsername(USERNAME);
        entity.setPassword(PASSWORD);
        entity.setEnabled(true);

        ClientDTO dto = ClientMapper.mapEntityToDTO(entity);

        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(USERNAME, dto.getUsername());
        assertEquals(PASSWORD, dto.getPassword());
        assertTrue(dto.isEnabled());
    }

    @Test
    void mapDTOToEntity()
    {
        ClientDTO dto = new ClientDTO();
        dto.setId(ID);
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);
        dto.setEnabled(true);

        Client entity = ClientMapper.mapDTOToEntity(dto);

        assertNotNull(entity);
        assertEquals(ID, entity.getId());
        assertEquals(USERNAME, entity.getUsername());
        assertEquals(PASSWORD, entity.getPassword());
        assertTrue(entity.isEnabled());
    }

    @Test
    void mapEntityToDTOEntityIsNullTest()
    {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> ClientMapper.mapEntityToDTO(null));

        assertEquals("Entity must not be null", exception.getMessage());
    }

    @Test
    void mapDTOToEntityDTOIsNullTest()
    {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> ClientMapper.mapDTOToEntity(null));

        assertEquals("DTO must not be null", exception.getMessage());
    }
}

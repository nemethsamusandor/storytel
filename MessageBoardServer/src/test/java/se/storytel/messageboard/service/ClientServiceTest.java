package se.storytel.messageboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.storytel.messageboard.entity.Client;
import se.storytel.messageboard.exception.ClientNotFoundException;
import se.storytel.messageboard.repository.ClientRepository;

/**
 * Unit test for {@link ClientService} class
 *
 * @author Sandor Nemeth
 */
@ExtendWith(MockitoExtension.class)
class ClientServiceTest
{
    private ClientService service;

    @Mock
    private ClientRepository repository;

    @BeforeEach
    private void init()
    {
        service = new ClientService(repository);
    }

    @Test
    void getClientIdByUsername()
    {
        Client client = new Client();
        client.setId(1L);
        doReturn(Optional.of(client)).when(repository).findByUsername(anyString());

        Long clientId = service.getClientIdByUsername(anyString());

        verify(repository, atLeastOnce()).findByUsername(anyString());
        assertEquals(1L, clientId);
    }

    @Test
    void getClientIdByUsername_ClientNotFound()
    {
        doReturn(Optional.empty()).when(repository).findByUsername(anyString());

        Exception exception = assertThrows(Exception.class, () -> service.getClientIdByUsername(anyString()));

        verify(repository, atLeastOnce()).findByUsername(anyString());
        assertTrue(exception instanceof ClientNotFoundException);
    }

}

package se.storytel.messageboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import se.storytel.messageboard.dto.MessageDTO;
import se.storytel.messageboard.dto.SaveMessageDTO;
import se.storytel.messageboard.entity.Client;
import se.storytel.messageboard.entity.Message;
import se.storytel.messageboard.exception.ClientNotFoundException;
import se.storytel.messageboard.exception.MessageNotFoundException;
import se.storytel.messageboard.repository.ClientRepository;
import se.storytel.messageboard.repository.MessageRepository;

/**
 * Unit test for {@link MessageService} class
 *
 * @author Sandor Nemeth
 */
@ExtendWith(MockitoExtension.class)
class MessageServiceTest
{
    private MessageService service;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    private void init()
    {
        service = new MessageService(messageRepository, clientRepository);
    }

    @Test
    void findAllMessage()
    {
        doReturn(new ArrayList<>()).when(messageRepository).findAll();

        List<Message> messages = service.findAllMessage();

        verify(messageRepository, atLeastOnce()).findAll();
        assertNotNull(messages);
    }

    @Test
    void findMessagesByClientId()
    {
        doReturn(new ArrayList<>()).when(messageRepository).findMessagesByClientId(Mockito.anyLong());

        List<Message> messages = service.findMessagesByClientId(1L);

        verify(messageRepository, atLeastOnce()).findMessagesByClientId(Mockito.anyLong());
        assertNotNull(messages);
    }

    @Test
    void deleteMessageByIdAndClientId_MessageIdNull()
    {
        Exception exception = assertThrows(Exception.class, () -> service.deleteMessageByIdAndClientId(null, null));

        assertEquals("MessageId must not be null!", exception.getMessage());
    }

    @Test
    void deleteMessageByIdAndClientId_ClientIdNull()
    {
        Exception exception = assertThrows(Exception.class, () -> service.deleteMessageByIdAndClientId(1L, null));

        assertEquals("ClientId must not be null!", exception.getMessage());
    }

    @Test
    void deleteMessageByIdAndClientId_MessageNotFound()
    {
        doReturn(Optional.empty())
            .when(messageRepository).findMessageByIdAndClientId(Mockito.anyLong(), Mockito.anyLong());

        Exception exception = assertThrows(Exception.class,
                () -> service.deleteMessageByIdAndClientId(Mockito.anyLong(), Mockito.anyLong()));

        verify(messageRepository, atLeastOnce()).findMessageByIdAndClientId(Mockito.anyLong(), Mockito.anyLong());
        assertTrue(exception instanceof MessageNotFoundException);
    }

    @Test
    void deleteMessageByIdAndClientId()
    {
        when(messageRepository.findMessageByIdAndClientId(Mockito.anyLong(), Mockito.anyLong()))
            .thenReturn(Optional.of(new Message()));

        service.deleteMessageByIdAndClientId(Mockito.anyLong(), Mockito.anyLong());

        verify(messageRepository, atLeastOnce()).deleteById(Mockito.anyLong());
    }

    @Test
    void saveMessage_DTOIsNull()
    {
        Exception exception = assertThrows(Exception.class, () -> service.saveMessage(null));

        assertEquals("DTO must not be null", exception.getMessage());
    }

    @Test
    void saveMessage_ClientIdIsNull()
    {
        Exception exception = assertThrows(Exception.class, () -> service.saveMessage(new SaveMessageDTO()));

        assertEquals("ClientId must not be null", exception.getMessage());
    }

    @Test
    void saveMessage_ClientNotFound()
    {
        doReturn(Optional.empty()) .when(clientRepository).findById(Mockito.anyLong());

        SaveMessageDTO dto = new SaveMessageDTO();
        dto.setClientId(Mockito.anyLong());

        Exception exception = assertThrows(Exception.class,
            () -> service.saveMessage(dto));

        verify(clientRepository, atLeastOnce()).findById(Mockito.anyLong());
        assertTrue(exception instanceof ClientNotFoundException);
    }

    @Test
    void saveMessage()
    {
        doReturn(Optional.of(new Client())).when(clientRepository).findById(Mockito.anyLong());

        Client client = new Client();
        client.setId(1L);

        Message message = new Message();
        message.setClient(client);

        doReturn(message).when(messageRepository).save(Mockito.any(Message.class));

        SaveMessageDTO dto = new SaveMessageDTO();
        dto.setClientId(Mockito.anyLong());

        MessageDTO savedMessage = service.saveMessage(dto);

        verify(clientRepository, atLeastOnce()).findById(Mockito.anyLong());
        verify(messageRepository, atLeastOnce()).save(Mockito.any(Message.class));
        assertNotNull(savedMessage);
    }

    @Test
    void findMessageByIdAndClientId_DTOIsNull()
    {
        Exception exception = assertThrows(Exception.class, () -> service.findMessageByIdAndClientId(null));

        assertEquals("DTO must not be null!", exception.getMessage());
    }

    @Test
    void findMessageByIdAndClientId_MessageIdIsNull()
    {
        Exception exception = assertThrows(Exception.class,
            () -> service.findMessageByIdAndClientId(new SaveMessageDTO()));

        assertEquals("MessageId must not be null!", exception.getMessage());
    }

    @Test
    void findMessageByIdAndClientId_ClientIdIsNull()
    {
        SaveMessageDTO dto = new SaveMessageDTO();
        dto.setId(1L);

        Exception exception = assertThrows(Exception.class, () -> service.findMessageByIdAndClientId(dto));

        assertEquals("ClientId must not be null!", exception.getMessage());
    }

    @Test
    void findMessageByIdAndClientId()
    {
        SaveMessageDTO dto = new SaveMessageDTO();
        dto.setId(1L);
        dto.setClientId(1L);

        when(messageRepository.findMessageByIdAndClientId(Mockito.anyLong(), Mockito.anyLong()))
            .thenReturn(Optional.of(new Message()));

        Message message = service.findMessageByIdAndClientId(dto);

        verify(messageRepository, atLeastOnce()).findMessageByIdAndClientId(Mockito.anyLong(), Mockito.anyLong());
        assertNotNull(message);
    }

}

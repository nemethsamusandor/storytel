package se.storytel.messageboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import se.storytel.messageboard.dto.MessageDTO;
import se.storytel.messageboard.dto.SaveMessageDTO;
import se.storytel.messageboard.entity.Client;
import se.storytel.messageboard.entity.Message;
import se.storytel.messageboard.exception.ClientNotFoundException;
import se.storytel.messageboard.exception.MessageNotFoundException;
import se.storytel.messageboard.mapper.MessageMapper;
import se.storytel.messageboard.repository.ClientRepository;
import se.storytel.messageboard.repository.MessageRepository;

/**
 * Service class for Message entity related methods
 *
 * @author Sandor Nemeth
 */
@Service
public class MessageService
{
    private MessageRepository messageRepository;

    private ClientRepository clientRepository;

    @Autowired
    public MessageService(MessageRepository repository, ClientRepository clientRepository)
    {
        this.messageRepository = repository;
        this.clientRepository = clientRepository;
    }

    public List<Message> findAllMessage()
    {
        return messageRepository.findAll();
    }

    public List<Message> findMessagesByClientId(Long id)
    {
        return messageRepository.findMessagesByClientId(id);
    }

    public void deleteMessageByIdAndClientId(Long id, Long clientId)
    {
        Assert.notNull(id, "MessageId must not be null!");
        Assert.notNull(clientId, "ClientId must not be null!");

        findMessageByIdAndClientId(id, clientId);
        messageRepository.deleteById(id);
    }

    public MessageDTO saveMessage(SaveMessageDTO dto)
    {
        Assert.notNull(dto, "DTO must not be null");
        Assert.notNull(dto.getClientId(), "ClientId must not be null");

        // Load client data and throw ClientNotFoundException if client is not exist
        Client client = clientRepository.findById(dto.getClientId())
            .orElseThrow(ClientNotFoundException::new);

        Message savedEntity = messageRepository.save(getMessage(dto.getId(), dto.getText(), client));

        return MessageMapper.mapEntityToDTO(savedEntity);
    }

    public Message findMessageByIdAndClientId(SaveMessageDTO dto)
    {
        Assert.notNull(dto, "DTO must not be null!");
        Assert.notNull(dto.getId(), "MessageId must not be null!");
        Assert.notNull(dto.getClientId(), "ClientId must not be null!");

        return findMessageByIdAndClientId(dto.getId(), dto.getClientId());
    }

    private Message findMessageByIdAndClientId(Long id, Long clientId)
    {
        return messageRepository.findMessageByIdAndClientId(id, clientId)
            .orElseThrow(MessageNotFoundException::new);
    }

    private Message getMessage(Long id, String text, Client client)
    {
        Message message = new Message();
        message.setId(id);
        message.setText(text);
        message.setClient(client);

        return message;
    }
}

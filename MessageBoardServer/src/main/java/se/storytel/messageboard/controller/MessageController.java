package se.storytel.messageboard.controller;

import java.security.Principal;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.storytel.messageboard.dto.ErrorResponseWrapper;
import se.storytel.messageboard.dto.MessageDTO;
import se.storytel.messageboard.dto.MessageListWrapper;
import se.storytel.messageboard.dto.SaveMessageDTO;
import se.storytel.messageboard.dto.UserDTO;
import se.storytel.messageboard.mapper.MessageMapper;
import se.storytel.messageboard.service.ClientService;
import se.storytel.messageboard.service.MessageService;

/**
 * Message board Rest API mappings
 *
 * @author Sandor Nemeth
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController
{
    private final MessageService messageService;
    private final ClientService clientService;

    @Autowired
    public MessageController(MessageService messageService, ClientService clientService)
    {
        this.messageService = messageService;
        this.clientService = clientService;
    }

    @PostMapping ("/login")
    public boolean login(@RequestBody UserDTO user)
    {
        return clientService.validAuthentication(user.getUsername(), user.getPassword());
    }
    /**
     * Find all messages independently of client
     *
     * @return {@link MessageListWrapper} class contains the list of all messages
     */
    @GetMapping("/messages")
    public ResponseEntity<MessageListWrapper> findAllMessages()
    {
        return ResponseEntity.ok(
            new MessageListWrapper(
                messageService.findAllMessage().stream()
                    .map(MessageMapper::mapEntityToDTO)
                    .collect(Collectors.toList())
            )
        );
    }

    /**
     * Find all messages of a client
     *
     * @return {@link MessageListWrapper} class contains the list of messages
     */
    @GetMapping("messages/client")
    public ResponseEntity<MessageListWrapper> findClientMessages(Principal principal)
    {
        return ResponseEntity.ok(
            new MessageListWrapper(
                messageService.findMessagesByClientId(getClientId(principal)).stream()
                    .map(MessageMapper::mapEntityToDTO)
                    .collect(Collectors.toList())
            )
        );
    }

    /**
     * Create a new message
     *
     * @param message   Message to save
     * @return  Saved message with status
     */
    @PostMapping(value = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createMessage(@RequestBody SaveMessageDTO message, Principal principal)
    {
        if (message.getId() != null)
        {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(
                new ErrorResponseWrapper("Message id must not be set for adding new message",
                    status.toString()), status);
        }

        message.setClientId(getClientId(principal));

        return new ResponseEntity<>(messageService.saveMessage(message), HttpStatus.CREATED);
    }

    /**
     * Modify message by message id and client id
     *
     * @param dto    Message
     * @return Saved message with status
     */
    @PutMapping(value="/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> updateMessage(@RequestBody SaveMessageDTO dto, Principal principal)
    {
        dto.setClientId(getClientId(principal));

        // Check if the message with the id is exists for the client with clientId.
        // If not MessageNotFoundException os thrown
        messageService.findMessageByIdAndClientId(dto);

        return ResponseEntity.ok(messageService.saveMessage(dto));
    }

    /**
     * Delete message by id and client id
     *
     * @param id        id of message
     * @return Empty response with status
     */
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable long id, Principal principal)
    {
        messageService.deleteMessageByIdAndClientId(id, getClientId(principal));

        return ResponseEntity.noContent().build();
    }

    /**
     * Get client id by username
     *
     * @param principal Logged in credentials
     * @return  Client id
     */
    public Long getClientId(Principal principal)
    {
        Assert.notNull(principal, "Client credentials must not be null");
        Assert.notNull(principal.getName(), "Client name must not be null");

        return clientService.getClientIdByUsername(principal.getName());
    }

}

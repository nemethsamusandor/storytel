package se.storytel.messageboard.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import se.storytel.messageboard.dto.MessageDTO;
import se.storytel.messageboard.dto.MessageListWrapper;
import se.storytel.messageboard.dto.SaveMessageDTO;
import se.storytel.messageboard.mapper.MessageMapper;
import se.storytel.messageboard.service.MessageService;

/**
 * Message board Rest API mappings
 *
 * @author Sandor Nemeth
 */
@RestController
@RequestMapping(value = "/api/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController
{
    MessageService service;

    @Autowired
    public MessageController (MessageService service)
    {
        this.service = service;
    }

    /**
     * Find all messages independently of client
     *
     * @return {@link MessageListWrapper} class contains the list of all messages
     */
    @GetMapping
    public ResponseEntity<MessageListWrapper> findAllMessages()
    {
        return ResponseEntity.ok(
            new MessageListWrapper(
                service.findAllMessage().stream()
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
    @GetMapping("/client/{id}")
    public ResponseEntity<MessageListWrapper> findClientMessages(@PathVariable long id)
    {
        return ResponseEntity.ok(
            new MessageListWrapper(
                service.findMessagesByClientId(id).stream()
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO createMessage(@RequestBody SaveMessageDTO message)
    {
        return service.saveMessage(message);
    }

    /**
     * Modify message by message id and client id
     *
     * @param dto    Message
     * @return Saved message with status
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> updateMessage(@RequestBody SaveMessageDTO dto)
    {
        // Check if the message with the id is exists for the client with clientId.
        // If not MessageNotFoundException os thrown
        service.findMessageByIdAndClientId(dto);

        return ResponseEntity.ok(service.saveMessage(dto));
    }

    /**
     * Delete message by id and client id
     *
     * @param id        id of message
     * @param clientId  id of client
     * @return Empty response with status
     */
    @DeleteMapping("/{id}/client/{clientId}")
    public ResponseEntity<Object> deleteMessage(@PathVariable long id, @PathVariable long clientId)
    {
        service.deleteMessageByIdAndClientId(id, clientId);

        return ResponseEntity.noContent().build();
    }

}

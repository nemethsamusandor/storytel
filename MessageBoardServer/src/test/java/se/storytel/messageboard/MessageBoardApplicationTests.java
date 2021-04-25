package se.storytel.messageboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import dto.HealthCheckTestDTO;
import se.storytel.messageboard.controller.MessageController;
import se.storytel.messageboard.dto.ErrorResponseWrapper;
import se.storytel.messageboard.dto.MessageDTO;
import se.storytel.messageboard.dto.MessageListWrapper;
import se.storytel.messageboard.dto.SaveMessageDTO;

/**
 * Integration test of the application
 * Data for the tests are loaded from
 *    Message   -> main/resources/db/changelog/data/message_test_data.csv
 *    Client    -> main/resources/db/changelog/data/client_data.csv
 *
 * @author Sandor Nemeth
 */
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageBoardApplicationTests
{
    private static final String HEALTH_PATH = "/api/health";
    private static final String MESSAGES_BASE_PATH = "/api/messages";
    private static final String CLIENT_MESSAGES_PATH = MESSAGES_BASE_PATH + "/client";
    private static final String DELETE_PATH = MESSAGES_BASE_PATH + "/%s";

    private static final String PASSWORD = "password";
    private static final String CLIENT_1 = "Emily";
    private static final String CLIENT_2 = "John";
    private static final String CLIENT_3 = "Sandor";
    private static final String CLIENT_4 = "Jan";

    private static Long id;

    @Autowired
    MessageController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Order(1)
    @Test
    void contextLoads()
    {
        assertThat(controller).isNotNull();
    }

    @Order(2)
    @Test
    void serviceHealthCheckTest()
    {
        ResponseEntity<HealthCheckTestDTO> responseEntity = restTemplate.getForEntity(HEALTH_PATH,
            HealthCheckTestDTO.class);

        assertNotNull(responseEntity.getBody());
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("UP", responseEntity.getBody().getStatus());
    }

    /**
     * Get All messages of the service
     */
    @Order(3)
    @Test
    void findAllMessages()
    {
        HttpEntity<Object> request = new HttpEntity<>(null, getHeaders(CLIENT_1));

        ResponseEntity<MessageListWrapper> responseEntity = restTemplate.exchange(MESSAGES_BASE_PATH, HttpMethod.GET,
            request, MessageListWrapper.class);

        assertSame(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getMessages());

        assertEquals(7, responseEntity.getBody().getMessages().size());
    }

    /**
     * Get All messages of a client
     */
    @Order(4)
    @Test
    void findClientMessages()
    {
        // Get Messages of Client "John"
        HttpEntity<Object> request = new HttpEntity<>(null, getHeaders(CLIENT_2));

        ResponseEntity<MessageListWrapper> responseEntity = restTemplate.exchange(CLIENT_MESSAGES_PATH, HttpMethod.GET,
            request, MessageListWrapper.class);

        assertSame(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getMessages());

        assertEquals(1, responseEntity.getBody().getMessages().size());
        MessageDTO responseDto = responseEntity.getBody().getMessages().get(0);
        assertEquals("Test message 3", responseDto.getText());
        assertEquals(2, responseDto.getClient().getId());

        // Get Messages of Client "Sandor"
        request = new HttpEntity<>(null, getHeaders(CLIENT_3));

        responseEntity = restTemplate.exchange(CLIENT_MESSAGES_PATH, HttpMethod.GET, request, MessageListWrapper.class);

        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getMessages());
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(4, responseEntity.getBody().getMessages().size());
    }

    /**
     * Create a new client message
     */
    @Order(5)
    @Test
    void createMessageTest()
    {
        String newMessageText = "New message";

        SaveMessageDTO messageDTO = new SaveMessageDTO();
        messageDTO.setText(newMessageText);
        messageDTO.setClientId(controller.getClientId(() -> CLIENT_2));

        HttpEntity<SaveMessageDTO> request = new HttpEntity<>(messageDTO, getHeaders(CLIENT_2));

        ResponseEntity<MessageDTO> responseEntity =
            restTemplate.postForEntity(MESSAGES_BASE_PATH, request, MessageDTO.class);

        assertSame(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        MessageDTO savedMessage = responseEntity.getBody();
        assertEquals(newMessageText, savedMessage.getText());

        id = savedMessage.getId();

        assertNotNull(savedMessage.getClient());
        assertEquals(messageDTO.getClientId(), savedMessage.getClient().getId());
        assertEquals(CLIENT_2, savedMessage.getClient().getUsername());
    }

    /**
     * Update a client message
     */
    @Order(6)
    @Test
    void updateMessageTest()
    {
        String modifiedMessageText = "Modified message";

        SaveMessageDTO messageDTO = new SaveMessageDTO();
        messageDTO.setId(id);
        messageDTO.setText(modifiedMessageText);
        messageDTO.setClientId(controller.getClientId(() -> CLIENT_2));

        HttpEntity<SaveMessageDTO> request = new HttpEntity<>(messageDTO, getHeaders(CLIENT_2));

        ResponseEntity<MessageDTO> responseEntity = restTemplate.exchange(MESSAGES_BASE_PATH, HttpMethod.PUT, request, MessageDTO.class);

        assertSame(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        MessageDTO savedMessage = responseEntity.getBody();
        assertEquals(modifiedMessageText, savedMessage.getText());

        assertNotNull(savedMessage.getClient());
        assertEquals(messageDTO.getClientId(), savedMessage.getClient().getId());
        assertEquals(CLIENT_2, savedMessage.getClient().getUsername());
    }

    /**
     * Delete a client message
     */
    @Order(7)
    @Test
    void deleteMessageTest()
    {
        HttpEntity<String> request = new HttpEntity<>("", getHeaders(CLIENT_2));

        String path = String.format(DELETE_PATH, id);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(path, HttpMethod.DELETE, request,
            Object.class);

        assertNotNull(responseEntity);
        assertSame(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    /**
     * Get All messages not authorized
     */
    @Test
    void findAllMessagesNotAuthorized()
    {
        HttpEntity<Object> request = new HttpEntity<>(null, getHeaders(CLIENT_4));

        ResponseEntity<MessageListWrapper> responseEntity = restTemplate.exchange(MESSAGES_BASE_PATH, HttpMethod.GET,
            request, MessageListWrapper.class);

        assertSame(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    /**
     * Create message with message id
     */
    @Test
    void createMessageWithMessageIdTest()
    {
        String newMessageText = "New message";

        SaveMessageDTO messageDTO = new SaveMessageDTO();
        messageDTO.setId(1L);
        messageDTO.setText(newMessageText);
        messageDTO.setClientId(controller.getClientId(() -> CLIENT_2));

        HttpEntity<SaveMessageDTO> request = new HttpEntity<>(messageDTO, getHeaders(CLIENT_2));

        ResponseEntity<ErrorResponseWrapper> responseEntity =
            restTemplate.postForEntity(MESSAGES_BASE_PATH, request, ErrorResponseWrapper.class);

        assertSame(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        ErrorResponseWrapper savedMessage = responseEntity.getBody();
        assertEquals("Message id must not be set for adding new message", savedMessage.getMessage());
    }

    /**
     * Try to delete another client's message
     */
    @Order(8)
    @Test
    void deleteMessageOfOtherClientTest()
    {
        HttpEntity<String> request = new HttpEntity<>("", getHeaders(CLIENT_2));

        String path = String.format(DELETE_PATH, 1);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(path, HttpMethod.DELETE, request,
            Object.class);

        assertNotNull(responseEntity);
        assertSame(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Try to update another client's message
     */
    @Order(9)
    @Test
    void updateMessageOfOtherClientTest()
    {
        String modifiedMessageText = "Modified message";

        SaveMessageDTO messageDTO = new SaveMessageDTO();
        messageDTO.setId(1L);
        messageDTO.setText(modifiedMessageText);
        messageDTO.setClientId(controller.getClientId(() -> CLIENT_2));

        HttpEntity<SaveMessageDTO> request = new HttpEntity<>(messageDTO, getHeaders(CLIENT_2));

        ResponseEntity<MessageDTO> responseEntity = restTemplate.exchange(MESSAGES_BASE_PATH, HttpMethod.PUT, request, MessageDTO.class);

        assertSame(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Call interface with wrong method type
     */
    @Order(10)
    @Test
    void methodNotAllowedTest()
    {
        String modifiedMessageText = "Modified message";

        SaveMessageDTO messageDTO = new SaveMessageDTO();
        messageDTO.setId(1L);
        messageDTO.setText(modifiedMessageText);
        messageDTO.setClientId(controller.getClientId(() -> CLIENT_1));

        HttpEntity<SaveMessageDTO> request = new HttpEntity<>(messageDTO, getHeaders(CLIENT_1));

        ResponseEntity<MessageDTO> responseEntity = restTemplate.exchange(MESSAGES_BASE_PATH, HttpMethod.DELETE, request, MessageDTO.class);

        assertSame(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
    }

    /**
     * Id is not provided to delete
     */
    @Order(11)
    @Test
    void missingIdForDeleteTest()
    {
        HttpEntity<String> request = new HttpEntity<>("", getHeaders(CLIENT_2));

        String path = String.format(DELETE_PATH, (Object) null);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(path, HttpMethod.DELETE, request,
            Object.class);

        assertNotNull(responseEntity);
        assertSame(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    private HttpHeaders getHeaders(String username)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String authorizationHeader = "Basic " +
            DatatypeConverter.printBase64Binary((username + ":" + PASSWORD).getBytes());

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authorizationHeader);

        return headers;
    }
}

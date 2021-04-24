package se.storytel.messageboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;

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
    private static final String CLIENT_MESSAGES_PATH = MESSAGES_BASE_PATH + "/client/%s";
    private static final String DELETE_PATH = MESSAGES_BASE_PATH + "/%s/client/%s";

    private static Long id;
    private static Long clientId = 4L;

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
        ResponseEntity<MessageListWrapper> responseEntity = restTemplate.getForEntity(MESSAGES_BASE_PATH,
            MessageListWrapper.class);

        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getMessages());
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(7, responseEntity.getBody().getMessages().size());
    }

    /**
     * Get All messages of a client
     */
    @Order(4)
    @Test
    void findClientMessages()
    {
        // Get Messages of Client with id 2
        ResponseEntity<MessageListWrapper> responseEntity =
            restTemplate.getForEntity(String.format(CLIENT_MESSAGES_PATH, 2), MessageListWrapper.class);

        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getMessages());
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(1, responseEntity.getBody().getMessages().size());
        MessageDTO responseDto = responseEntity.getBody().getMessages().get(0);
        assertEquals("Test message 3", responseDto.getMessage());
        assertEquals(2, responseDto.getClient().getId());

        // Get Messages of Client with id 3
        responseEntity = restTemplate.getForEntity(String.format(CLIENT_MESSAGES_PATH, 3), MessageListWrapper.class);

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
        messageDTO.setMessage(newMessageText);
        messageDTO.setClientId(clientId);

        HttpEntity<SaveMessageDTO> request = new HttpEntity<>(messageDTO, getHeaders());

        ResponseEntity<MessageDTO> responseEntity =
            restTemplate.postForEntity(MESSAGES_BASE_PATH, request, MessageDTO.class);

        assertSame(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        MessageDTO savedMessage = responseEntity.getBody();
        assertEquals(newMessageText, savedMessage.getMessage());

        id = savedMessage.getId();

        assertNotNull(savedMessage.getClient());
        assertEquals(clientId, savedMessage.getClient().getId());
        assertEquals("Lili", savedMessage.getClient().getName());
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
        messageDTO.setMessage(modifiedMessageText);
        messageDTO.setClientId(clientId);

        HttpEntity<SaveMessageDTO> request = new HttpEntity<>(messageDTO, getHeaders());

        ResponseEntity<MessageDTO> responseEntity = restTemplate.exchange(MESSAGES_BASE_PATH, HttpMethod.PUT, request, MessageDTO.class);

        assertSame(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        MessageDTO savedMessage = responseEntity.getBody();
        assertEquals(modifiedMessageText, savedMessage.getMessage());

        assertNotNull(savedMessage.getClient());
        assertEquals(clientId, savedMessage.getClient().getId());
        assertEquals("Lili", savedMessage.getClient().getName());
    }

    /**
     * Update a client message
     */
    @Order(7)
    @Test
    void deleteMessageTest()
    {
        HttpEntity<String> request = new HttpEntity<>("", getHeaders());

        String path = String.format(DELETE_PATH, id, clientId);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(path, HttpMethod.DELETE, request,
            Object.class);

        assertNotNull(responseEntity);
        assertSame(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    private HttpHeaders getHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}

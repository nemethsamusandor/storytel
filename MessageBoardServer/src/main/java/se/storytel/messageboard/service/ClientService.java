package se.storytel.messageboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.storytel.messageboard.entity.Client;
import se.storytel.messageboard.exception.ClientNotFoundException;
import se.storytel.messageboard.repository.ClientRepository;

/**
 * Service for {@link Client} repository
 *
 * @author Sandor Nemeth
 */
@Service
public class ClientService
{
    private ClientRepository repository;

    @Autowired
    public ClientService(ClientRepository repository)
    {
        this.repository = repository;
    }

    public Long getClientIdByUsername(String username)
    {
        Client client = repository.findByUsername(username)
            .orElseThrow(ClientNotFoundException::new);

        return client.getId();
    }
}

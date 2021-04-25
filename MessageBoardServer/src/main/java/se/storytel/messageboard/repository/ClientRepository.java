package se.storytel.messageboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.storytel.messageboard.entity.Client;

/**
 * Repository class for Client entity related processes
 *
 * @author Sandor Nemeth
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long>
{
    Optional<Client> findByUsername(String username);

    Client findByUsernameAndPassword(String username,String password);
}

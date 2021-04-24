package se.storytel.messageboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.storytel.messageboard.entity.Message;

/**
 * Repository class for Message entity related processes
 *
 * @author Sandor Nemeth
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long>
{
    Optional<Message> findMessageByIdAndClientId(Long id, Long clientId);

    List<Message> findMessagesByClientId(Long clientId);
}
